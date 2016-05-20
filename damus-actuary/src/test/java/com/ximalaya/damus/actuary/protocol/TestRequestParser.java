package com.ximalaya.damus.actuary.protocol;

import java.util.Map;

import com.ximalaya.damus.actuary.service.impl.AbstractRequestParser;
import com.ximalaya.damus.common.util.JsonUtils;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class TestRequestParser extends AbstractRequestParser<String> {

	@Override
	protected CalcRequest doParse(String jsonStr) {

		TestCalcRequestBean bean = JsonUtils.fromJsonString(TestCalcRequestBean.class, jsonStr);

		CalcRequest request = new CalcRequest();
		Map<String, Long> valueMap = getValueMap(DimType.NETWORK);

		for (String netValue : bean.getNetworks()) {
			request.add(DimType.NETWORK, valueMap.get(netValue));
		}

		valueMap = getValueMap(DimType.OS);
		for (String osValue : bean.getOss()) {
			request.add(DimType.NETWORK, valueMap.get(osValue));
		}

		return request;
	}

	private Map<String, Long> getValueMap(DimType dimType) {
		return dict.get().getDimMeta(dimType).getValueMap();
	}

}
