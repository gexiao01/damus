package com.ximalaya.damus.actuary.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ximalaya.damus.actuary.BaseContextTest;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * @author shroke.cao
 * @date 20151217
 */
public class RequestParserTest extends BaseContextTest {
	// private static final Logger logger =
	// LoggerFactory.getLogger(CalcRequestServiceTest.class);

	@Autowired
	private RequestParser<String> requestParser;

	@Test
	public void testRequestParse() {

		String jsonStr = "{\"networks\": [\"3g\"],\"oss\": [\"iPhone\",\"Android\"]}";
		CalcRequest calcRequest = requestParser.parseCalcRequest(jsonStr);

		logger.info("testRequestParse Ret: " + calcRequest);

		Assert.assertTrue(calcRequest.getTargets().get(DimType.NETWORK).contains(4L));

	}
}
