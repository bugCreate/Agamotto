package com.tang.platform.consistency.jraft.scheduler;

public interface Scheduler {
    LogReplicationScheduledContext scheduleLogReplicationTask(Runnable task);

    //定时等待选举超时，超时执行超时策略
    ElectionTimeout scheduleElectionTimeout(Runnable task);

    void stop() throws InterruptedException;
}
