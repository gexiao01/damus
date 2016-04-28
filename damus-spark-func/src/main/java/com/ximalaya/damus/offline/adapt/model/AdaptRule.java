// 文件名称: AdaptRule.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ximalaya.damus.protocol.config.DimType;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public class AdaptRule implements Serializable{
    private static final long serialVersionUID = -6136009904561286999L;
    private AdaptType adaptType;
    private String match;
    private String result;
    private DimType dimType;

    @XmlElement(name = "adaptType")
    public AdaptType getAdaptType() {
        return adaptType;
    }

    public void setAdaptType(AdaptType adaptType) {
        this.adaptType = adaptType;
    }

    @XmlElement(name = "match")
    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    @XmlElement(name = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @XmlElement(name = "dimType")
    public DimType getDimType() {
        return dimType;
    }

    public void setDimType(DimType dimType) {
        this.dimType = dimType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
