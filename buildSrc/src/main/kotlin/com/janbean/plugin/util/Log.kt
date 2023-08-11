package com.janbean.plugin.util

object Log {
    const val DEBUG = true

    fun i(tag: String, msg: String) {
        println("$tag: $msg")
    }

    fun d(tag: String, msg: String) {
        if (DEBUG) {
            println("$tag: $msg")
        }
    }

    fun e(tag: String, msg: String) {
        println("$tag: $msg")
    }
}
