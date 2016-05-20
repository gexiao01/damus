// 文件名称: Platform.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年4月1日
 */
public enum Platform {
	ANDROID(Arrays.asList("android", "androidpad")), IOS(Arrays.asList("iphone")), WINPHONE(Arrays.asList("winphone")), IPAD(
			Arrays.asList("ipad")), WEB(Arrays.asList("web")), OTHER(Collections.<String> emptyList());

	private List<String> names;

	private Platform(List<String> names) {
		this.names = names;
	}

	public List<String> getNames() {
		return names;
	}

	public static Platform fromName(String name) {
		if (name == null) {
			return OTHER;
		}
		String lowName = name.toLowerCase();
		for (Platform platform : values()) {
			for (String namePlatform : platform.getNames()) {
				if (lowName.indexOf(namePlatform) >= 0) {
					return platform;
				}
			}
		}
		return OTHER;
	}
}
