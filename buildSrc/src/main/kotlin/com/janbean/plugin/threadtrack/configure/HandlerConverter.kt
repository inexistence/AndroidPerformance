package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor

class HandlerConverter: BaseConverter() {
    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)
        builder.transferConstructor(
            from = Method(
                "android/os/Handler",
                "<init>",
                "()V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandler",
                "<init>",
                "(Ljava/lang/String;)V",
                false
            ),
            beforeReplace = {
                putClassNameToArg(classContext, it)
            }
        ).transferConstructor(
            from = Method(
                "android/os/Handler",
                "<init>",
                "(Landroid/os/Callback;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandler",
                "<init>",
                "(Landroid/os/Callback;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = {
                putClassNameToArg(classContext, it)
            }
        ).transferConstructor(
            from = Method(
                "android/os/Handler",
                "<init>",
                "(Landroid/os/Looper;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandler",
                "<init>",
                "(Landroid/os/Looper;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = {
                putClassNameToArg(classContext, it)
            }
        ).transferConstructor(
            from = Method(
                "android/os/Handler",
                "<init>",
                "(Landroid/os/Looper;Landroid/os/Callback;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PHandler",
                "<init>",
                "(Landroid/os/Looper;Landroid/os/Callback;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = {
                putClassNameToArg(classContext, it)
            }
        )
    }
}