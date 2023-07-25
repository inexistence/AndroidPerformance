package com.janbean.thread;


import static com.janbean.thread.util.ThreadUtils.setThreadName;

import com.janbean.thread.util.ThreadTracker;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@KeepThread
class NamedThreadFactory implements ThreadFactory {

    public String type = "NamedThreadFactory";

    /**
     * Used by {@code ThreadTransformer} for thread renaming
     *
     * @param prefix the prefix of name
     * @return an instance of ThreadFactory
     */
    public static ThreadFactory newInstance(final String prefix) {
        return new NamedThreadFactory(prefix);
    }

    /**
     * Used by {@code ThreadTransformer} for thread renaming
     *
     * @param factory the factory delegate
     * @param prefix  the prefix of name
     * @return an instance of ThreadFactory
     */
    public static ThreadFactory newInstance(final ThreadFactory factory, final String prefix) {
        return new NamedThreadFactory(factory, prefix);
    }

    private final String name;
    private final AtomicInteger counter = new AtomicInteger(1);
    private final ThreadGroup group;
    private final ThreadFactory factory;

    public ThreadTracker.Record record;

    public NamedThreadFactory(final String name) {
        this(null, name);
    }

    public NamedThreadFactory(final ThreadFactory factory, final String name) {
        this.factory = factory;
        this.name = name;
        this.group = Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(final Runnable r) {
        if (null == this.factory) {
            final PThread t = new PThread(this.group, r, this.name + "#" + this.counter.getAndIncrement(), 0, "");
            t.setType(type);
            t.setTrace(false);

            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            record = ThreadTracker.trace(type, t.getName());
            return t;
        }
        Thread t = this.factory.newThread(r);
        if (t instanceof PThread) {
            ((PThread) t).setType(type);
            ((PThread) t).setTrace(false);
        }
        t = setThreadName(t, this.name);
        record = ThreadTracker.trace(type, t.getName());
        return t;
    }
}
