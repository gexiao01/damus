// 文件名称: LineToFlowConverter.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import org.apache.spark.api.java.function.Function;

import com.ximalaya.damus.common.util.JsonUtils;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月11日
 */
public class LineToFlowConverter<F> implements Function<String, F> {

	/**
     * 
     */
	private static final long serialVersionUID = -7716566386919477705L;

	private Class<F> fClass;

	public LineToFlowConverter(Class<F> fClass) {
		this.fClass = fClass;
	}

	@Override
	public F call(String line) {
		try {
			return JsonUtils.fromJsonString(fClass, line);
		} catch (Exception e) {
			return null; // will be filtered in FlowFilter
		}
	}

}
