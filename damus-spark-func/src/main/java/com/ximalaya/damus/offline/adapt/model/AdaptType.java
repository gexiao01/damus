// 文件名称: AdaptType.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt.model;

import com.ximalaya.damus.offline.adapt.CaseRuleAdaptor;
import com.ximalaya.damus.offline.adapt.EqualRuleAdaptor;
import com.ximalaya.damus.offline.adapt.IsNullRuleAdaptor;
import com.ximalaya.damus.offline.adapt.LikeRuleAdaptor;
import com.ximalaya.damus.offline.adapt.RegexpRuleAdaptor;
import com.ximalaya.damus.offline.adapt.RuleAdaptor;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public enum AdaptType {
    LIKE(LikeRuleAdaptor.class),
    REGEXP(RegexpRuleAdaptor.class),
    IN,
    NOTIN,
    EQUAL(EqualRuleAdaptor.class),
    ISNULL(IsNullRuleAdaptor.class),
    CASE(CaseRuleAdaptor.class);

    private Class<? extends RuleAdaptor> ruleAdaptorClass;

    private AdaptType() {
    }

    private AdaptType(Class<? extends RuleAdaptor> ruleAdaptorClass) {
        this.ruleAdaptorClass = ruleAdaptorClass;
    }

    public Class<? extends RuleAdaptor> getRuleAdaptorClass() {
        return ruleAdaptorClass;
    }

}
