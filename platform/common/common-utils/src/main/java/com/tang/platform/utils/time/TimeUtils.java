package com.tang.platform.utils.time;

import java.time.Instant;

public class TimeUtils {
    public static final long EPOCH = 1420041600000L;
    private static final TimeUtils instance = new TimeUtils();

    private TimeUtils() {
    }

    public static TimeUtils getInstance() {
        return instance;
    }

    public long getCurrentEpochMilliStamp() {
        Instant now = Instant.now();
        return now.toEpochMilli();
    }

    public long getCurrentEpochStamp() {
        Instant now = Instant.now();
        return now.getEpochSecond();
    }

}
