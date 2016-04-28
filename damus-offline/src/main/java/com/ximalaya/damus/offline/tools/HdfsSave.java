package com.ximalaya.damus.offline.tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.ximalaya.damus.offline.spark.io.ReducedFlowOutputFormat;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.resource.FileResource;
import com.ximalaya.damus.protocol.resource.Resource;

/**
 * not in testsuite
 */
public final class HdfsSave {

	@SuppressWarnings({ "serial", "resource" })
	public static void testSaveReducedFlow() throws Exception {

		Resource<DimDict> dimDictResource = new FileResource<DimDict>("./data/test/damus.meta", DimDict.class);
		DimDict dict = dimDictResource.lazyGet();

		List<byte[]> bts = new ArrayList<byte[]>();
		ReducedFlow flow = new ReducedFlow();
		flow.put(DimType.PROVINCE, 510000 + "", dict);
		flow.put(DimType.RESOLUTION, "2460*1440", dict);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		flow.serialize(bos);
		bts.add(bos.toByteArray());

		ReducedFlow flow2 = new ReducedFlow();
		flow2.put(DimType.PROVINCE, 510000 + "", dict);
		flow2.put(DimType.RESOLUTION, "800*480", dict);
		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		flow2.serialize(bos2);
		bts.add(bos2.toByteArray());

		JavaSparkContext sc = new JavaSparkContext("local[2]", "HdfsSaveTest");
		JavaRDD<byte[]> lines = sc.parallelize(bts).repartition(1);
		// lines.cache();

		// lines.saveAsObjectFile("file:/Users/xmly/git/damus/damus-offline/data/redflowObject");

		lines.mapToPair(new PairFunction<byte[], NullWritable, BytesWritable>() {

			@Override
			public Tuple2<NullWritable, BytesWritable> call(byte[] bytes) throws Exception {
				return new Tuple2<NullWritable, BytesWritable>(NullWritable.get(), new BytesWritable(bytes));
			}
		}).saveAsHadoopFile("file:/Users/xmly/git/damus/damus-offline/data/redflowFormat", NullWritable.class,
				BytesWritable.class, ReducedFlowOutputFormat.class);
		sc.stop();
		sc.close();
	}

	public static void main(String[] args) throws Exception {
		testSaveReducedFlow();
	}
}
