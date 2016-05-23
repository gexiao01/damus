package com.ximalaya.damus.relativity.spark;

import java.util.EnumMap;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.ximalaya.damus.common.util.Pair;
import com.ximalaya.damus.protocol.config.DimType;

public class EnumTypes2RelativityDistMapper implements
		PairFunction<EnumMap<DimType, String>, Pair<String, String>, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1691539183004347440L;
	private final DimType dim1;
	private final DimType dim2;

	public EnumTypes2RelativityDistMapper(DimType dim1, DimType dim2) {
		this.dim1 = dim1;
		this.dim2 = dim2;
	}

	@Override
	public Tuple2<Pair<String, String>, Long> call(EnumMap<DimType, String> typeMap) throws Exception {
		return new Tuple2<Pair<String, String>, Long>(new Pair<String, String>(typeMap.get(dim1), typeMap.get(dim2)),
				1L);
	}
}
