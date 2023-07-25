package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor

class ScheduledThreadPoolConverter: BaseConverter() {
    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)
        builder.transferConstructor(
            from = Method(
                "java/util/concurrent/ScheduledThreadPoolExecutor",
                "<init>",
                "(I)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/ThreadFactory;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/ThreadFactory;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/RejectedExecutionHandler;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/RejectedExecutionHandler;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PScheduledThreadPoolExecutor",
                "<init>",
                "(ILjava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        )
    }
}