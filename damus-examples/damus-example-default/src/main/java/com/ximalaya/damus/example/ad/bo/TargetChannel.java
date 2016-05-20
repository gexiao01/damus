// 文件名称: TargetChannel.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年8月14日
 */
public class TargetChannel implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1565788155170975761L;

	public enum Strategy {
		NO, RULED_IN, // 属于
		RULED_OUT // 排除
	}

	private Strategy strategy;
	private List<String> targertChannels;

	public TargetChannel() {
		super();
	}

	public TargetChannel(Strategy strategy, List<String> targertChannels) {
		super();
		this.strategy = strategy;
		this.targertChannels = targertChannels;
	}

	public List<String> getTargertChannels() {
		return targertChannels;
	}

	public void setTargertChannels(List<String> targertChannels) {
		this.targertChannels = targertChannels;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
