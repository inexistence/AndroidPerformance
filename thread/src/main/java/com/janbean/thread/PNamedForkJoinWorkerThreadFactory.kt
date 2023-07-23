package com.janbean.thread

import com.janbean.thread.util.ThreadUtils
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinWorkerThread

@KeepThread
class PNamedForkJoinWorkerThreadFactory: ForkJoinPool.ForkJoinWorkerThreadFactory {

    private val name: String
    private val factory: ForkJoinPool.ForkJoinWorkerThreadFactory?

    constructor(name: String) : this(name, null)

    constructor(name: String, factory: ForkJoinPool.ForkJoinWorkerThreadFactory?) {
        this.name = name
        this.factory = factory
    }

    override fun newThread(pool: ForkJoinPool?): ForkJoinWorkerThread {
        val thread = factory?.newThread(pool) ?: object: ForkJoinWorkerThread(pool) {}
        thread.name = ThreadUtils.makeThreadName(thread.name, this.name)
        return thread
    }
}