package com.ximalaya.damus.actuary.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.actuary.service.DispatchService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * a simple implement of DispatchService. for test cases. no batches, no asyncs
 * 
 * @author xiao.ge
 * @date 20151202
 */
public class SimpleDispatchService implements DispatchService {

    @Autowired
    private ActuaryService actuaryService;

    @Override
    public long handleRequest(CalcRequest request) throws DamusException {
        Map<Long, Long> batchResults = actuaryService.calculate(Arrays.asList(request));
        return batchResults.get(request.getId());
    }

}
