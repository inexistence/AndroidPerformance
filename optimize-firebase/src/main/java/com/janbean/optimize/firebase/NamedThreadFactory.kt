package com.janbean.optimize.firebase

import android.os.Process
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

class NamedThreadFactory(private val name: String) : ThreadFactory {
    private val threadFactory = Executors.defaultThreadFactory()
    override fun newThread(var1: Runnable): Thread {
        val runnable = WrapRunnable(var1, 0)
        val thread = threadFactory.newThread(runnable)
        thread.name = name
        return thread
    }
}


internal class WrapRunnable(private val zza: Runnable, val priority: Int) : Runnable {
    override fun run() {
        Process.setThreadPriority(priority)
        zza.run()
    }
}

