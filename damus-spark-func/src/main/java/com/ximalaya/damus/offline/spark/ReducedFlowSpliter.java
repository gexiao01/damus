// 文件名称: ReducedFlowToEntryMap.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.spark.api.java.function.FlatMapFunction;

import com.ximalaya.damus.common.util.Pair;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月24日
 */
public class ReducedFlowSpliter implements FlatMapFunction<ReducedFlow, Pair<DimType, Long>> {

	private static final long serialVersionUID = -2823077583756890221L;

	@Override
	public Iterable<Pair<DimType, Long>> call(ReducedFlow reducedFlow) {
		List<Pair<DimType, Long>> list = new ArrayList<Pair<DimType, Long>>();
		for (Entry<DimType, Long> entry : reducedFlow.getDims().entrySet()) {
			list.add(new Pair<DimType, Long>(entry.getKey(), entry.getValue()));
		}
		return list;
	}

}
