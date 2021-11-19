package com.tang.platform.consistency.jraft.scheduler;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DefaultScheduler implements Scheduler {
    private final int minElectionTimetout;
    private final int maxElectionTimeout;
    private final int logReplicationDelay;
    private final int logReplicationInterval;
    private final Random electionTiomeoutRandom;
    private final ScheduledExecutorService scheduledExecutorService;

    public DefaultScheduler(int minElectionTimeout, int maxElectionTimeout, int logReplicationDelay, int logReplicationInterval) {
        if (maxElectionTimeout <= 0 || minElectionTimeout <= 0 || minElectionTimeout > maxElectionTimeout) {
            throw new IllegalArgumentException("election timeout config  is illegal");
        }
        if (logReplicationInterval <= 0 || logReplicationDelay < 0) {
            throw new IllegalArgumentException("log replication delay < 0 or log replication interval <=0");
        }
        this.minElectionTimetout = minElectionTimeout;
        this.maxElectionTimeout = maxElectionTimeout;
        this.logReplicationDelay = logReplicationDelay;
        this.logReplicationInterval = logReplicationInterval;
        this.electionTiomeoutRandom = new Random();
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "scheduler"));
    }

    @Override
    public LogReplicationScheduledContext scheduleLogReplicationTask(Runnable task) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(task, logReplicationDelay, logReplicationInterval, TimeUnit.MILLISECONDS);

        return new LogReplicationScheduledContext(scheduledFuture);
    }

    @Override
    public ElectionTimeout scheduleElectionTimeout(Runnable task) {
        int timeout = electionTiomeoutRandom.nextInt(maxElectionTimeout - minElectionTimetout) + minElectionTimetout;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(task, timeout, TimeUnit.MILLISECONDS);
        return new ElectionTimeout(scheduledFuture);
    }

    @Override
    public void stop() throws InterruptedException {
        scheduledExecutorService.shutdown();
    }
}
