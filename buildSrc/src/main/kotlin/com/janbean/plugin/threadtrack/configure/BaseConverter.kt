package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.threadtrack.ThreadTrackTransform
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.MethodVisitor
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseConverter : IConverter {
    private lateinit var count: AtomicInteger

    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        count = ThreadTrackTransform.map.getOrPut(classContext.currentClassData.className) {
            AtomicInteger(1)
        }
    }

    fun putClassNameToArg(classContext: ClassContext, mv: MethodVisitor) {
        mv.visitLdcInsn(
            ThreadTrackTransform.MARK + count.getAndIncrement()
                .toString() + "." + classContext.currentClassData.className
        )
    }
}