package com.janbean.thread

import com.janbean.thread.util.ThreadTracker
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

@KeepThread
class PScheduledThreadPoolExecutor : ScheduledThreadPoolExecutor {
    constructor(corePoolSize: Int, prefix: String?) : super(
        corePoolSize,
        NamedThreadFactory(prefix).apply {
            type = "ScheduledThreadPoolExecutor"
        })

    constructor(corePoolSize: Int, threadFactory: ThreadFactory?, prefix: String?) : super(
        corePoolSize,
        NamedThreadFactory(threadFactory, prefix).apply {
            type = "ScheduledThreadPoolExecutor"
        }
    )

    constructor(corePoolSize: Int, handler: RejectedExecutionHandler?, prefix: String?) : super(
        corePoolSize,
        NamedThreadFactory(prefix).apply {
            type = "ScheduledThreadPoolExecutor"
        },
        handler
    )

    constructor(
        corePoolSize: Int,
        threadFactory: ThreadFactory?,
        handler: RejectedExecutionHandler?,
        prefix: String?
    ) : super(corePoolSize, NamedThreadFactory(threadFactory, prefix).apply {
        type = "ScheduledThreadPoolExecutor"
    }, handler)

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        val name = Thread.currentThread().name
        val record = (threadFactory as? NamedThreadFactory)?.record
        if (record?.corePoolSize == null) {
            record?.corePoolSize = corePoolSize
            record?.maximumPoolSize = maximumPoolSize
            record?.keepAliveTime = getKeepAliveTime(TimeUnit.MILLISECONDS)
            record?.allowCoreThreadTimeout = allowsCoreThreadTimeOut()
        }
        ThreadTracker.startRun(name, record)

        super.beforeExecute(t, r)
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)
        val name = Thread.currentThread().name
        val record = (threadFactory as? NamedThreadFactory)?.record
        ThreadTracker.endRun(name, record)
    }
}