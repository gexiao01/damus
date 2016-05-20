// Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年5月12日
 */
public enum Operator {
	YIDONG(0, "中国移动"), LIANTONG(1, "中国联通"), DIANXIN(2, "中国电信"), OTHER(3, "<other>"); // TODO
																						// 特判

	private final int code;
	private final String logValue;

	private Operator(int code, String logValue) {
		this.code = code;
		this.logValue = logValue;
	}

	public int getCode() {
		return code;
	}

	public static Operator fromCode(int code) {
		for (Operator operator : values()) {
			if (code == operator.getCode()) {
				return operator;
			}
		}
		return OTHER;
	}

	public String getLogValue() {
		return logValue;
	}

	@Override
	public String toString() {
		return name() + "[" + logValue + "]";
	}
}
