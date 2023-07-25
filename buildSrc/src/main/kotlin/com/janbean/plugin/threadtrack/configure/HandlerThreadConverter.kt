package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor

class HandlerThreadConverter : BaseConverter() {
    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)
        builder.transferConstructor(
            from = Method(
                "android/os/HandlerThread",
                "<init>",
                "(Ljava/lang/String;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandlerThread",
                "<init>",
                "(Ljava/lang/String;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "android/os/HandlerThread",
                "<init>",
                "(Ljava/lang/String;I)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandlerThread",
                "<init>",
                "(Ljava/lang/String;ILjava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        )
    }
}