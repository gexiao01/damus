package com.ximalaya.damus.offline.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ListMultimap;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.common.util.LogMessageBuilder;
import com.ximalaya.damus.common.util.XmlUtil;
import com.ximalaya.damus.offline.adapt.model.AdaptRule;
import com.ximalaya.damus.offline.adapt.model.Rules;
import com.ximalaya.damus.offline.global.DimDictAccumulatorParam;
import com.ximalaya.damus.offline.service.CalcService;
import com.ximalaya.damus.offline.spark.ReducedFlowOutputMapper;
import com.ximalaya.damus.offline.spark.io.ReducedFlowOutputFormat;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.dist.OneDDist;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.resource.Resource;

public class CalcServiceSpark implements CalcService, InitializingBean {

	@Value("#{sparkProps}")
	private Properties props;

	private JavaSparkContext sc;

	@Autowired
	private CalculateManager<?> manager;

	@Autowired
	@Qualifier("dimDictResource")
	private Resource<DimDict> dict;
	@Autowired
	@Qualifier("dimDictDeltaResource")
	private Resource<DimDict> dictDelta;

	@Autowired
	@Qualifier("oneDDistResource")
	private Resource<OneDDist> oneDDistResource;

	@Value("${damus.offline.rawdata.path}")
	private String rawDataPath;
	@Autowired
	@Qualifier("reducedFlowResource")
	private Resource<ReducedFlow> reducedFlowResource;

	@Value("${damus.offline.spark.minpartition}")
	private int minPartition;

	private ListMultimap<DimType, AdaptRule> adaptRuleMap = ImmutableListMultimap.of();

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void afterPropertiesSet() throws Exception {

		/**
		 * 反序列化adapt-rule.xml
		 */
		URL url = getClass().getResource("/adapt-rule.xml");
		logger.info("load adapt-rule.xml from " + url);
		try {
			Rules rules = XmlUtil.xmlToBean(url, Rules.class);
			buildAdaptRuleMap(rules.getAdaptRules());
		} catch (JAXBException e) {
			logger.error("load " + url + " error.", e);
			throw e;
		}

		logger.info("loading SparkConf: " + props);
		SparkConf sparkConf = new SparkConf();
		for (Entry<Object, Object> entry : props.entrySet()) {
			sparkConf.set((String) entry.getKey(), (String) entry.getValue());
		}
		sc = new JavaSparkContext(sparkConf);
	}

	@Override
	public void destroy() throws Exception {
		sc.close();
	}

	@Override
	public DimDict calcReduceflow() throws DamusException, IOException {
		// 定义spark全局未识别字典
		Accumulator<DimDict> dictDeltaAccu = sc.accumulator(new DimDict(), new DimDictAccumulatorParam());

		OneDDist dist = new OneDDist();
		// 定义广播适配数据
		Broadcast<ListMultimap<DimType, AdaptRule>> adaptData = sc.broadcast(adaptRuleMap);

		logger.info("Loading Raw Data: " + rawDataPath);
		JavaRDD<String> lines = sc.textFile(rawDataPath, minPartition);

		JavaRDD<byte[]> reducedData = manager.calculate(lines, dictDeltaAccu, adaptData, dist);

		JavaPairRDD<NullWritable, BytesWritable> reducedDataOutput = reducedData
				.mapToPair(new ReducedFlowOutputMapper());

		// Remove output dir if exists
		String reducedFlowPath = reducedFlowResource.getPath();
		if (reducedFlowResource.exists()) {
			logger.warn("Remove Existing Output Path: " + reducedFlowPath);
			reducedFlowResource.remove();
		}

		// Must save reducedFlow first to make dailyrolling dirs
		reducedDataOutput.saveAsHadoopFile(reducedFlowPath, NullWritable.class, BytesWritable.class,
				ReducedFlowOutputFormat.class);

		logger.info("Calculate All File Finish.");

		logger.info(new LogMessageBuilder("Start Write Delta File.").addParameter("path", dictDelta.getPath())
				.addParameter("value", dictDeltaAccu.value()).toString());

		dictDelta.set(dictDeltaAccu.value());
		dictDelta.save();
		logger.info("End Write Delta File.");
		logger.info("build OneDDList Success.And Write OneDDList Into Filesystem.");

		oneDDistResource.set(dist);
		oneDDistResource.save();

		return dictDeltaAccu.value();
	}

	private void buildAdaptRuleMap(List<AdaptRule> adaptRules) {
		// 基本不会为空
		if (adaptRules != null) {
			Builder<DimType, AdaptRule> mapBuilder = ImmutableListMultimap.builder();
			for (AdaptRule adaptRule : adaptRules) {
				mapBuilder.put(adaptRule.getDimType(), adaptRule);
			}
			this.adaptRuleMap = mapBuilder.build();
		}
	}

}
