package com.janbean.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.janbean.thread.util.ThreadTracker
import com.janbean.thread.util.ThreadUtils.MARK

@KeepThread
open class PHandler: Handler {
    @Volatile
    private var record: ThreadTracker.Record? = null
    private var prefix: String? = null

    constructor() : super()
    constructor(callback: Callback?) : super(callback)
    constructor(looper: Looper) : super(looper)
    constructor(looper: Looper, callback: Callback?) : super(looper, callback)

    constructor(prefix: String) : super() {
        this.prefix = prefix
    }
    constructor(callback: Callback?, prefix: String) : super(callback) {
        this.prefix = prefix
    }
    constructor(looper: Looper, prefix: String) : super(looper) {
        this.prefix = prefix
    }
    constructor(looper: Looper, callback: Callback?, prefix: String) : super(looper, callback) {
        this.prefix = prefix
    }

    override fun dispatchMessage(msg: Message) {
        var name = Thread.currentThread().name
        if (!name.startsWith(MARK) && prefix != null) {
            name = "$prefix/$name"
        }
        if (record != null) {
            ThreadTracker.startRun(name, record)
        } else {
            val isMainThread = looper == Looper.getMainLooper()
            if (!isMainThread) {
                record = ThreadTracker.startRun("HandlerThread", name)

                if (record == null) {
                    record = ThreadTracker.trace("Handler", name)
                    ThreadTracker.startRun(name, record)
                }
            }
        }
        super.dispatchMessage(msg)

        if (record != null) {
            ThreadTracker.endRun(name, record)
        }
    }
}