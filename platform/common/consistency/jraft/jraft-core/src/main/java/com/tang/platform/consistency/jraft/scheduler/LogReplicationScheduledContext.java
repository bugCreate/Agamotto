package com.tang.platform.consistency.jraft.scheduler;

import java.util.concurrent.ScheduledFuture;

public class LogReplicationScheduledContext extends ScheduledContext {
    public LogReplicationScheduledContext(ScheduledFuture<?> scheduledFuture) {
        super("LogReplicationScheduledTask", scheduledFuture);
    }
}
