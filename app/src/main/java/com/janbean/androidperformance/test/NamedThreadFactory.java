package com.janbean.androidperformance.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private final ThreadFactory mDefaultThreadFactory = Executors.defaultThreadFactory();
    private final String mBaseName;
    private final AtomicInteger mCount = new AtomicInteger(0);
    private final int mPriority;

    public NamedThreadFactory(String baseName, int priority) {
        this.mBaseName = baseName;
        this.mPriority = priority;
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = this.mDefaultThreadFactory.newThread(runnable);
        thread.setName(this.mBaseName + "-" + this.mCount.getAndIncrement());
        thread.setPriority(this.mPriority);
        return thread;
    }
}
