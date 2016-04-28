package com.ximalaya.damus.offline.service;

import java.io.IOException;

import org.springframework.beans.factory.DisposableBean;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.meta.DimDict;

/**
 * core entry of damus-offline business
 * 
 * @author xiao.ge
 * @date 20151201
 */
public interface CalcService extends DisposableBean {

	/**
	 * 1.process raw flow data and generate reduced data. may include filtering,
	 * adapting, sampling, compressing, etc
	 * 
	 * 2.calculate the one dimensional distribution from current data and save
	 * 
	 * 3.record unrecognized dict items and save s
	 * 
	 * resulting reducedData should be persisted
	 * 
	 * @return dict of unrecognized dimension values, for merging usage
	 * @throws IOException
	 *             ,DamusException
	 */
	DimDict calcReduceflow() throws DamusException, IOException;

}
