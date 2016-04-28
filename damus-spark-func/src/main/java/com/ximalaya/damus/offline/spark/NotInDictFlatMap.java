// 文件名称: NotInDictFlatMap.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.Accumulator;
import org.apache.spark.api.java.function.FlatMapFunction;

import com.clearspring.analytics.util.Lists;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.exception.MetaException;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.meta.DimMeta;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public class NotInDictFlatMap implements
        FlatMapFunction<Iterator<EnumMap<DimType, String>>, EnumMap<DimType, String>> {
    private static final long serialVersionUID = -2775342556634826303L;
    private EnumMap<DimType, Set<String>> unknownConfig;
    private Accumulator<DimDict> gloabParam;
    private DimDict dimDict;

    public NotInDictFlatMap(DimDict dimDict, Accumulator<DimDict> gloabParam,
            EnumMap<DimType, Set<String>> unknownConfig) {
        this.dimDict = dimDict;
        this.gloabParam = gloabParam;
        this.unknownConfig = unknownConfig;
    }

    @Override
    public Iterable<EnumMap<DimType, String>> call(Iterator<EnumMap<DimType, String>> itor) {
        List<EnumMap<DimType, String>> list = Lists.newArrayList();
        DimDict newDimDict = new DimDict();
        for (; itor.hasNext();) {
            EnumMap<DimType, String> sourceData = itor.next();
            boolean inDict = true;
            for (DimType dimType : DimType.values()) {
                String dimTypeStr = sourceData.get(dimType);
                if (notNeedInNewDimDict(dimType, dimTypeStr)) {
                    continue;
                }
                // source字典数据
                DimMeta meta = dimDict.getDimMeta(dimType);
                // 字典某一type为null,视为当前type字典无数据,或当meta获取ID未知的情况
                if (meta == null || Constant.UNKNOWN_ID == meta.getId(dimTypeStr)) {
                    inDict = false;
                    long soureMaxId = getSourceMaxId(meta, dimType);
                    try {
                        putInNewDimDict(newDimDict, dimType, dimTypeStr, soureMaxId);
                    } catch (MetaException e) {
                        // 基本不会抛异常
                        continue;
                    }
                }
            }
            list.add(sourceData);
            if (!inDict) {
                synchronized (gloabParam) {
                    gloabParam.merge(newDimDict);
                }
            }
        }
        return list;
    }

    /**
     * @param dimType
     * @param dimTypeStr
     * @return
     */
    private boolean notNeedInNewDimDict(DimType dimType, String dimTypeStr) {
        // 该类型不需要维护字典
        if (!dimType.isLiteral()) {
            return true;
        }
        if (StringUtils.isBlank(dimTypeStr)) {
            return true;
        }
        Set<String> unknownStrs = unknownConfig.get(dimType);
        // 忽略不知道属性
        if (unknownStrs != null && unknownStrs.contains(StringUtils.trim(dimTypeStr))) {
            return true;
        }
        return false;
    }

    private long getSourceMaxId(DimMeta meta, DimType dimType) {
        long sourceMaxId = (meta == null) ? Constant.UNKNOWN_ID : meta.getMaxId();
        return sourceMaxId;
    }

    private void putInNewDimDict(DimDict newDimDict, DimType dimType, String dimTypeStr,
            long soureMaxId) throws MetaException {
        DimMeta newMeta = newDimDict.getDimMeta(dimType);
        // newMeta不可能为null
        if (newMeta.getId(dimTypeStr) == Constant.UNKNOWN_ID) {
            // 保证原字典最大id + 1开始递增
            if (newMeta.getMaxId() == Constant.UNKNOWN_ID) {
                newMeta.put(soureMaxId + 1, dimTypeStr);
            } else {
                newMeta.putNew(dimTypeStr);
            }
        }
    }
}
