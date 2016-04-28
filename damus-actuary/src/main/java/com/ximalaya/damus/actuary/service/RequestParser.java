package com.ximalaya.damus.actuary.service;

import com.ximalaya.damus.protocol.request.CalcRequest;

public interface RequestParser {

	CalcRequest parseCalcRequest(String jsonStr);
}
