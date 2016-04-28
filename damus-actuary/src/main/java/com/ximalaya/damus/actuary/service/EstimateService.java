package com.ximalaya.damus.actuary.service;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * main service damus-realtime business
 * 
 * @author xiao.ge
 * @date 20151203
 */
public interface EstimateService {

    long handleRequest(CalcRequest request) throws DamusException;
}
