package com.ximalaya.damus.protocol.raw;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AppInfo implements Serializable{
    private static final long serialVersionUID = 2555182374796819050L;
    private String packageName;
    private String version;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
