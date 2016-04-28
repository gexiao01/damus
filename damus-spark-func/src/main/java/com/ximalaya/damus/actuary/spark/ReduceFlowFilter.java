package com.ximalaya.damus.actuary.spark;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.request.CalcRequest;

public class ReduceFlowFilter implements Function<ReducedFlow, Boolean> {
    private CalcRequest request;
    private static final long serialVersionUID = -5797068789400354961L;

    public ReduceFlowFilter(CalcRequest request) {
        this.request = request;
    }

    @Override
    public Boolean call(ReducedFlow reducedFlow) {
        // ReducedFlow reducedFlow = new ReducedFlow();
        // InputStream is = new ByteArrayInputStream(agr);
        // reducedFlow.deserialize(is);
        return request.hit(reducedFlow.getDims());
    }
}