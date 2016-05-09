// 文件名称: FlowToDimTypeMapConverter.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.util.EnumMap;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.protocol.config.DimType;

/**
 * Flow转成EnumMap<DimType, Object>转换器
 * 
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月11日
 */
public abstract class AbstractDimTypeMapper<F> implements Function<F, EnumMap<DimType, String>> {
	private static final long serialVersionUID = -3963351706496019098L;

	/**
	 * 将flow全部转换为EnumMap<DimType, String>,用String而非Object是方便字典文件判断
	 */
	@Override
	public EnumMap<DimType, String> call(F flow) {
		// flow已经在FlowFilter里过滤一遍了.在这就不做校验了
		try {
			return doMap(flow);
		} catch (Exception e) {
			return new EnumMap<DimType, String>(DimType.class);
		}

	}

	protected abstract EnumMap<DimType, String> doMap(F flow);
}
