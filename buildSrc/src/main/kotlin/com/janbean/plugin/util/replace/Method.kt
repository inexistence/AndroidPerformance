package com.janbean.plugin.util.replace

data class Method(
    val owner: String,
    val name: String,
    val descriptor: String,
    val isInterface: Boolean? = null,
)