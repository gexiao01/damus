package com.ximalaya.damus.offline.func;

import com.ximalaya.damus.offline.protocol.TestFlow;
import com.ximalaya.damus.offline.spark.AbstractFlowFilter;

public class TestFlowFilter extends AbstractFlowFilter<TestFlow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8478695059762493123L;

	@Override
	protected boolean doFilter(TestFlow flow) {
		return flow != null && flow.getType() < 100;
	}

}
