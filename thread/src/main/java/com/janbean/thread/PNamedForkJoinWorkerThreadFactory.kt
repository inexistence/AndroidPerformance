package com.janbean.thread

import com.janbean.thread.util.ThreadTracker
import com.janbean.thread.util.ThreadTracker.trace
import com.janbean.thread.util.ThreadUtils
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinWorkerThread

@KeepThread
class PNamedForkJoinWorkerThreadFactory: ForkJoinPool.ForkJoinWorkerThreadFactory {

    private val name: String
    private val factory: ForkJoinPool.ForkJoinWorkerThreadFactory?
    var type: String = "NamedForkJoinWorkerThreadFactory"
    private var record: ThreadTracker.Record? = null

    constructor(name: String) : this(name, null)

    constructor(name: String, factory: ForkJoinPool.ForkJoinWorkerThreadFactory?) {
        this.name = name
        this.factory = factory
    }

    override fun newThread(pool: ForkJoinPool?): ForkJoinWorkerThread {
        val thread = factory?.newThread(pool) ?: object: ForkJoinWorkerThread(pool) {}
        thread.name = ThreadUtils.makeThreadName(thread.name, this.name)
        record = trace(type, thread.name)
        return thread
    }
}