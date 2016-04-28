// 文件名称: XmlTest.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt;

import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import com.ximalaya.damus.common.util.XmlUtil;
import com.ximalaya.damus.offline.adapt.model.AdaptRule;
import com.ximalaya.damus.offline.adapt.model.AdaptType;
import com.ximalaya.damus.offline.adapt.model.Rules;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
public class XmlTest {
	@Test
	public void testAdaptRuleParse() throws JAXBException {
		URL url = getClass().getResource("/adapt-rule-ut.xml");
		Rules rules = XmlUtil.xmlToBean(url, Rules.class);
		Assert.assertNotNull(rules);

		List<AdaptRule> adaptRules = rules.getAdaptRules();
		Assert.assertNotEquals(0, adaptRules.size());
		AdaptRule rule = adaptRules.get(0);

		Assert.assertEquals(AdaptType.LIKE, rule.getAdaptType());
	}
}
