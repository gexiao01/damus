// 文件名称: ReducedFlowToByteArrMap.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.io.ByteArrayOutputStream;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.protocol.reduced.ReducedFlow;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月24日
 */
public class ReducedFlowToByteArrMap implements Function<ReducedFlow, byte[]> {

    private static final long serialVersionUID = 1200763728397950729L;

    @Override
    public byte[] call(ReducedFlow reducedFlow) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reducedFlow.serialize(bos);
        return bos.toByteArray();
    }

}
