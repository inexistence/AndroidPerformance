package com.janbean.androidperformance

import android.app.Application
import android.content.Context
import com.janbean.optimize.okhttp.BuilderConfigure
import com.janbean.optimize.okhttp.GlobalOkHttpBuilder
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

class MyApplication: Application() {
    companion object {
        private val okHttpDispatcher by lazy { Dispatcher() }
        private val okHttpConnectionPool by lazy { ConnectionPool() }
        var instance: Application? = null
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MyApplication.instance = this
    }

    override fun onCreate() {
        super.onCreate()

        GlobalOkHttpBuilder.setDefaultBuilderConfigure(object : BuilderConfigure {
            override fun configDefaultBuilder(builder: OkHttpClient.Builder) {
                builder.dispatcher(okHttpDispatcher)
                builder.connectionPool(okHttpConnectionPool)
            }
        })
    }
}