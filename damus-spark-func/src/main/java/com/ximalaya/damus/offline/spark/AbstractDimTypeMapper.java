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

		// typeObjectMap.put(DimType.POSITION,
		// flow.getProps().getPositionName());
		// typeObjectMap.put(DimType.PROVINCE, "" +
		// flow.getProps().getProvinceId());
		// typeObjectMap.put(DimType.CITY, "" + flow.getProps().getCityId());
		// typeObjectMap.put(DimType.VERSION, flow.getAppInfo().getVersion());
		// typeObjectMap.put(DimType.RESOLUTION,
		// flow.getDeviceInfo().getResolution().toString());
		// typeObjectMap.put(DimType.PACKAGE,
		// flow.getAppInfo().getPackageName());
		// typeObjectMap.put(DimType.CARRIER, "" +
		// flow.getProps().getOperator());
		// typeObjectMap.put(DimType.OS, flow.getProps().getDevice());
		// typeObjectMap.put(DimType.NETWORK, flow.getProps().getNetwork());
		// // 时间在这算一遍吧
		// typeObjectMap.put(DimType.HOUR, "" +
		// DateUtils.getLongTimeHour(flow.getTs()));
		// typeObjectMap.put(DimType.APP, "" + flow.getProps().getAppid());
	}

	protected abstract EnumMap<DimType, String> doMap(F flow);
}
