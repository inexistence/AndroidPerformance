package com.janbean.thread

import com.janbean.thread.util.ThreadTracker
import com.janbean.thread.util.ThreadUtils

/**
 * Initiazlize {@code Thread} with new name, these constructors are used by {@code ThreadTrackTransformer} for renaming
 */
@KeepThread
open class PThread: Thread {
    @Volatile
    private var record: ThreadTracker.Record? = null
    var type: String? = null
    var trace: Boolean? = true

    constructor(prefix: String?): super(ThreadUtils.makeThreadName(prefix))
    constructor(target: Runnable?, prefix: String?): super(target, ThreadUtils.makeThreadName(prefix))
    constructor(group: ThreadGroup?, target: Runnable?, prefix: String?): super(group, target, ThreadUtils.makeThreadName(prefix))
    constructor(name: String?, prefix: String?): super(ThreadUtils.makeThreadName(name, prefix))
    constructor(group: ThreadGroup?, name: String?, prefix: String?): super(group, ThreadUtils.makeThreadName(name, prefix))
    constructor(runnable: Runnable?, name: String?, prefix: String?): super(runnable, ThreadUtils.makeThreadName(name, prefix))
    constructor(group: ThreadGroup?,runnable: Runnable?, name: String?, prefix: String?): super(group, runnable, ThreadUtils.makeThreadName(name, prefix))
    constructor(group: ThreadGroup?,runnable: Runnable?, name: String?, stackSize: Long, prefix: String?): super(group, runnable, ThreadUtils.makeThreadName(name, prefix), stackSize)

    fun setName(name: String, prefix: String) {
        super.setName(ThreadUtils.makeThreadName(name, prefix))
    }

    override fun start() {
        if (trace != false) {
            if (type.isNullOrBlank()) {
                type = this.javaClass.name
            }
            record = ThreadTracker.trace(type ?: this.javaClass.name, name)
        }
        super.start()
    }

    override fun run() {
        if (record != null) {
            ThreadTracker.startRun(name, record)
        }
        super.run()
        if (record != null) {
            ThreadTracker.endRun(name, record)
        }
    }
}
