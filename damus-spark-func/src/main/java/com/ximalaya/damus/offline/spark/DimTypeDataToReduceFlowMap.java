// 文件名称: DimTypeDataToReduceFlowMap.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.util.EnumMap;
import java.util.Map.Entry;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
public class DimTypeDataToReduceFlowMap implements Function<EnumMap<DimType, String>, ReducedFlow>{

    private static final long serialVersionUID = 1460338129862545599L;
    
    private DimDict dimDict;
    
    public DimTypeDataToReduceFlowMap(DimDict dimDict) {
        super();
        this.dimDict = dimDict;
    }

    @Override
    public ReducedFlow call(EnumMap<DimType, String> enumMap) throws Exception {
        ReducedFlow reducedFlow = new ReducedFlow();
        for (Entry<DimType, String> entry : enumMap.entrySet()) {
            reducedFlow.put(entry.getKey(), entry.getValue(), dimDict);
        }
        return reducedFlow;
    }

}
