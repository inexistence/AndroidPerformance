package com.janbean.thread.util

data class ThreadInfo(
    val id: Long,
    val name: String,
    val state: Thread.State?
)