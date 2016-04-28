package com.ximalaya.damus.common.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateUtils {

    private static final FastDateFormat TIME_TO_MILLIS_FORMAT = FastDateFormat
            .getInstance("HH:mm:ss.SS");

    private static final FastDateFormat TIME_TO_FORMAT_STRING = FastDateFormat
            .getInstance("yyyy-MM-dd HH:mm:ss");

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String formatTimeToMillis(Date date) {
        return TIME_TO_MILLIS_FORMAT.format(date);
    }

    public static String formatTime(Date date) {
        if (date == null) {
            date = new Date();
        }
        return TIME_TO_FORMAT_STRING.format(date);
    }

    public static String formatDateTime(Date time, String format) {
        if (time == null) {
            return null;
        }
        return FastDateFormat.getInstance(format).format(time);
    }

    public static Date parseDateTime(String time, String format) {
        if (time == null) {
            return null;
        }

        try {
            return FastDateFormat.getInstance(format).parse(time);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static int getLongTimeHour(long ts) {
        return (int) ((ts % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));  
    }
}
