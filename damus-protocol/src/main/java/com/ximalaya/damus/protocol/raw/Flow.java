package com.ximalaya.damus.protocol.raw;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity of raw flow data
 * 
 * @author xiao.ge
 * @date 2015.11.30
 */
public class Flow implements Serializable{

    private static final long serialVersionUID = -2931629858477989332L;
    private AppInfo appInfo;
    private DeviceInfo deviceInfo;
    private Props props;
    private long ts;

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
