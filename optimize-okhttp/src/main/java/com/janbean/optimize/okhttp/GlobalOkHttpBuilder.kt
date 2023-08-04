package com.janbean.optimize.okhttp

import okhttp3.OkHttpClient

object GlobalOkHttpBuilder {
//    private val defaultCreator by lazy {
//        DefaultBuilderConfigure()
//    }

    private var configure: BuilderConfigure? = null

    @JvmStatic
    fun setDefaultBuilderConfigure(configure: BuilderConfigure) {
        this.configure = configure
    }

    @JvmStatic
    fun configBuilder(builder: OkHttpClient.Builder) {
        configure?.configDefaultBuilder(builder)
    }
}
