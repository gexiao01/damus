package com.ximalaya.damus.mining.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import scala.Tuple2;

public class CtrDataPoint extends DataPoint implements Serializable {

    private static final long serialVersionUID = 1020105177137020551L;

    private final int adId;
    private final String deviceId;
    private long show;
    private long click;

    public CtrDataPoint(int adId, String deviceId) {
        super(new Tuple2<Integer, Integer>(deviceId.hashCode(), adId));
        this.adId = adId;
        this.deviceId = deviceId;
    }

    @Override
    public int getProduct() {
        return adId;
    }

    @Override
    public int getUser() {
        return deviceId.hashCode();
    }

    @Override
    public double getRating() {
        if (click > show) {
            return 1D;
        } else if (show != 0) {
            return 1D * click / show;
        } else {
            return 0D;
        }
    }

    public CtrDataPoint merge(CtrDataPoint other) {
        if (other != null && this.getKey().equals(other.getKey())) {
            this.show += other.show;
            this.click += other.click;
        }
        return this;
    }

    public int getAdId() {
        return adId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getShow() {
        return show;
    }

    public void setShow(long show) {
        this.show = show;
    }

    public long getClick() {
        return click;
    }

    public void setClick(long click) {
        this.click = click;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
