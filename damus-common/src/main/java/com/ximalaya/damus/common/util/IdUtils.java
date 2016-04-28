package com.ximalaya.damus.common.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdUtils {

    private static AtomicLong al = new AtomicLong(System.currentTimeMillis());

    public static long generateId() {
        return al.incrementAndGet();
    }
}
