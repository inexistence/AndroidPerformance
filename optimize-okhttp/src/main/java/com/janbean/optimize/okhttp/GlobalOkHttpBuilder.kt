package com.janbean.optimize.okhttp

import okhttp3.OkHttpClient

object GlobalOkHttpBuilder {
//    private val defaultCreator by lazy {
//        DefaultBuilderConfigure()
//    }

    private var creator: BuilderConfigure? = null

    @JvmStatic
    fun setDefaultBuilderCreator(creator: BuilderConfigure) {
        this.creator = creator
    }

    @JvmStatic
    fun configBuilder(builder: OkHttpClient.Builder) {
        creator?.configDefaultBuilder(builder)
    }
}
