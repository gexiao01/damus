package com.ximalaya.damus.actuary.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ximalaya.damus.actuary.service.ActuaryService;
import com.ximalaya.damus.actuary.service.DispatchService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.request.CalcRequest;

/**
 * a (complicated) implement of DispatchService.
 * 
 * employ a mixture of batch/async strategy to balance concurrency and responding legacy
 * 
 * there are two cases when Requests are delayed for submitting
 * 
 * 1. Busy case: when calculation load is full (#concurrency>=#maxConcurrency ||
 * #runningRequest>=#maxRunningRequest in this case Request will turn into #RequestStatus.BLOCKED
 * after at most {@link #batchInterval} ms. once any calculation returns, will immediately try
 * submitting BLOCKED Requests
 * 
 * 2. Idle case: when waiting following Requests to form a batch (since batch calculation is faster
 * than one by one) in this case Request will wait at most {@link #batchInterval} ms
 * 
 * @author xiao.ge
 * @date 20151202
 */
public class BalancingDispatchService implements DispatchService, InitializingBean, DisposableBean {

    @Autowired
    private ActuaryService actuaryService;

    @Value("${damux.actuary.dispacth.maxPendingRequest}")
    private int maxPendingRequest;
    @Value("${damux.actuary.dispacth.maxRunningRequest}")
    private int maxRunningRequest;
    @Value("${damux.actuary.dispacth.maxConcurrency}")
    private int maxConcurrency;
    @Value("${damux.actuary.dispacth.batchSize}")
    private int batchSize;
    @Value("${damux.actuary.dispacth.batchInterval}")
    private long batchInterval; // time to wait for collecting one batch, -1 if no delay wanted
    @Value("${damux.actuary.dispacth.requestTimeout}")
    private long requestTimeout; // max time to wait for a request (including queueing time)

    // any action on the following 4 fields must be protected under syncronization of
    // DispatchServiceImpl.this
    private LinkedBlockingQueue<CalcRequest> pendingRequests; // FIFO Queue of waiting requests
    private int runningRequest = 0; // equals at most #maxConcurrency*#batchSize
    private int concurrency = 0; // this is actually the number of running #SubmitWorker Threads

    // Map of results, do not need syncronization
    private final Map<Long, Result> results = new HashMap<Long, Result>();
    // periodically sumbit #pendingRequests in batches, works only if #batchInterval>0
    private Timer submitTimer;

    private static enum RequestStatus {
        NEW,
        BLOCKED, // due to calc capacity block
        FINISHED;
    }

    private class Result {
        private RequestStatus status = RequestStatus.NEW;
        private long hits = 0L;
        private final long id;

        public Result(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "(" + id + ":" + status + ")";
        }
    }

    Logger logger = Logger.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        pendingRequests = new LinkedBlockingQueue<CalcRequest>();
        if (batchInterval > 0) {

            // prevent batchSize<=0
            batchSize = Math.max(batchSize, 1);
            // should at least pend one batch
            maxPendingRequest = Math.max(maxPendingRequest, batchSize);

            submitTimer = new Timer();
            submitTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // on Timer invokation, must sumbit at least on batch (even not full)
                    // avoiding requests to wait too long
                    trySubmit(false);
                }
            }, 0L, batchInterval);
        } else {
            batchSize = Integer.MAX_VALUE; // just double check
        }
    }

    @Override
    public void destroy() throws Exception {
        if (submitTimer != null) {
            submitTimer.cancel();
        }
    }

    @Override
    public long handleRequest(CalcRequest request) throws DamusException {
        // synchronization in this class only maintains thread-safety of #pendingRequests and
        // #status. actual calculation (impl by #actuaryService.calculate()) is asynic (and time
        // consuming) and should not hold any locks

        Result result = new Result(request.getId());

        synchronized (pendingRequests) {
            logger.info("start handle Request: " + request + " pendingRequests="
                    + pendingRequests.size());
            if (pendingRequests.size() >= maxPendingRequest) {
                trySubmit(true); // make a last struggle

                if (pendingRequests.size() >= maxPendingRequest) {
                    // Ooops, cannot HOLD this
                    logger.warn("pending requests overflow " + request);
                    throw new DamusException(
                            "pending requests overflow, enlarge damux.actuary.dispacth.maxPendingRequest or try later");
                }
            }
            pendingRequests.add(request); // push to queue
            results.put(request.getId(), result); // register result

            if (batchInterval <= 0 || pendingRequests.size() >= batchSize) {
                // no batch or having a full batch, try a submit immediately (do not wait
                // #batchInterval)
                trySubmit(true);
            } else { // wait for #executeTimer to invoke submit trial (within #batchInterval ms)
            }
        }

        synchronized (result) {
            if (result.status != RequestStatus.FINISHED) {
                try {
                    result.wait(requestTimeout); // wait calculation
                } catch (InterruptedException e) {
                    // no manual interrupt so should not be here
                    throw new DamusException(e);
                }
            }
        }

        results.remove(request.getId()); // remove anyway, won't be null
        if (result.status == RequestStatus.FINISHED) { // success
            logger.info("handle Request success: " + request + " hits=" + result.hits);
            return result.hits;
        } else { // timeout
            // make a token to deletion when calculation finished
            // this is to avoid #finishedRequests being filled with timeout results
            logger.warn("Request Timeout: " + request);
            throw new DamusException("request timeout " + request);
        }
    }

    /**
     * Trial of submitting pending requests for (async) calculation. this method is expected to
     * return soon, while creating a collection of SubmitWorker threads.
     * 
     * @param fromTimer if false, all pending requests will be either submitted or turned into
     *            BLOCKED. if true, non-BLOCKED requests will not be submitted (and will wait for
     *            submitTimer)
     */
    private void trySubmit(boolean force) {
        synchronized (pendingRequests) {
            if (pendingRequests.isEmpty()) {
                return; // no pending requests
            }

            logger.info("Try submitting requests: force=" + force + " pendingRequests="
                    + pendingRequests.size());

            Collection<CalcRequest> requestBatch = new HashSet<CalcRequest>();

            // calculation capacity is always protected
            while (concurrency < maxConcurrency && runningRequest < maxRunningRequest) {

                if (pendingRequests.isEmpty()) {
                    if (!requestBatch.isEmpty()) { // async submit last batch
                        logger.info("Submitting requests: " + requestBatch);
                        changeStatus(requestBatch, true);

                        // number of Threads is controlled by #concurrency
                        // TODO should be better to employ Thread Pools instead of raw Threads
                        new SubmitWorker(requestBatch).start();
                    }
                    return;
                }

                if ((batchInterval > 0 && requestBatch.size() >= batchSize)
                        || pendingRequests.isEmpty()) {
                    // async submit full batch
                    // mark status
                    logger.info("Submitting requests: " + requestBatch);
                    changeStatus(requestBatch, true);
                    new SubmitWorker(requestBatch).start();
                    requestBatch = new HashSet<CalcRequest>(); // new batch
                }

                CalcRequest request = pendingRequests.peek(); // next request
                if (!results.containsKey(request.getId())) { // already timeout, discard
                    pendingRequests.poll();
                    continue;
                }

                if (requestBatch.isEmpty()
                        && results.get(request.getId()).status != RequestStatus.BLOCKED && !force) { // ==NEW
                    break; // if not #force, do not form a batch all of NEW requests
                }

                // can submit, add to batch
                pendingRequests.poll(); // equals #request
                requestBatch.add(request);
            }

            // in any cases, all unsubmitted request become BLOCKED
            logger.info("Blocking requests: " + pendingRequests.size());
            for (CalcRequest request : pendingRequests) {
                results.get(request.getId()).status = RequestStatus.BLOCKED;
            }
        }
    }

    /**
     * @param requests actually only size() is used
     * @param isAdd add or release task
     */
    private void changeStatus(Collection<CalcRequest> requests, boolean isAdd) {
        synchronized (pendingRequests) {
            int sign = isAdd ? 1 : -1;
            concurrency += sign;
            runningRequest += (requests.size() * sign);
        }
    }

    private class SubmitWorker extends Thread {
        private final Collection<CalcRequest> requestBatch;

        public SubmitWorker(Collection<CalcRequest> requestBatch) {
            this.requestBatch = requestBatch;
        }

        @Override
        public void run() {

            // worker thread block, waiting for calculation
            // do not let #actuaryService.calculate hold any locks!
            Map<Long, Long> batchResults = actuaryService.calculate(requestBatch);
            logger.info("Batch Results: " + batchResults);

            // release status
            changeStatus(requestBatch, false);

            // record results
            for (Entry<Long, Long> entry : batchResults.entrySet()) {
                Result result = results.get(entry.getKey());
                if (result != null) {
                    synchronized (result) {
                        result.status = RequestStatus.FINISHED;
                        result.hits = entry.getValue();
                        result.notifyAll(); // invoke result check
                    }
                }

            }

            // now we finish some calculation, try to handle BLOCKED Requests
            // this works in case of after a big request peak, we need to handle the pending request
            // (possibly several times size of #batchSize) ASAP, instead of waiting Timer

            // Note: trySubmit() is asynic so SubmitWorker.this thread will end soon
            trySubmit(false);
        }
    }

    public int getMaxPendingRequest() {
        return maxPendingRequest;
    }

    public int getMaxRunningRequest() {
        return maxRunningRequest;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public long getBatchInterval() {
        return batchInterval;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * for test usage
     * 
     * @param maxPendingRequest
     * @param maxRunningRequest
     * @param maxConcurrency
     * @param batchSize
     * @param batchInterval
     * @param requestTimeout
     * @throws Exception
     */
    public void setConfig(int maxPendingRequest, int maxRunningRequest, int maxConcurrency,
            int batchSize, long batchInterval, long requestTimeout) throws Exception {
        this.maxPendingRequest = maxPendingRequest;
        this.maxRunningRequest = maxRunningRequest;
        this.maxConcurrency = maxConcurrency;
        this.batchSize = batchSize;
        this.batchInterval = batchInterval;
        this.requestTimeout = requestTimeout;
        this.destroy();
        this.afterPropertiesSet();
    }

}
