package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor

class ThreadPoolExecutorConverter : BaseConverter() {
    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)

        builder.transferConstructor(
            from = Method(
                "java/util/concurrent/ThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;)V",
                false
            ),
            to = Method(
                "com/janbean/thread/PThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/lang/String;)V",
                false
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V"
            ),
            to = Method(
                "com/janbean/thread/PThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)V"
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/RejectedExecutionHandler;)V"
            ),
            to = Method(
                "com/janbean/thread/PThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/RejectedExecutionHandler;Ljava/lang/String;)V"
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferConstructor(
            from = Method(
                "java/util/concurrent/ThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V"
            ),
            to = Method(
                "com/janbean/thread/PThreadPoolExecutor",
                "<init>",
                "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;Ljava/lang/String;)V"
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        )
    }
}