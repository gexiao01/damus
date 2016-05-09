package com.ximalaya.damus.example.ad.func;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Sets;
import com.ximalaya.damus.example.ad.protocol.Flow;
import com.ximalaya.damus.offline.spark.AbstractFlowFilter;

public class AdFlowFilter extends AbstractFlowFilter<Flow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8478695059762493123L;

	// 保留的logType类型set集合
	private Set<String> logTypeSet;
	// 过滤的failReason类型set集合
	private Set<Integer> failReasonSet;

	public AdFlowFilter() {
	}

	public AdFlowFilter(Set<String> logTypeSet, Set<Integer> failReasonSet) {
		super();
		this.logTypeSet = logTypeSet;
		this.failReasonSet = failReasonSet;
	}

	@Override
	protected boolean doFilter(Flow flow) {
		// 目前只针对，failReason与logType的null进行校验.
		boolean nullValid = filterNull(flow, flow.getProps(), flow.getProps().getFailReason(), flow.getProps()
				.getLogType(), flow.getAppInfo(), flow.getDeviceInfo());
		boolean finalValid = nullValid && logTypeValid(flow.getProps().getLogType())
				&& failReasonValid(flow.getProps().getFailReason()) && tsValid(flow.getTs());
		return finalValid;
	}

	/**
	 * 过滤失败类型，目前只过滤配置中的类型
	 */
	private boolean failReasonValid(Integer failReason) {
		// failReason基本不可能超过128, 使用contains应该没问题
		return !failReasonSet.contains(failReason);
	}

	/**
	 * 过滤LogType类型
	 * 
	 */
	private boolean logTypeValid(String logType) {
		return logTypeSet.contains(logType);
	}

	/**
	 * 时间检验
	 * 
	 * @param 时间
	 * @return
	 */
	private boolean tsValid(long ts) {
		return ts != 0l;
	}

	@Value("${flow.log.type}")
	public void setLogTypeSet(String val) {
		this.logTypeSet = Sets.newHashSet(StringUtils.split(val, ","));
	}

	@Value("${flow.filter.failreason}")
	public void setFailReasonSet(String val) {
		String[] strs = StringUtils.split(val, ",");
		Set<Integer> set = Sets.newHashSet();
		for (String str : strs) {
			set.add(NumberUtils.toInt(str, -1));
		}
		this.failReasonSet = set;
	}
}
