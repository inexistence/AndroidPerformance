package com.janbean.crashfix;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangjianbin on 2022/5/20
 */

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
