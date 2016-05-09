package com.ximalaya.damus.example.ad.protocol;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DeviceInfo implements Serializable {

	private static final long serialVersionUID = -2639065051183926683L;
	private String carrierOperator;
	private String networkMode;
	private String os;
	private Resolution resolution;
	private String deviceId;

	private String deviceName;

	public String getCarrierOperator() {
		return carrierOperator;
	}

	public void setCarrierOperator(String carrierOperator) {
		this.carrierOperator = carrierOperator;
	}

	public String getNetworkMode() {
		return networkMode;
	}

	public void setNetworkMode(String networkMode) {
		this.networkMode = networkMode;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
