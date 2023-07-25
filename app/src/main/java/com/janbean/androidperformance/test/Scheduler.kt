package com.janbean.androidperformance.test

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ExecutorScheduler {

    private val POOL_SIZE = 2
    private val KEEP_ALIVE_TIME = 10L

    private val threadFactory by lazy {
        object : NamedThreadFactory("startup", Thread.MAX_PRIORITY) {
            override fun newThread(r: Runnable?): Thread {
                return super.newThread(r)
            }
        }
    }

    private val executor by lazy {
        ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(),
            threadFactory).apply {
            allowCoreThreadTimeOut(true)
        }
    }

    fun schedule(runnable: Runnable) {
        executor.execute(runnable)
    }
}