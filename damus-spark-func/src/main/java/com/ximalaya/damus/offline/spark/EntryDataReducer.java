// 文件名称: EntryDataReducer.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;


import org.apache.spark.api.java.function.Function2;


/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月24日
 */
public class EntryDataReducer implements Function2<Long, Long, Long>{

    private static final long serialVersionUID = -4750641303730207218L;

    @Override
    public Long call(Long v1, Long v2) {
        return v1 + v2;
    }


}
