package com.janbean.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@KeepThread
public class Executors {
    public static ThreadFactory defaultThreadFactory(String name) {
        return new PDefaultThreadFactory(name);
    }

    public static ExecutorService newSingleThreadExecutor(String name) {
//        NamedThreadFactory factory = new NamedThreadFactory(name);
//        factory.type = "SingleThread";
        // TODO 自己实现，好计算任务耗时
//        return java.util.concurrent.Executors.newSingleThreadExecutor(factory);
        PThreadPoolExecutor executor = new PThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                name);
        executor.setType("SingleThread");
        return executor;
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory factory, String name) {
//        NamedThreadFactory proxyFactory = new NamedThreadFactory(factory, name);
//        proxyFactory.type = "SingleThread";
//        // TODO 自己实现，好计算任务耗时
//        return java.util.concurrent.Executors.newSingleThreadExecutor(proxyFactory);
        PThreadPoolExecutor executor = new PThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                factory, name);
        executor.setType("SingleThread");
        return executor;
    }

    public static ExecutorService newFixedThreadPool(int nThreads, String name) {
        PThreadPoolExecutor executorService = new PThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), name);
        executorService.setType("FixedThread");
        return executorService;
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory factory, String name) {
        PThreadPoolExecutor executorService = new PThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), factory, name);
        executorService.setType("FixedThread");
        return executorService;
    }

    public static ExecutorService newCachedThreadPool(String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(name);
        proxyFactory.type = "CachedThread";
        // TODO 自己实现，好计算任务耗时
        return java.util.concurrent.Executors.newCachedThreadPool(proxyFactory);
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory factory, String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(factory, name);
        proxyFactory.type = "CachedThread";
        // TODO 自己实现，好计算任务耗时
        return java.util.concurrent.Executors.newCachedThreadPool(proxyFactory);
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(final String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(name);
        proxyFactory.type = "SingleThreadScheduled";
        return java.util.concurrent.Executors.newSingleThreadScheduledExecutor(proxyFactory);
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(final ThreadFactory factory, final String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(factory, name);
        proxyFactory.type = "SingleThreadScheduled";
        return java.util.concurrent.Executors.newSingleThreadScheduledExecutor(proxyFactory);
    }

    public static ScheduledExecutorService newScheduledThreadPool(final int corePoolSize, final String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(name);
        proxyFactory.type = "ScheduledThread";
        return java.util.concurrent.Executors.newScheduledThreadPool(corePoolSize, proxyFactory);
    }

    public static ScheduledExecutorService newScheduledThreadPool(final int corePoolSize, final ThreadFactory factory, final String name) {
        NamedThreadFactory proxyFactory = new NamedThreadFactory(factory, name);
        proxyFactory.type = "ScheduledThread";
        return java.util.concurrent.Executors.newScheduledThreadPool(corePoolSize, proxyFactory);
    }
    public static ExecutorService newWorkStealingPool(final String name) {
        PNamedForkJoinWorkerThreadFactory proxyFactory = new PNamedForkJoinWorkerThreadFactory(name);
        proxyFactory.setType("WorkStealing");
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), proxyFactory, null, true);
    }

    public static ExecutorService newWorkStealingPool(final int parallelism, final String name) {
        PNamedForkJoinWorkerThreadFactory proxyFactory = new PNamedForkJoinWorkerThreadFactory(name);
        proxyFactory.setType("WorkStealing");
        return new ForkJoinPool(parallelism, proxyFactory, null, true);
    }
}
