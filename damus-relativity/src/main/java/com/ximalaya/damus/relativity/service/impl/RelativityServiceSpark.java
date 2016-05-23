package com.ximalaya.damus.relativity.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import scala.Tuple2;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ximalaya.damus.common.spark.SumReducer;
import com.ximalaya.damus.common.util.Pair;
import com.ximalaya.damus.offline.spark.AbstractDimTypeMapper;
import com.ximalaya.damus.offline.spark.LineToFlowConverter;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.relativity.service.RelativityService;
import com.ximalaya.damus.relativity.spark.EnumTypes2RelativityDistMapper;

public class RelativityServiceSpark<F> implements RelativityService, InitializingBean, DisposableBean {

	@Value("#{sparkProps}")
	private Properties props;

	private JavaSparkContext sc;

	@Value("${damus.relativity.rawdata.path}")
	private String rawDataPath;

	@Value("${damus.relativity.sample.raw}")
	private double rawSampleRatio;
	@Value("${damus.relativity.sample.effect}")
	private double effectSampleRatio;
	@Value("${damus.relativity.sample.seed}")
	private long sampleSeed;

	@Value("${damus.relativity.repartition}")
	private int repartitionNum;
	@Value("${damus.relativity.spark.minpartition}")
	private int minPartition;

	private Logger logger = Logger.getLogger(getClass());

	private Class<F> fClass;
	private AbstractDimTypeMapper<F> dimTypeMapper;

	@Override
	public void afterPropertiesSet() throws Exception {

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
	public double calcRelativity(final DimType dim1, final DimType dim2) {

		logger.info("Loading Raw Data: " + rawDataPath);
		JavaRDD<String> lines = sc.textFile(rawDataPath, minPartition);

		// deserialize
		JavaRDD<F> flows = lines.sample(false, rawSampleRatio, sampleSeed).map(new LineToFlowConverter<F>(fClass));

		if (repartitionNum > 0) {
			flows = flows.repartition(Math.max(repartitionNum, minPartition));
		}

		// Extract types
		JavaRDD<EnumMap<DimType, String>> dimTypeValues = flows.map(dimTypeMapper);

		// logger.info("TestOut:" + dimTypeValues.count());

		// Calculation
		Collection<Tuple2<Pair<String, String>, Long>> dist = dimTypeValues
				.mapToPair(new EnumTypes2RelativityDistMapper(dim1, dim2)).reduceByKey(new SumReducer()).collect();

		return corrlation(dist);
	}

	private double corrlation(Collection<Tuple2<Pair<String, String>, Long>> tuples) {
		BigDecimal sum = new BigDecimal(0);
		Map<String, Long> dim1Dist = new HashMap<String, Long>();
		Map<String, Long> dim2Dist = new HashMap<String, Long>();
		Table<String, String, Long> twodDist = HashBasedTable.<String, String, Long> create();

		for (Tuple2<Pair<String, String>, Long> tuple : tuples) {
			String d1 = tuple._1.getA();
			String d2 = tuple._1.getB();
			long value = tuple._2;

			Long exist = dim1Dist.containsKey(d1) ? dim1Dist.get(d1) : new Long(0);
			dim1Dist.put(tuple._1.getA(), exist + value);

			exist = dim1Dist.containsKey(d2) ? dim2Dist.get(d2) : new Long(0);
			dim2Dist.put(tuple._1.getB(), exist + value);

			exist = twodDist.contains(d1, d2) ? twodDist.get(d1, d2) : new Long(0);
			twodDist.put(d1, d2, exist + value);

			sum = sum.add(new BigDecimal(value));
		}

		BigDecimal r = new BigDecimal(0);

		// 这里不能直接遍历dist，而要遍历d1*d2，因为0流量的点也要累加
		for (String d1 : dim1Dist.keySet()) {
			for (String d2 : dim2Dist.keySet()) {
				Long flowLong = twodDist.contains(d1, d2) ? twodDist.get(d1, d2) : new Long(0);
				BigDecimal flow = new BigDecimal(flowLong);
				BigDecimal subFLow1 = new BigDecimal(dim1Dist.get(d1));
				BigDecimal subFLow2 = new BigDecimal(dim2Dist.get(d2));
				BigDecimal deviation = subFLow1.multiply(subFLow2).subtract(flow.multiply(sum))
						.divide(sum.pow(2), 100, BigDecimal.ROUND_HALF_DOWN);
				r = r.add(deviation.abs());
			}
		}
		return r.doubleValue();
	}

	public void setfClass(Class<F> fClass) {
		this.fClass = fClass;
	}

	public void setDimTypeMapper(AbstractDimTypeMapper<F> dimTypeMapper) {
		this.dimTypeMapper = dimTypeMapper;
	}
}
