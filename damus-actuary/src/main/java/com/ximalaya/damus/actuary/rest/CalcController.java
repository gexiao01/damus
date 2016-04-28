package com.ximalaya.damus.actuary.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dp.simplerest.annotation.Rest;
import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.actuary.service.RequestParser;
import com.ximalaya.damus.actuary.service.DispatchService;
import com.ximalaya.damus.actuary.service.EstimateService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

@Component
@Rest
public class CalcController {

	@Autowired
	private DispatchService dispatchService;
	@Autowired
	private EstimateService estimateService;
	@Autowired
	private RequestParser requestService;
	@Autowired
	private ActuaryService actuaryService;

	@Rest(path = "/ad/actuary")
	public long actuary(String target) throws DamusException {
		CalcRequest request = getRequest(target);
		return dispatchService.handleRequest(request);
	}

	// @Rest(path = "/ad/actuaryBatch")
	// public Map<Long, Long> actuaryBatch(String ids) throws DamusException {
	// Collection<CalcRequest> requests = new ArrayList<CalcRequest>();
	// // TODO impl at dispatchService level
	// Map<Long, Long> idMap = new HashMap<Long, Long>();
	// for (String idStr : ids.split(",")) {
	// long id = Long.valueOf(idStr);
	// CalcRequest request = getRequest(id);
	// idMap.put(request.getId(), id);
	// requests.add(request);
	// }
	//
	// Map<Long, Long> reqResult = actuaryService.calculate(requests);
	// Map<Long, Long> result = new HashMap<Long, Long>();
	// for (Entry<Long, Long> entry : reqResult.entrySet()) {
	// result.put(idMap.get(entry.getKey()), entry.getValue());
	// }
	//
	// return result;
	// }

	@Rest(path = "/ad/estimate")
	public long estimate(String target) throws DamusException {
		CalcRequest request = getRequest(target);
		return estimateService.handleRequest(request);
	}

	private CalcRequest getRequest(String target) {
		return requestService.parseCalcRequest(target);
	}

}
