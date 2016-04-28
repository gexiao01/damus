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
			// 目前只针对，failReason与logType的null进行校验.
			// boolean nullValid = filterNull(flow, flow.getProps(),
			// flow.getProps().getFailReason(), flow.getProps()
			// .getLogType(), flow.getAppInfo(), flow.getDeviceInfo());
			// boolean finalValid = nullValid &&
			// logTypeValid(flow.getProps().getLogType())
			// && failReasonValid(flow.getProps().getFailReason()) &&
			// tsValid(flow.getTs());

			boolean finalValid = doFilter(flow);
			if (!finalValid) {
				addFilterFlowLog(flow);
			}
			return finalValid;
		} catch (Exception e) {
			addFilterFlowLog(flow);
			return false;
		}
	}

	protected abstract boolean doFilter(F flow);

	/**
	 * 先暂时不打日志，数据量过大，也没人去看 TODO 可以考虑就记录个总数
	 * 
	 * @param flow
	 */
	protected void addFilterFlowLog(F flow) {
		logger.debug(new LogMessageBuilder("flow data invalid.").addParameter("flow", flow).toString());
	}

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
