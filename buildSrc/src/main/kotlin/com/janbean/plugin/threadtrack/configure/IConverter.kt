package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.MethodReplaceVisitor

interface IConverter {
    fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder)
}
