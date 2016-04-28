package com.ximalaya.damus.actuary.service;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * entry for handling upstream requests
 * 
 * @author xiao.ge
 * @date 20151202
 */
public interface DispatchService {

    long handleRequest(CalcRequest request) throws DamusException;
}
