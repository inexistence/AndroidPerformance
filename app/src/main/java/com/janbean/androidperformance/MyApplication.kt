package com.janbean.androidperformance

import android.app.Application
import android.content.Context

class MyApplication: Application() {
    companion object {
        var instance: Application? = null
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MyApplication.instance = this
    }
}