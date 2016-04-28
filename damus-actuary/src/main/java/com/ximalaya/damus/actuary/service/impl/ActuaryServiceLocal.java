package com.ximalaya.damus.actuary.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.common.util.ThreadUtil;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.request.CalcRequest;
import com.ximalaya.damus.protocol.resource.Resource;
import com.ximalaya.damus.protocol.util.SerializeUtil;

/**
 * Single Site impl of ActuaryService, employing request merging
 * 
 * Register as spring bean to utilize
 * 
 * @author xiao.ge
 * @date 20151209
 */
public class ActuaryServiceLocal implements ActuaryService, InitializingBean, DisposableBean {

	private Logger logger = Logger.getLogger(getClass());

	private final CalcWorker calcWorker = new CalcWorker();

	@Value("${damus.offline.sample}")
	private double sampleRatio;

	private final Map<Long, RequestStatus> status = new HashMap<Long, RequestStatus>();

	@Autowired
	@Qualifier("reducedFlowResource")
	private Resource<ReducedFlow> flowResource;

	@Override
	public void afterPropertiesSet() throws Exception {
		calcWorker.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroy() throws Exception {
		calcWorker.stop();
	}

	@Override
	public Map<Long, Long> calculate(Collection<CalcRequest> requests) {

		Map<Long, Long> results = new HashMap<Long, Long>();
		if (CollectionUtils.isEmpty(requests)) {
			return results;
		}

		RequestStatus waitingRequest = null;
		synchronized (status) {
			for (CalcRequest request : requests) {
				waitingRequest = new RequestStatus(request, calcWorker.pos);
				status.put(request.getId(), waitingRequest);
			}
			status.notifyAll(); // invoke calculation

		}

		synchronized (waitingRequest) {
			while (!waitingRequest.finish) {
				ThreadUtil.waitQuitely(waitingRequest); // wait for calculation
			}
		}

		synchronized (status) {
			for (CalcRequest request : requests) {
				RequestStatus ret = status.remove(request.getId()); // won't be
																	// null
				long unsampledHits = (long) (ret.hits / sampleRatio);
				results.put(request.getId(), unsampledHits);
			}
		}
		return results;
	}

	private static class RequestStatus {
		CalcRequest request;
		long hits = 0L;
		long offset;
		boolean finish = false;

		RequestStatus(CalcRequest request, long offset) {
			this.request = request;
			this.offset = offset;
		}
	}

	/**
	 * singlet calucation impl, dynamically merging requests
	 * 
	 * here we do not limit request concurrency, which should be managed in
	 * DispatchService
	 */
	private class CalcWorker extends Thread {

		long pos;

		@Override
		public void run() {
			while (true) {
				InputStream is = null;

				try {
					// XXX do not support wild card Resource, don't use this in
					// product env
					// Spark-local is suitable for local test
					is = flowResource.getInputStream();
					int corrupted = 0;
					for (pos = 0; is.available() > 0; pos++) {

						try {
							ReducedFlow flow = SerializeUtil.deserializeFromStream(is, ReducedFlow.class);

							synchronized (status) {
								if (MapUtils.isEmpty(status)) {
									ThreadUtil.waitQuitely(status); // wait for
																	// new
																	// requests
								}

								List<RequestStatus> finishedRequests = new ArrayList<RequestStatus>();
								for (RequestStatus requestStatus : status.values()) {
									if (!requestStatus.finish && requestStatus.request.hit(flow.getDims())) {
										requestStatus.hits++;
										if (pos == requestStatus.offset - 1
												|| (requestStatus.offset == 0 && is.available() <= 0)) {
											requestStatus.finish = true;
											finishedRequests.add(requestStatus);
										}
									}
								}

								for (RequestStatus finishedRquest : finishedRequests) {
									// notify result check
									synchronized (finishedRquest) {
										finishedRquest.notifyAll();
									}
								}
							}

						} catch (DamusException e) {
							if (corrupted++ == 1000) {
								logger.warn("more than 1000 lines corrupted in " + flowResource, e);
							}
						}
					}

					flowResource.closeInputStream();
				} catch (IOException e) {
					logger.error("IO Error when loading " + flowResource, e);
					break; // cannot load resource, quit the loop
				} finally {
					flowResource.closeInputStream();
				}
			}
		}
	}
}
