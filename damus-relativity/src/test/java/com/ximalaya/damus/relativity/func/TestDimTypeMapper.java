package com.ximalaya.damus.relativity.func;

import java.util.EnumMap;

import com.ximalaya.damus.offline.spark.AbstractDimTypeMapper;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.relativity.protocol.TestFlow;

public class TestDimTypeMapper extends AbstractDimTypeMapper<TestFlow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8789595119702802365L;

	@Override
	protected EnumMap<DimType, String> doMap(TestFlow flow) {
		EnumMap<DimType, String> typeObjectMap = new EnumMap<DimType, String>(DimType.class);
		typeObjectMap.put(DimType.NETWORK, (flow.getNetwork() + "").toLowerCase());
		typeObjectMap.put(DimType.OS, (flow.getOs() + "").toLowerCase());
		return typeObjectMap;
	}
}
