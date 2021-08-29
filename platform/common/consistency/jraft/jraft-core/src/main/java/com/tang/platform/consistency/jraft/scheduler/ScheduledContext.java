package com.tang.platform.consistency.jraft.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledContext {
    private final String name;
    private final ScheduledFuture<?> scheduledFuture;

    public ScheduledContext(String name, ScheduledFuture<?> scheduledFuture) {
        this.name = name;
        this.scheduledFuture = scheduledFuture;
    }

    public void cancel() {
        this.scheduledFuture.cancel(false);
    }

    @Override
    public String toString() {

        if (scheduledFuture.isCancelled()) {
            return name + "(state=cancelled)";
        }
        if (scheduledFuture.isDone()) {
            return name + "(state=done)";
        }
        return name + "(delay=" + scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + "ms)";
    }
}
