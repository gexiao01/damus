package com.ximalaya.damus.offline.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.beans.factory.annotation.Value;

import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.resource.FileResource;
import com.ximalaya.damus.protocol.resource.Resource;

/**
 * a tool class for transforming ReducedFlow from bytes to literal
 */
public class ReduceFlowToLiteral {

	@SuppressWarnings({ "resource", "serial" })
	public static void readReducedFlow(String dictPath, String inPath, String outPath) throws Exception {

		Resource<DimDict> dimDictResource = new FileResource<DimDict>(dictPath, DimDict.class);
		final DimDict dict = dimDictResource.lazyGet();

		JavaSparkContext sc = new JavaSparkContext("local", "ReduceFlowToLiteral");
		JavaRDD<byte[]> flows = sc.binaryRecords(inPath, DimType.getRecordLength());

		// flows.cache();
		List<byte[]> bts = flows.take(10);
		for (byte[] bt : bts) {
			for (byte b : bt) {
				System.err.print(b + ",");
			}
			System.err.println();
		}
		JavaRDD<String> prettyFlows = flows.map(new Function<byte[], String>() {

			@Override
			public String call(byte[] bt) throws Exception {
				ReducedFlow reducedFlow = new ReducedFlow();
				InputStream is = new ByteArrayInputStream(bt);
				reducedFlow.deserialize(is);
				return reducedFlow.toPrettyFormat(dict);
			}
		});

		prettyFlows.repartition(1).saveAsTextFile(outPath);

		sc.stop();
		sc.close();
	}

	public static void main(String[] args) throws Exception {
		// awk -F "," '{arr[$7]+=1}END{for(i in arr)print i,arr[i]}'
		// pos-app.literal
		String rootPath = "file:/Users/xiaoge/Documents/damus/damus-offline/";
		String reducedFlowPath = "./data/output/reducedflow";
		String dictPath = "./data/damus.meta";
		ReduceFlowToLiteral.readReducedFlow(dictPath, rootPath + reducedFlowPath + "/part-*", rootPath
				+ reducedFlowPath + "/literal");
	}
}
