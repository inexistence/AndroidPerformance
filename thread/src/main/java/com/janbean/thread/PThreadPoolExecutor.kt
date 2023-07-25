package com.janbean.thread

import android.os.SystemClock
import android.util.Log
import com.janbean.thread.util.ThreadTracker
import java.util.concurrent.BlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@KeepThread
open class PThreadPoolExecutor : ThreadPoolExecutor {
    constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: BlockingQueue<Runnable>?,
        prefix: String?
    ) : super(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        NamedThreadFactory(prefix).apply { this.type = "ThreadPoolExecutor" }
    )

    constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: BlockingQueue<Runnable>?,
        threadFactory: ThreadFactory?,
        prefix: String?
    ) : super(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        NamedThreadFactory(threadFactory, prefix).apply { this.type = "ThreadPoolExecutor" }
    )

    constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: BlockingQueue<Runnable>?,
        handler: RejectedExecutionHandler?,
        prefix: String?
    ) : super(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        NamedThreadFactory(prefix).apply { this.type = "ThreadPoolExecutor" },
        handler
    )

    constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit?,
        workQueue: BlockingQueue<Runnable>?,
        threadFactory: ThreadFactory?,
        handler: RejectedExecutionHandler?,
        prefix: String?
    ) : super(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        NamedThreadFactory(threadFactory, prefix).apply { this.type = "ThreadPoolExecutor" },
        handler
    )

    override fun execute(command: Runnable?) {
        val trace = Thread.currentThread().stackTrace
        val stack1 = trace.firstOrNull {
            !it.className.contains("java.util.concurrent") && !it.className.contains(
                "dalvik.system.VMStack"
            ) && !it.className.contains("java.lang.Thread")
                    && !it.className.contains("com.imo.android.imoim.util.db.DbExecutor")
                    && !it.className.contains("bolts.Task")
                    && !it.className.contains("sg.bigo.core.task.AppExecutors")
                    && !it.className.startsWith("kotlinx.coroutines")
                    && !it.className.contains("androidx.work.impl.utils.SerialExecutor")
                    && !it.className.contains("com.imo.android.imoim.util.ExSerialExecutor")
                    && !it.className.startsWith("android.os")
                    && !it.className.startsWith("com.janbean.thread")
                    && !it.className.startsWith("com.live.share64.ClientExecutor")
                    && !it.className.startsWith("kotlin.coroutines")
        }
        val stack = if (stack1 == null) {
            trace.joinToString(",") { it.className + "#" + it.lineNumber }
        } else {
            stack1?.className + "#" + stack1?.lineNumber
        }
        super.execute(if (command != null) {
            Runnable {
                val name = Thread.currentThread().name
                val record = (threadFactory as? NamedThreadFactory)?.record
                if (record?.corePoolSize == null) {
                    record?.corePoolSize = corePoolSize
                    record?.maximumPoolSize = maximumPoolSize
                    record?.keepAliveTime = getKeepAliveTime(TimeUnit.MILLISECONDS)
                    record?.allowCoreThreadTimeout = allowsCoreThreadTimeOut()
                }
                val start = SystemClock.elapsedRealtime()
                ThreadTracker.startRun(name, record)
                command.run()
                ThreadTracker.endRun(name, record)
                Log.i("hjianbin2", "$name execute $stack cost=${SystemClock.elapsedRealtime() - start}")
            }
        } else null)
    }

    fun setType(type: String) {
        (threadFactory as? NamedThreadFactory)?.type = type
    }
}