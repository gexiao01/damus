package com.ximalaya.damus.common.util;

public class ThreadUtil {

    public static void waitQuitely(Object obj) {
        try {
            obj.wait();
        } catch (InterruptedException e) {
        }
    }

    public static void sleepQuitely(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
