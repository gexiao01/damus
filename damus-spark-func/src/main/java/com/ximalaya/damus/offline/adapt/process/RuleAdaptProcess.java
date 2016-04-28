// 文件名称: RuleAdaptProcess.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.adapt.process;

import com.ximalaya.damus.protocol.config.DimType;

/**
 * 一条记录适配过程器
 * 
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月23日
 */
public interface RuleAdaptProcess {
    /**
     * 
     * @param dimType
     *            字典类型
     * @param value
     *            适配原始string
     * @return
     */
    String process(DimType dimType, String value);
}
