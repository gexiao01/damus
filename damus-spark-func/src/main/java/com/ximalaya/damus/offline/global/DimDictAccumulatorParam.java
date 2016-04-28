// 文件名称: DimDictAccumulatorParam.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.global;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.AccumulatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ximalaya.damus.common.util.LogMessageBuilder;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.meta.DimMeta;

/**
 * spark全局维护的dimdict
 * 
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月15日
 */
public class DimDictAccumulatorParam implements AccumulatorParam<DimDict> {

	private static final transient Logger logger = LoggerFactory.getLogger(DimDictAccumulatorParam.class);
	private static final long serialVersionUID = 253104371670483946L;

	@SuppressWarnings("unchecked")
	@Override
	public DimDict addInPlace(DimDict dict1, DimDict dict2) {
		try {
			for (DimType dimType : DimType.values()) {
				DimMeta sourceMeta = dict1.getDimMeta(dimType);
				DimMeta mergeMeta = dict2.getDimMeta(dimType);
				long maxSourceId = sourceMeta.getMaxId();
				// 如果被merge最大id为0,直接跳过
				if (mergeMeta.getMaxId() == Constant.UNKNOWN_ID) {
					continue;
				}
				// 如果原meta最大id为0,直接merge
				if (maxSourceId == Constant.UNKNOWN_ID) {
					sourceMeta.merge(mergeMeta);
				} else {
					Collection<String> sourceVals = sourceMeta.getIdMap().values();
					Collection<String> mergeVals = mergeMeta.getIdMap().values();
					// 去重
					Collection<String> subtractVals = CollectionUtils.subtract(mergeVals, sourceVals);
					if (CollectionUtils.isNotEmpty(subtractVals)) {
						DimMeta distinctMeta = new DimMeta(dimType);
						int count = 0;
						for (String val : subtractVals) {
							count++;
							distinctMeta.put(maxSourceId + count, val);
						}
						sourceMeta.merge(distinctMeta);
					}
				}
			}
		} catch (Exception e) {
			logger.error(new LogMessageBuilder("merge error." + e.getMessage()).addParameter("source", dict1)
					.addParameter("merged", dict2).toString());
		}
		return dict1;
	}

	@Override
	public DimDict zero(DimDict arg0) {
		return new DimDict();
	}

	@Override
	public DimDict addAccumulator(DimDict dict1, DimDict dict2) {
		return addInPlace(dict1, dict2);
	}
}
