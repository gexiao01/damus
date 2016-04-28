package com.ximalaya.damus.protocol.raw;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Props implements Serializable {

    private static final long serialVersionUID = 1020105177137020551L;
    private Integer provinceId;
    private Integer cityId;
    private String positionName;
    private String trackid;
    private String logType;
    private Integer failReason;

    private String anrdroidId;
    private Integer appid;
    private String device;
    private String network;
    private String version;
    private Integer operator;
    private String type;

    private Integer adItemId;

    private Integer xCity;
    private Integer xCityCode;
    private Integer xProvince;
    private Integer xProvinceCode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getTrackid() {
        return trackid;
    }

    public void setTrackid(String trackid) {
        this.trackid = trackid;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Integer getFailReason() {
        return failReason;
    }

    public void setFailReason(Integer failReason) {
        this.failReason = failReason;
    }

    public String getAnrdroidId() {
        return anrdroidId;
    }

    public void setAnrdroidId(String anrdroidId) {
        this.anrdroidId = anrdroidId;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAdItemId() {
        return adItemId;
    }

    public void setAdItemId(Integer adItemId) {
        this.adItemId = adItemId;
    }

    @JsonProperty("x-city")
    public Integer getxCity() {
        return xCity;
    }

    public void setxCity(Integer xCity) {
        this.xCity = xCity;
    }

    @JsonProperty("x-cityCode")
    public Integer getxCityCode() {
        return xCityCode;
    }

    public void setxCityCode(Integer xCityCode) {
        this.xCityCode = xCityCode;
    }

    @JsonProperty("x-province")
    public Integer getxProvince() {
        return xProvince;
    }

    public void setxProvince(Integer xProvince) {
        this.xProvince = xProvince;
    }

    @JsonProperty("x-provinceCode")
    public Integer getxProvinceCode() {
        return xProvinceCode;
    }

    public void setxProvinceCode(Integer xProvinceCode) {
        this.xProvinceCode = xProvinceCode;
    }

}
