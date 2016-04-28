package com.ximalaya.damus.actuary.service;

import java.util.Collection;
import java.util.Map;

import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * core algrithem for actuary
 * 
 * @author xiao.ge
 * @date 20151202
 */
public interface ActuaryService {

    /**
     * calculate flow hits of given requests, using ReducedFlows datas
     * 
     * @param requests
     * @return key-CalcRequest.id, value-hits
     */
    Map<Long, Long> calculate(Collection<CalcRequest> requests);
}
