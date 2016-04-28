package com.ximalaya.damus.actuary.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.actuary.service.DispatchService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * a simple async impl of DispatchService
 * 
 * return immediately with request.id for later fetch. save results on local
 * disk
 * 
 * @author xiao.ge
 * @date 20151202
 */
public class SimpleAsyncDispatchService implements DispatchService {

	@Autowired
	private ActuaryService actuaryService;

	@Value("${damux.actuary.dispacth.maxConcurrency}")
	private int maxConcurrency;
	@Value("${damux.actuary.dispacth.asynSavePathPrefix}")
	private String savePathPrefix;

	// I really need a cocurrent Set, but there is only ConcurrentHashMap
	private final Collection<Long> runningRequests = new HashSet<Long>();

	Logger logger = Logger.getLogger(getClass());

	/**
	 * @return request.id this is truly asynic and do not return any real result
	 * @throws DamusException
	 */
	@Override
	public synchronized long handleRequest(final CalcRequest request) throws DamusException {
		if (runningRequests.size() >= maxConcurrency) {
			throw new DamusException("Thread pool is full. try later");
		}

		runningRequests.add(request.getId());
		new Thread() {
			@Override
			public void run() {
				Long hits = actuaryService.calculate(Arrays.asList(request)).get(request.getId());

				String savePath = savePathPrefix + "." + request.getId();
				try {
					FileUtils.writeStringToFile(new File(savePath), hits + "");
				} catch (IOException e) {
					logger.error("Fail to record asyn calculation: " + request + " hits=" + hits, e);
				}

				// TODO a purge thread to remove old files

				synchronized (SimpleAsyncDispatchService.this) {
					runningRequests.remove(request.getId());
				}
			}
		}.start();

		return request.getId();
	}

}
