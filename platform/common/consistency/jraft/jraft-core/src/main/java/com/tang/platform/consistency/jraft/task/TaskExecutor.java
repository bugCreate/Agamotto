package com.tang.platform.consistency.jraft.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface TaskExecutor {
    Future<?> submit(Runnable runnable);

    <V> Future<V> submit(Callable<V> task);

    void shutdown() throws InterruptedException;
}
