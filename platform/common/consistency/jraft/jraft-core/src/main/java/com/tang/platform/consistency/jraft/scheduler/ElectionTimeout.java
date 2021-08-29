package com.tang.platform.consistency.jraft.scheduler;

import java.util.concurrent.ScheduledFuture;

public class ElectionTimeout extends ScheduledContext {
    public static final ElectionTimeout NONE=new ElectionTimeout(null);
    public ElectionTimeout(ScheduledFuture<?> scheduledFuture) {
        super("ElectionScheduledTask", scheduledFuture);
    }
}
