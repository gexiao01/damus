// 文件名称: EqualRuleAdaptor.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt;

import com.ximalaya.damus.offline.adapt.model.AdaptRule;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public class EqualRuleAdaptor implements RuleAdaptor{

    @Override
    public String adapt(AdaptRule adaptRule, String str) {
        if(str != null && str.equals(adaptRule.getMatch())) {
            return adaptRule.getResult();
        }
        return str;
    }

}
