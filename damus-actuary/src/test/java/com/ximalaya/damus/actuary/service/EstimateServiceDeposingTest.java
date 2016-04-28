package com.ximalaya.damus.actuary.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ximalaya.damus.actuary.BaseContextTest;
import com.ximalaya.damus.actuary.service.impl.EstimateServiceDeposing;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class EstimateServiceDeposingTest extends BaseContextTest {

	@Autowired
	private EstimateServiceDeposing service;

	@Test
	public void testDimDeposingCalc() throws Exception {

		CalcRequest request = new CalcRequest();
		request.add(DimType.NETWORK, 1L, 2L);
		request.add(DimType.OS, 2L);
		long ret = service.handleRequest(request);

		// 9*(4/9+2/9)*(3/9)=2
		Assert.assertEquals(2L, ret);
	}
}
