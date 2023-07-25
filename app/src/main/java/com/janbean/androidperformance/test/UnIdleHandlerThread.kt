package com.janbean.androidperformance.test

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.MessageQueue
import android.util.Log

open class UnIdleHandlerThread : HandlerThread {

    constructor(name: String?) : super(name)
    constructor(name: String?, priority: Int) : super(name, priority)

    private val heartBeatObj: Byte = 8

    override fun onLooperPrepared() {
        val handler = Handler(this.looper)
        Looper.myQueue().addIdleHandler(MessageQueue.IdleHandler {
            Log.i("UnIdleThread:${name}", "on idle")
            val hearBeatCode: Int = Int.MAX_VALUE
            handler.removeMessages(hearBeatCode, heartBeatObj)
            handler.sendMessageDelayed(handler.obtainMessage(hearBeatCode, heartBeatObj), 30000)
            false
        })
    }
}
