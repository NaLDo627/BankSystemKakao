package com.kakaointerntask.bank.common;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimestampUtil {
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getFirstTimestampOfMonth(Timestamp timestamp) {
        return new Timestamp(getFirstMillisOfMonth(timestamp));
    }

    public static Timestamp getLastTimestampOfMonth(Timestamp timestamp) {
        return new Timestamp(getLastMillisOfMonth(timestamp));
    }

    public static long getFirstMillisOfMonth(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getLastMillisOfMonth(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getFirstMillisOfMonth(timestamp));
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTimeInMillis();
    }

    public static int compareTimestamp(Timestamp t1, Timestamp t2) {
        if (t1.getTime() > t2.getTime())
            return 1;
        else if (t1.getTime() < t2.getTime())
            return -1;
        return 0;
    }
}
