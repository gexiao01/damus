// 文件名称: Rules.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
@XmlRootElement(name = "rules")
public class Rules {
    private List<AdaptRule> adaptRules;

    @XmlElement(name = "rule")
    public List<AdaptRule> getAdaptRules() {
        return adaptRules;
    }

    public void setAdaptRules(List<AdaptRule> adaptRules) {
        this.adaptRules = adaptRules;
    }
     
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
