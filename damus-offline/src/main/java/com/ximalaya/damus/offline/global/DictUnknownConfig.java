package com.ximalaya.damus.offline.global;

import java.util.EnumMap;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ximalaya.damus.protocol.config.DimType;

/**
 * 单例配置 xdcs里各DimType未知值.配置程序里可以改进,不过这块变化应该不大
 * 
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月15日
 */
public class DictUnknownConfig {
	private static EnumMap<DimType, Set<String>> unknownConfig = null;

	public static EnumMap<DimType, Set<String>> getUnknownConfig() {
		if (unknownConfig == null) {
			// synchronized (unknownConfig) {
			// if (unknownConfig == null) {
			unknownConfig = new EnumMap<DimType, Set<String>>(DimType.class);
			unknownConfig.put(DimType.POSITION, Sets.newHashSet("null", ""));
			unknownConfig.put(DimType.PROVINCE, Sets.newHashSet("null", "0", ""));
			unknownConfig.put(DimType.CITY, Sets.newHashSet("null", "0", ""));
			unknownConfig.put(DimType.VERSION, Sets.newHashSet("null"));
			unknownConfig.put(DimType.RESOLUTION, Sets.newHashSet("null", "0*0", ""));
			unknownConfig.put(DimType.PACKAGE, Sets.newHashSet("null", ""));
			unknownConfig.put(DimType.CARRIER, Sets.newHashSet("null", "未知", ""));
			unknownConfig.put(DimType.OS, Sets.newHashSet("null", ""));
			unknownConfig.put(DimType.NETWORK, Sets.newHashSet("null", ""));
			unknownConfig.put(DimType.HOUR, Sets.newHashSet(""));
			// }
			// }
		}
		return unknownConfig;
	}

	private DictUnknownConfig() {
	}

}
