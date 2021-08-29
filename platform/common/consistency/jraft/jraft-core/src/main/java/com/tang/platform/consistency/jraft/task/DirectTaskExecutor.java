package com.tang.platform.consistency.jraft.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class DirectTaskExecutor implements TaskExecutor{
    @Override
    public Future<?> submit(Runnable runnable) {
        FutureTask<?> futureTask = new FutureTask<>(runnable,null);
        futureTask.run();
        return futureTask;
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) {
        FutureTask<V> futureTask = new FutureTask<>(task);
        futureTask.run();
        return futureTask;
    }

    @Override
    public void shutdown() throws InterruptedException {
        //do nothing

    }
}
