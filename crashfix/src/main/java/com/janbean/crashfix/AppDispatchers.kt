package com.janbean.crashfix

import android.os.Looper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService

object AppDispatchers {
    private var threadPoolFactory: ThreadPoolFactory? = null

    const val NAME_IO = "global-io-thread"
    const val NAME_DEFAULT = "default"

    @JvmStatic
    val MAIN: MainCoroutineDispatcher by lazy {
        Looper.getMainLooper().asHandler(false).asCoroutineDispatcher("ui")
    }

    @JvmStatic
    val IO: CoroutineDispatcher by lazy {
        threadPoolFactory?.newThreadPool(NAME_IO)?.asCoroutineDispatcher()
            ?: kotlinx.coroutines.Dispatchers.IO
    }

    @JvmStatic
    val DEFAULT: CoroutineDispatcher by lazy {
        threadPoolFactory?.newThreadPool(NAME_DEFAULT)?.asCoroutineDispatcher() ?: kotlinx.coroutines.Dispatchers.Default
    }

    fun setThreadPoolFactory(threadPoolFactory: ThreadPoolFactory) {
        this.threadPoolFactory = threadPoolFactory
    }

    interface ThreadPoolFactory {
        fun newThreadPool(name: String): ExecutorService
    }
}