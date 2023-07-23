package com.janbean.plugin.util.replace

import org.objectweb.asm.MethodVisitor

data class TransferMethod(
    val opcode: Int,
    val from: Method,
    val to: Method,
    val beforeReplace: ((mv: MethodVisitor) -> Unit)? = null,
    val afterReplace: ((mv: MethodVisitor) -> Unit)? = null
)

data class TransferType(
    val opcode: Int,
    val from: String,
    val to: String
)