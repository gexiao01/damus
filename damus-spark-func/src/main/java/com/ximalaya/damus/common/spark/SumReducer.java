package com.ximalaya.damus.common.spark;

import org.apache.spark.api.java.function.Function2;

public class SumReducer implements Function2<Long, Long, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -368670179859790124L;

	@Override
	public Long call(Long arg0, Long arg1) throws Exception {
		return arg0 + arg1;
	}
}