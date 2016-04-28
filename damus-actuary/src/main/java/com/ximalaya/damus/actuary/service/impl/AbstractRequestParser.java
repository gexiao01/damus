package com.ximalaya.damus.actuary.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ximalaya.damus.actuary.service.RequestParser;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.request.CalcRequest;
import com.ximalaya.damus.protocol.resource.Resource;

public abstract class AbstractRequestParser implements RequestParser {

	@Autowired
	@Qualifier("dimDictResource")
	protected Resource<DimDict> dict;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public CalcRequest parseCalcRequest(String jsonStr) {
		logger.info("parseCalcRequest Start: " + jsonStr);

		CalcRequest calcRequest = doParse(jsonStr);
		// parseNetwork(calcRequest, targetContent.getTargetNetworks());

		logger.info("parseCalcRequest Finish: " + calcRequest);
		return calcRequest;
	}

	// private void parseNetwork(CalcRequest calcRequest, List<NetworkType>
	// targetNetworks) {
	// Map<String, Long> valueMap = getValueMap(DimType.NETWORK);
	//
	// for (NetworkType network : targetNetworks) {
	// for (String name : network.getNames()) {
	// if (valueMap.containsKey(name)) {
	// calcRequest.add(DimType.NETWORK, valueMap.get(name));
	// }
	// }
	// }
	//
	// calcRequest.add(DimType.NETWORK, Constant.NOT_FOUND_ID);
	// if (targetNetworks.contains(NetworkType.OTHER)) {
	// // explicitly target unknown
	// calcRequest.add(DimType.NETWORK, Constant.UNKNOWN_ID);
	// }
	//
	// }

	protected abstract CalcRequest doParse(String jsonStr);

}
