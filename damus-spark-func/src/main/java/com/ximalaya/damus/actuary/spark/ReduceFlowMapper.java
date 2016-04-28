package com.ximalaya.damus.actuary.spark;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;

public class ReduceFlowMapper implements Function<byte[], ReducedFlow> {
    /**
     * 
     */
    private static final long serialVersionUID = -5524804667641324844L;

    @Override
    public ReducedFlow call(byte[] agr) throws DamusException, IOException {
        ReducedFlow reducedFlow = new ReducedFlow();
        InputStream is = new ByteArrayInputStream(agr);
        reducedFlow.deserialize(is);
        return reducedFlow;
    }
}
