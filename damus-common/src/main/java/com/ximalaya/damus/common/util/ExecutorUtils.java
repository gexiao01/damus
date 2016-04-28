package com.ximalaya.damus.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ExecutorUtils {

    static final Logger logger = Logger.getLogger(ExecutorUtils.class);

    private static final int EXECUTOR_TERMINATION_TIMEOUT = 60;

    public static void shutdownAndWait(ExecutorService executorService, String name) {
        logger.info("Shutting down " + name + " and wait.");
        executorService.shutdown();
        awaitTermination(executorService, name);
    }

    public static void shutdownNow(ExecutorService executorService, String name) {
        logger.info("Shutting down " + name + " now.");
        executorService.shutdownNow();
        awaitTermination(executorService, name);
    }

    public static void shutdownImmediately(ExecutorService executor, String name) {
        logger.info("Shutting down " + name + " immediately.");
        executor.shutdownNow();
    }

    private static void awaitTermination(ExecutorService executor, String name) {
        try {
            while (!executor.awaitTermination(EXECUTOR_TERMINATION_TIMEOUT, TimeUnit.SECONDS)) {
                logger.info("Waiting for all tasks complete execution in " + name);
            }
            logger.info(name + " is shut down.");
        } catch (InterruptedException e) {
            logger.error("Shutting down " + name + " failed.", e);
            Thread.currentThread().interrupt();
        }
    }
}
