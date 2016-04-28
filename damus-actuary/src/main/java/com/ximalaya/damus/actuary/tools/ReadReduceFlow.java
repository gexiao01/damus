package com.ximalaya.damus.actuary.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;

public class ReadReduceFlow {

    @SuppressWarnings("serial")
    public static void main(String agrs[]) {

        String logFile = "./data/dist/reducedFlow";
        SparkConf conf = new SparkConf().setAppName("Simple Application");
        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<byte[]> flows = sc.binaryRecords(logFile, DimType.getRecordLength());

        JavaRDD<Long> flowsJavaRDD = flows.map(new Function<byte[], Long>() {
            @Override
            public Long call(byte[] agr) throws IOException, DamusException {
                ReducedFlow reducedFlow = new ReducedFlow();
                InputStream is = new ByteArrayInputStream(agr);
                reducedFlow.deserialize(is);
                System.out.println("reducedFlow:" + reducedFlow.getDims());
                return 1l;
            }
        });

        Long valueLong = flowsJavaRDD.reduce(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long a, Long b) {
                return a + b;
            }
        });

        sc.close();
        System.out.println("valueLong:" + valueLong);
    }

}