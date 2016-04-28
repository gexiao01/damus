package com.ximalaya.damus.actuary.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class MockActuaryService implements ActuaryService {

    @Override
    public Map<Long, Long> calculate(Collection<CalcRequest> requests) {

        Map<Long, Long> result = new HashMap<Long, Long>();
        for (CalcRequest request : requests) {
            result.put(request.getId(), 100L);
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private long delay;

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
