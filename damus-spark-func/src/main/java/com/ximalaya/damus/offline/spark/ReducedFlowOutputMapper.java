package com.ximalaya.damus.offline.spark;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class ReducedFlowOutputMapper implements PairFunction<byte[], NullWritable, BytesWritable> {

    /**
     * 
     */
    private static final long serialVersionUID = 1828927137194330875L;

    @Override
    public Tuple2<NullWritable, BytesWritable> call(byte[] bytes) throws Exception {
        return new Tuple2<NullWritable, BytesWritable>(NullWritable.get(), new BytesWritable(bytes));
    }
}
