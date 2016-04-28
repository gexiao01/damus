// 文件名称: RddCalculateManager.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.service.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import org.apache.spark.Accumulator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import scala.Tuple2;

import com.google.common.collect.ListMultimap;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.common.util.Pair;
import com.ximalaya.damus.offline.adapt.model.AdaptRule;
import com.ximalaya.damus.offline.global.DictUnknownConfig;
import com.ximalaya.damus.offline.spark.DataAdaptMap;
import com.ximalaya.damus.offline.spark.DimTypeDataToReduceFlowMap;
import com.ximalaya.damus.offline.spark.EntryDataMapper;
import com.ximalaya.damus.offline.spark.EntryDataReducer;
import com.ximalaya.damus.offline.spark.AbstractFlowFilter;
import com.ximalaya.damus.offline.spark.AbstractDimTypeMapper;
import com.ximalaya.damus.offline.spark.LineToFlowConverter;
import com.ximalaya.damus.offline.spark.NotInDictFlatMap;
import com.ximalaya.damus.offline.spark.ReducedFlowSpliter;
import com.ximalaya.damus.offline.spark.ReducedFlowToByteArrMap;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.dist.OneDDist;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.resource.Resource;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月11日
 */
public class CalculateManager<F> implements InitializingBean {

	static final Logger logger = LoggerFactory.getLogger(CalculateManager.class);

	@Autowired
	@Qualifier("dimDictResource")
	private Resource<DimDict> dimDict;

	@Value("${damus.offline.sample.raw}")
	private double rawSampleRatio;
	@Value("${damus.offline.sample.effect}")
	private double effectSampleRatio;
	@Value("${damus.offline.sample.seed}")
	private long sampleSeed;

	@Value("${damus.offline.repartition}")
	private int repartitionNum;
	@Value("${damus.offline.spark.minpartition}")
	private int minPartition;

	private Class<F> fClass;
	private AbstractFlowFilter<F> flowFilter;
	private AbstractDimTypeMapper<F> dimTypeMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (sampleSeed <= 0) {
			sampleSeed = new Random().nextLong();
		}
	}

	/**
	 * rdd整体计算流程管理器 在CalcServiceSpark里做也可以，不过出于Serializable的考虑，还是抽出一个类来
	 * 
	 * @param dimDictDelta
	 * @param adaptData
	 * @param totalDist
	 *            as part of output, send new OneDDist()
	 * @throws DamusException
	 */
	public JavaRDD<byte[]> calculate(JavaRDD<String> lines, Accumulator<DimDict> dimDictDelta,
			Broadcast<ListMultimap<DimType, AdaptRule>> adaptData, OneDDist dist) throws DamusException {

		// System.err.println(lines.take(20));
		// 反序列化
		JavaRDD<F> flows = lines.sample(false, rawSampleRatio, sampleSeed).map(new LineToFlowConverter<F>(fClass));
		// System.err.println(flows.take(20));

		// 自定义过滤
		JavaRDD<F> filteredFlows = flows.filter(flowFilter).sample(false, effectSampleRatio, sampleSeed);

		if (repartitionNum > 0) {
			filteredFlows = filteredFlows.repartition(Math.max(repartitionNum, minPartition));
		}
		// System.err.println(filteredFlows.take(20));

		// flows.unpersist();
		// firstFilterFlows.cache();
		// logger.info("Filtered Flows: " + firstFilterFlows.count());

		// 自定义提取字段。将过滤完的flows转换为EnumMap<DimType, String>，方便与字典比对.
		JavaRDD<EnumMap<DimType, String>> dimTypeValues = filteredFlows.map(dimTypeMapper);
		// firstFilterFlows.unpersist();

		// 自定义适配，根据xml规则
		JavaRDD<EnumMap<DimType, String>> adaptedDimTypeObjects = dimTypeValues.map(new DataAdaptMap(adaptData));
		// System.err.println(adaptedDimTypeObjects.take(20));

		// 将字典中未有的数据筛选出来，merge到全局维护的增量字典中
		JavaRDD<EnumMap<DimType, String>> filterNotInDict = adaptedDimTypeObjects.mapPartitions(new NotInDictFlatMap(
				dimDict.get(), dimDictDelta, DictUnknownConfig.getUnknownConfig()));
		// System.err.println(filterNotInDict.take(20));

		// 将最终合法数据转化为ReduceFlow
		JavaRDD<ReducedFlow> reducedFlows = filterNotInDict.map(new DimTypeDataToReduceFlowMap(dimDict.get()));
		reducedFlows.persist(StorageLevel.MEMORY_AND_DISK());

		System.err.println(reducedFlows.take(20));

		// 接下来分2步进行:1.将ReducedFlow转化为byte[]供actuary使用.
		// 2.统计最精准数据OneDDist
		// 进行第一步转化.
		JavaRDD<byte[]> reducedFlowFinalData = reducedFlows.map(new ReducedFlowToByteArrMap());

		// 进行第二步转化. 将ReducedFlow数据打散,转化为一个个Entry<DimType, Long>
		JavaRDD<Pair<DimType, Long>> entryRDD = reducedFlows.flatMap(new ReducedFlowSpliter());

		reducedFlows.unpersist();

		JavaPairRDD<Pair<DimType, Long>, Long> entryPairRDD = entryRDD.mapToPair(new EntryDataMapper());

		JavaPairRDD<Pair<DimType, Long>, Long> reduceEntryData = entryPairRDD.reduceByKey(new EntryDataReducer());

		List<Tuple2<Pair<DimType, Long>, Long>> oneddists = reduceEntryData.collect();

		// rebuild OneDDist
		for (Tuple2<Pair<DimType, Long>, Long> tuple2 : oneddists) {
			logger.info("Put onddist: " + tuple2);
			dist.put(tuple2._1.getA(), tuple2._1.getB(), tuple2._2);
		}

		// entryRDD.collect();
		// 触发action算子
		// reducedFlows.collect();

		// if (logger.isDebugEnabled()) {
		// logger.debug("calculate lines total time is " +
		// (System.currentTimeMillis() - l1));
		// }
		return reducedFlowFinalData;
	}

	public Class<F> getfClass() {
		return fClass;
	}

	public void setfClass(Class<F> fClass) {
		this.fClass = fClass;
	}

	public AbstractFlowFilter<F> getFlowFilter() {
		return flowFilter;
	}

	public void setFlowFilter(AbstractFlowFilter<F> flowFilter) {
		this.flowFilter = flowFilter;
	}

	public AbstractDimTypeMapper<F> getDimTypeMapper() {
		return dimTypeMapper;
	}

	public void setDimTypeMapper(AbstractDimTypeMapper<F> dimTypeMapper) {
		this.dimTypeMapper = dimTypeMapper;
	}
}
