package com.ximalaya.damus.example.ad.func;

import java.util.EnumMap;

import com.ximalaya.damus.common.util.DateUtils;
import com.ximalaya.damus.example.ad.protocol.Flow;
import com.ximalaya.damus.offline.spark.AbstractDimTypeMapper;
import com.ximalaya.damus.protocol.config.DimType;

public class AdDimTypeMapper extends AbstractDimTypeMapper<Flow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8789595119702802365L;

	@Override
	protected EnumMap<DimType, String> doMap(Flow flow) {
		// flow已经在FlowFilter里过滤一遍了.在这就不做校验了
		EnumMap<DimType, String> typeObjectMap = new EnumMap<DimType, String>(DimType.class);
		typeObjectMap.put(DimType.POSITION, flow.getProps().getPositionName());
		typeObjectMap.put(DimType.PROVINCE, "" + flow.getProps().getProvinceId());
		typeObjectMap.put(DimType.CITY, "" + flow.getProps().getCityId());
		typeObjectMap.put(DimType.VERSION, flow.getAppInfo().getVersion());
		typeObjectMap.put(DimType.RESOLUTION, flow.getDeviceInfo().getResolution().toString());
		typeObjectMap.put(DimType.PACKAGE, flow.getAppInfo().getPackageName());
		typeObjectMap.put(DimType.CARRIER, "" + flow.getProps().getOperator());
		typeObjectMap.put(DimType.OS, flow.getProps().getDevice());
		typeObjectMap.put(DimType.NETWORK, flow.getProps().getNetwork());
		// 时间在这算一遍吧
		typeObjectMap.put(DimType.HOUR, "" + DateUtils.getLongTimeHour(flow.getTs()));
		typeObjectMap.put(DimType.APP, "" + flow.getProps().getAppid());
		return typeObjectMap;
	}
}
