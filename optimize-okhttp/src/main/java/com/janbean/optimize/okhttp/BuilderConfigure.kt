package com.janbean.optimize.okhttp

import okhttp3.OkHttpClient

interface BuilderConfigure {
    fun configDefaultBuilder(builder: OkHttpClient.Builder)
}