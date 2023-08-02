package com.janbean.optimize.okhttp

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

class DefaultBuilderConfigure : BuilderConfigure {
    companion object {
        private val dispatcher by lazy { Dispatcher() }
        private val connectionPool by lazy { ConnectionPool() }
    }

    override fun configDefaultBuilder(builder: OkHttpClient.Builder) {
        builder.dispatcher(dispatcher).connectionPool(connectionPool)
    }
}