package com.janbean.optimize.firebase

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object FirebaseExecutors {
    private val executor by lazy {
        val var2 = ThreadPoolExecutor(
            1,
            1,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            NamedThreadFactory("firebase-iid-executor")
        )
        var2.allowCoreThreadTimeOut(true)
        Executors.unconfigurableExecutorService(var2)
    }

    @JvmStatic
    fun getExecutorForCloudMessagingReceiver(originExecutor: Executor?): Executor {
        return executor
    }
}
