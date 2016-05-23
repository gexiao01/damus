package com.ximalaya.damus.relativity.protocol;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TestFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -103951224025136637L;

	private String network;
	private String os;
	private int type;

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
