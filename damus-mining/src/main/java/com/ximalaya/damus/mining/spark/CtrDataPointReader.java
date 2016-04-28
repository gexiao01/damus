package com.ximalaya.damus.mining.spark;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.ximalaya.damus.mining.model.CtrDataPoint;

public class CtrDataPointReader implements
        PairFunction<String, Tuple2<Integer, Integer>, CtrDataPoint> {
    /**
     * 
     */
    private static final long serialVersionUID = -1059946141437348286L;

    private boolean isShow;

    public CtrDataPointReader(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    public Tuple2<Tuple2<Integer, Integer>, CtrDataPoint> call(String line) throws Exception {
        String[] items = line.split("\t");
        int adId = 0;
        if (StringUtils.isNumeric(items[1])) {
            adId = Integer.valueOf(items[1]);
        }
        CtrDataPoint pt = new CtrDataPoint(adId, items[0]);
        if (isShow) {
            pt.setShow(Long.valueOf(items[2]));
        } else {
            pt.setClick(Long.valueOf(items[2]));
        }
        return new Tuple2<Tuple2<Integer, Integer>, CtrDataPoint>(pt.getKey(), pt);
    }
}
