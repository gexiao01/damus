// 文件名称: RuleAdaptProcessor.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt.process;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.ximalaya.damus.common.util.LogMessageBuilder;
import com.ximalaya.damus.offline.adapt.RuleAdaptor;
import com.ximalaya.damus.offline.adapt.model.AdaptRule;
import com.ximalaya.damus.offline.adapt.model.AdaptType;
import com.ximalaya.damus.protocol.config.DimType;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
public class RuleAdaptProcessor implements RuleAdaptProcess, Serializable {
    private static final transient Logger logger = LoggerFactory
            .getLogger(RuleAdaptProcessor.class);
    private static final long serialVersionUID = -5980894316762555445L;

    private static final Map<AdaptType, RuleAdaptor> ruleAdaptors = Maps.newHashMap();

    static {
        for (AdaptType type : AdaptType.values()) {
            if (type.getRuleAdaptorClass() != null) {
                try {
                    ruleAdaptors.put(type, type.getRuleAdaptorClass().newInstance());
                } catch (Exception e) {
                    // 不会抛出异常..
                }
            }
        }
    }

    private ListMultimap<DimType, AdaptRule> adaptRuleMap = ImmutableListMultimap.of();

    /**
     * @param adaptRuleMap
     */
    public RuleAdaptProcessor(ListMultimap<DimType, AdaptRule> adaptRuleMap) {
        super();
        this.adaptRuleMap = adaptRuleMap;
    }

    @Override
    public String process(DimType dimType, String value) {
        // 规则中某一个dimType没有
        if (!adaptRuleMap.containsKey(dimType)) {
            return value;
        }
        List<AdaptRule> adaptRules = adaptRuleMap.get(dimType);
        for (AdaptRule adaptRule : adaptRules) {
            RuleAdaptor ruleAdaptor = selectAdaptor(adaptRule);
            if (ruleAdaptor != null) {
                String adaptResult = ruleAdaptor.adapt(adaptRule, value);
                // 未适配到结果,会原value返回,可以用==判断
                if (adaptResult != value) {
                    logger.debug("{}", new LogMessageBuilder("adapt success.")
                            .addParameter("match", value)
                            .addParameter("return", adaptResult)
                            .addParameter("rule", adaptRule));
                    return adaptResult;
                }
            }
        }
        return value;
    }

    private RuleAdaptor selectAdaptor(AdaptRule adaptRule) {
        AdaptType adaptType = adaptRule.getAdaptType();
        if (adaptType != null && ruleAdaptors.containsKey(adaptType)) {
            return ruleAdaptors.get(adaptType);
        }
        return null;
    }
}
