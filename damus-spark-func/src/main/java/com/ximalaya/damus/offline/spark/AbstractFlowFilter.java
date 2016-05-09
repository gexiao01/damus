// 文件名称: FlowFilter.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ximalaya.damus.common.util.LogMessageBuilder;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月10日
 */
public abstract class AbstractFlowFilter<F> implements Function<F, Boolean> {

	private transient static final Logger logger = LoggerFactory.getLogger(AbstractFlowFilter.class);
	private static final long serialVersionUID = -403621477475297007L;

	@Override
	public Boolean call(F flow) {
		try {
			boolean finalValid = doFilter(flow);
			if (!finalValid) {
				logger.debug(new LogMessageBuilder("flow data invalid.").addParameter("flow", flow).toString());
			}
			return finalValid;
		} catch (Exception e) {
			logger.debug(new LogMessageBuilder("flow data invalid.").addParameter("flow", flow).toString(), e);
			return false;
		}
	}

	protected abstract boolean doFilter(F flow);

	/**
	 * 判断是否为null验证
	 * 
	 * @param obj数组
	 *            ,注意数组顺序,不然会报异常
	 * @return
	 */
	protected boolean filterNull(Object... objs) {
		if (objs == null) {
			return false;
		}
		for (Object obj : objs) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}
}
