// 文件名称: RuleAdaptor.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt;

import com.ximalaya.damus.offline.adapt.model.AdaptRule;

/**
 * 适配器接口
 * 
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月16日
 */
public interface RuleAdaptor {
    /**
     * @param adaptRule
     *            xml配的规则
     * @param str
     *            待适配的str
     * @return 适配结果:(命中得返回适配后结果/未命中原str返回)
     */
    String adapt(AdaptRule adaptRule, String str);
}
