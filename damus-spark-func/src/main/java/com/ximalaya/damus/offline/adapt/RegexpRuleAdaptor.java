// 文件名称: RegexpRuleAdaptor.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt;


import com.ximalaya.damus.offline.adapt.model.AdaptRule;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public class RegexpRuleAdaptor implements RuleAdaptor{

    @Override
    public String adapt(AdaptRule adaptRule, String str) {
        if(str != null && str.matches(adaptRule.getMatch())) {
            return adaptRule.getResult();
        }
        return str;
    }
}