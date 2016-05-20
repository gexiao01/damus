// Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年4月4日
 */
public enum NetworkType {
	MOBILE2G(Arrays.asList("2g")), MOBILE3G(Arrays.asList("3g")), MOBILE4G(Arrays.asList("4g")), WIFI(Arrays
			.asList("wifi")), OTHER(Collections.<String> emptyList());

	private List<String> names;

	private NetworkType(List<String> names) {
		this.names = names;
	}

	public static NetworkType fromName(String name) {
		if (StringUtils.isEmpty(name)) {
			return OTHER;
		}
		String lowName = name.toLowerCase();
		for (NetworkType network : values()) {
			if (network.names.contains(lowName)) {
				return network;
			}
		}
		return OTHER;
	}

	public List<String> getNames() {
		return names;
	}
}
