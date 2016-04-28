package com.ximalaya.damus.actuary.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Maps;
import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.actuary.spark.ReduceFlowFilter;
import com.ximalaya.damus.actuary.spark.ReduceFlowMapper;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class ActuaryServiceSpark implements ActuaryService, InitializingBean, DisposableBean {

	private JavaSparkContext sc;

	@Value("#{sparkProps}")
	private Properties props;

	@Value("${damus.actuary.reducedfile.path}")
	private String reducedFlowPath;

	@Value("${damus.offline.sample}")
	private double sampleRatio;

	private JavaRDD<ReducedFlow> reducedFlows;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void afterPropertiesSet() throws Exception {

		logger.info("loading SparkConf: " + props);
		SparkConf conf = new SparkConf();
		for (Entry<Object, Object> entry : props.entrySet()) {
			conf.set((String) entry.getKey(), (String) entry.getValue());
		}
		sc = new JavaSparkContext(conf);

		refreshIfNecessary();
	}

	private synchronized void refreshIfNecessary() {

		logger.info("Reload reducedFlowResource from " + reducedFlowPath);
		if (reducedFlows != null) {
			reducedFlows.unpersist(); // release old cache
		}

		// load new data and cache
		JavaRDD<byte[]> flows = sc.binaryRecords(reducedFlowPath, DimType.getRecordLength());
		// logger.info("refresh reducedFlows " + flows.count()); // just for
		// debug

		reducedFlows = flows.map(new ReduceFlowMapper());
		reducedFlows.persist(StorageLevel.MEMORY_AND_DISK());

		logger.info("Reload reducedFlowResource finish " + reducedFlowPath);
	}

	@Override
	public Map<Long, Long> calculate(Collection<CalcRequest> requests) {

		logger.info("Start Calculation: " + requests);

		Map<Long, Long> sizesMap = Maps.newHashMap();
		for (CalcRequest request : requests) {
			JavaRDD<ReducedFlow> hitFlows = reducedFlows.filter(new ReduceFlowFilter(request));
			long hits = hitFlows.count();

			long unsampledHits = (long) (hits / sampleRatio);
			sizesMap.put(request.getId(), unsampledHits);
		}

		logger.info("Finish Calculation: " + sizesMap);
		return sizesMap;
	}

	@Override
	public void destroy() throws Exception {
		sc.close();
	}

	// for test usage/////////////////////

	public void setReducedFlows(ReducedFlow... reducedFlows) {
		this.reducedFlows = sc.parallelize(Arrays.asList(reducedFlows));
	}
}
