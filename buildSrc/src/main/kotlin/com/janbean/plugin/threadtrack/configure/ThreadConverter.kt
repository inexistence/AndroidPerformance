package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor

class ThreadConverter : BaseConverter() {
    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)
        builder
            .transferConstructor(
            from = Method("java/lang/Thread", "<init>", "()V", false),
            to = Method("com/janbean/thread/PThread", "<init>", "(Ljava/lang/String;)V", false),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method("java/lang/Thread", "<init>", "(Ljava/lang/String;)V", false),
            to = Method("com/janbean/thread/PThread", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", false),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method("java/lang/Thread", "<init>", "(Ljava/lang/Runnable;)V", false),
            to = Method(
                "com/janbean/thread/PThread",
                "<init>",
                "(Ljava/lang/Runnable;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method("java/lang/Thread", "<init>", "(Ljava/lang/Runnable;Ljava/lang/String;)V", false),
            to = Method(
                "com/janbean/thread/PThread",
                "<init>",
                "(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/lang/Thread",
                "<init>",
                "(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PThread",
                "<init>",
                "(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            })
    }
}