package com.janbean.thread

import android.os.Handler
import android.os.HandlerThread
import com.janbean.thread.util.ThreadTracker

@KeepThread
class PHandlerThread: HandlerThread {
    private var mHandler: Handler? = null
    private var record: ThreadTracker.Record? = null

    constructor(name: String): super(name)
    constructor(name: String, priority: Int): super(name, priority)

    override fun start() {
        super.start()
        record = ThreadTracker.trace("HandlerThread", name)
    }

    override fun quit(): Boolean {
        return super.quit()
    }

    override fun quitSafely(): Boolean {
        return super.quitSafely()
    }

//    override fun getThreadHandler(): Handler {
//        if (mHandler == null) {
//            mHandler = object : Handler(looper) {
//                override fun dispatchMessage(msg: Message) {
//                    ThreadTracker.startRun(name, record)
//                    super.dispatchMessage(msg)
//                    ThreadTracker.endRun(name, record)
//                }
//            }
//        }
//        return mHandler!!
//    }
}