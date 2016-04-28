// 文件名称: EntryDataMapper.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline.spark;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.ximalaya.damus.common.util.Pair;
import com.ximalaya.damus.protocol.config.DimType;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月24日
 */
public class EntryDataMapper implements PairFunction<Pair<DimType, Long>, Pair<DimType, Long>, Long> {

	private static final long serialVersionUID = -2554277663623262076L;

	@Override
	public Tuple2<Pair<DimType, Long>, Long> call(Pair<DimType, Long> entry) {
		return new Tuple2<Pair<DimType, Long>, Long>(entry, 1L);
	}

}
