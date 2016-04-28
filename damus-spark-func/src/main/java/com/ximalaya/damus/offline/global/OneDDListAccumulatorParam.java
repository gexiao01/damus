// 文件名称: OneDDListAccumulatorParam.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.global;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.spark.AccumulatorParam;

import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.dist.OneDDist;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月24日
 */
public class OneDDListAccumulatorParam implements AccumulatorParam<OneDDist> {

    private static final long serialVersionUID = -4026931931901434091L;

    @Override
    public OneDDist addInPlace(OneDDist sourceDist, OneDDist margedDist) {
        for (DimType type : DimType.values()) {
            Map<Long, Long> sourceMap = sourceDist.getDimTypeMap(type);
            Map<Long, Long> margedMap = margedDist.getDimTypeMap(type);

            for (Entry<Long, Long> entry : margedMap.entrySet()) {
                Long margedId = entry.getKey();
                Long margedSum = entry.getValue();
                mergeIntoSourceMap(sourceMap, margedId, margedSum);
            }
        }
        return sourceDist;
    }

    /**
     * @param sourceMap
     * @param margedId
     * @param margedSum
     */
    private void mergeIntoSourceMap(Map<Long, Long> sourceMap, Long margedId, Long margedSum) {
        if (margedId != null && margedSum != null) {
            Long sourceSum = sourceMap.get(margedId);
            if (sourceSum == null) {
                sourceMap.put(margedId, margedSum);
            } else {
                sourceMap.put(margedId, sourceSum + margedSum);
            }
        }
    }

    @Override
    public OneDDist zero(OneDDist arg0) {
        return new OneDDist();
    }

    @Override
    public OneDDist addAccumulator(OneDDist sourceDist, OneDDist margedDist) {
        return addInPlace(sourceDist, margedDist);
    }
}
