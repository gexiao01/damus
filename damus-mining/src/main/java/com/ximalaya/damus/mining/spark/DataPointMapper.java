package com.ximalaya.damus.mining.spark;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.ximalaya.damus.mining.model.DataPoint;

public class DataPointMapper<T extends DataPoint> implements
        PairFunction<Tuple2<Tuple2<Integer, Integer>, T>, Tuple2<Integer, Integer>, DataPoint> {

    /**
     * 
     */
    private static final long serialVersionUID = 4657632547875667706L;

    @Override
    public Tuple2<Tuple2<Integer, Integer>, DataPoint> call(Tuple2<Tuple2<Integer, Integer>, T> t)
            throws Exception {
        return new Tuple2<Tuple2<Integer, Integer>, DataPoint>(t._1(), t._2);
    }

}
