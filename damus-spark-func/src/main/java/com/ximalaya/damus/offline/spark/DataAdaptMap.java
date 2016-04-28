// 文件名称: DataAdaptMap.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.util.EnumMap;
import java.util.Map.Entry;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import com.google.common.collect.ListMultimap;
import com.ximalaya.damus.offline.adapt.model.AdaptRule;
import com.ximalaya.damus.offline.adapt.process.RuleAdaptProcess;
import com.ximalaya.damus.offline.adapt.process.RuleAdaptProcessor;
import com.ximalaya.damus.protocol.config.DimType;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
public class DataAdaptMap implements Function<EnumMap<DimType, String>, EnumMap<DimType, String>> {

    private static final long serialVersionUID = -6202878883723929683L;
    private RuleAdaptProcess process;

    public DataAdaptMap(Broadcast<ListMultimap<DimType, AdaptRule>> adaptData) {
        super();
        this.process = new RuleAdaptProcessor(adaptData.value());
    }

    @Override
    public EnumMap<DimType, String> call(EnumMap<DimType, String> data) throws Exception {
        for (Entry<DimType, String> entry : data.entrySet()) {
            String adaptResult = process.process(entry.getKey(), entry.getValue());
            data.put(entry.getKey(), adaptResult);
        }
        return data;
    }
}
