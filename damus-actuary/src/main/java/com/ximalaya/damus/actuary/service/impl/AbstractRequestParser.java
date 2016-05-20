package com.ximalaya.damus.actuary.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ximalaya.damus.actuary.service.RequestParser;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.request.CalcRequest;
import com.ximalaya.damus.protocol.resource.Resource;

public abstract class AbstractRequestParser<T> implements RequestParser<T> {

	@Autowired
	@Qualifier("dimDictResource")
	protected Resource<DimDict> dict;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public CalcRequest parseCalcRequest(T bo) {
		logger.info("parseCalcRequest Start: " + bo);

		CalcRequest calcRequest = doParse(bo);
		// parseNetwork(calcRequest, targetContent.getTargetNetworks());

		logger.info("parseCalcRequest Finish: " + calcRequest);
		return calcRequest;
	}

	protected abstract CalcRequest doParse(T bo);

}
