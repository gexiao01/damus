package com.ximalaya.damus.actuary.service;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.ximalaya.damus.actuary.BaseContextTest;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class ActuaryServiceTest extends BaseContextTest {
	private static final Logger logger = LoggerFactory.getLogger(ActuaryServiceTest.class);

	@Autowired
	private ActuaryService actuaryService;

	@Test
	public void testCalcuate() {

		CalcRequest request = new CalcRequest();
		request.add(DimType.NETWORK, 1L, 2L);
		request.add(DimType.OS, 2L);

		Map<Long, Long> value = actuaryService.calculate(Lists.newArrayList(request));

		logger.info("testCalcuate Ret:" + value.get(request.getId()));

		Assert.assertEquals(2L, value.get(request.getId()).longValue());
	}
}
