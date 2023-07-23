package com.janbean.thread

import android.os.HandlerThread
import com.janbean.thread.util.ThreadTracker

@KeepThread
class PHandlerThread: HandlerThread {
    constructor(name: String): super(name)
    constructor(name: String, priority: Int): super(name, priority)


    override fun start() {
        super.start()
        ThreadTracker.trace("ThreadHandler", name)
    }
}