package com.ximalaya.damus.actuary.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ximalaya.damus.actuary.mock.MockActuaryService;
import com.ximalaya.damus.actuary.service.impl.BalancingDispatchService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * @author xiao.ge
 * @date 20151209
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class BalancingDispatchServiceTest {

	private BalancingDispatchService dispatchService = new BalancingDispatchService();

	private BalancingDispatchService balancingDispatchService;

	final MockActuaryService mockActuaryService = new MockActuaryService();

	@Before
	public void setUp() throws Exception {
		// do not need entire spring context, manually init
		mockActuaryService.setDelay(3000L); // defaalt mock calc time
		Field field = BalancingDispatchService.class.getDeclaredField("actuaryService");
		field.setAccessible(true);

		balancingDispatchService = (BalancingDispatchService) dispatchService;
		field.set(balancingDispatchService, mockActuaryService);

		balancingDispatchService.afterPropertiesSet();
	}

	@After
	public void cleanUp() throws Exception {
		balancingDispatchService.destroy();
	}

	/**
	 * No batch, submit ASAP
	 */
	@Test
	public void testNoBatch() throws Exception {

		// only one request, success for sure
		balancingDispatchService.setConfig(2, 4, 2, -1, -1L, Long.MAX_VALUE);
		concurrentRequest(1, true);
		// timeout
		balancingDispatchService.setConfig(2, 4, 2, -1, -1L, 1000L);
		concurrentRequest(1, false);

		Thread.sleep(3500L); // wait SubmitWorker to finish...

		// two requests
		balancingDispatchService.setConfig(1, 1, 1, -1, -1L, Long.MAX_VALUE);
		// 1.Calc 2.Block(pending)
		concurrentRequest(2, true);
		// overflow
		// 1.Calc 2.Block(pending) 3.overflow
		concurrentRequest(3, false);
	}

	/**
	 * With batch
	 */
	@Test
	public void testBatch() throws Exception {

		// maxPendingRequest, maxRunningRequest, maxConcurrency, batchSize,
		// batchInterval

		// batch=2
		balancingDispatchService.setConfig(2, 2, 1, 2, 1000L, Long.MAX_VALUE);
		concurrentRequest(4, true);
		concurrentRequest(5, false);

		// batch=2, concurrency=2
		balancingDispatchService.setConfig(2, 4, 2, 2, 1000L, Long.MAX_VALUE);
		concurrentRequest(6, true);
		concurrentRequest(7, false);
	}

	private void concurrentRequest(int successNum, boolean expectSucess) throws Exception {
		List<Requester> requesters = new ArrayList<Requester>(successNum + 1);
		for (int i = 0; i < successNum; i++) {
			requesters.add(new Requester(true));
		}
		if (!expectSucess) {
			requesters.set(successNum - 1, new Requester(false));
		}

		for (Requester requester : requesters) {
			requester.start();
			Thread.sleep(100L); // Guarantee order
		}

		for (Requester requester : requesters) {
			requester.join(); // wait for finish
		}
	}

	int n;

	class Requester extends Thread {

		boolean expectSuccess;

		public Requester(boolean expectSuccess) {
			this.expectSuccess = expectSuccess;
			setName("Requester-" + (++n));
		}

		@Override
		public void run() {
			try {
				long hits = balancingDispatchService.handleRequest(new CalcRequest());
				Assert.assertTrue(expectSuccess && hits == 100);
			} catch (DamusException e) {
				Assert.assertTrue(!expectSuccess);
			}
		}
	}

}
