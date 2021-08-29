package com.tang.platform.consistency.jraft.task;

import java.util.concurrent.*;

public class SingleThreadTaskExecutor implements TaskExecutor {
    private final ExecutorService executorService;

    public SingleThreadTaskExecutor() {
        this(Executors.defaultThreadFactory());
    }

    public SingleThreadTaskExecutor(ThreadFactory threadFactory) {
        this.executorService = Executors.newSingleThreadExecutor(threadFactory);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) {
        return executorService.submit(task);
    }

    @Override
    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
