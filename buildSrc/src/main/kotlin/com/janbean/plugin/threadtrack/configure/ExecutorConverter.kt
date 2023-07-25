package com.janbean.plugin.threadtrack.configure

import com.android.build.api.instrumentation.ClassContext
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.Opcodes

class ExecutorConverter: BaseConverter() {

    override fun build(classContext: ClassContext, builder: MethodReplaceVisitor.Builder) {
        super.build(classContext, builder)
        builder.transferMethod(
            Opcodes.INVOKESTATIC,
            from = Method(
                "java/util/concurrent/Executors",
                "defaultThreadFactory",
                "()Ljava/util/concurrent/ThreadFactory;"
            ),
            to = Method(
                "com/janbean/thread/Executors",
                "defaultThreadFactory",
                "(Ljava/lang/String;)Ljava/util/concurrent/ThreadFactory;"
            ),
            beforeReplace = { mv ->
                putClassNameToArg(classContext, mv)
            }
        ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newSingleThreadExecutor",
                    "()Ljava/util/concurrent/ExecutorService;"
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newSingleThreadExecutor",
                    "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;"
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newSingleThreadExecutor",
                    "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newSingleThreadExecutor",
                    "(Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newFixedThreadPool",
                    "(I)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newFixedThreadPool",
                    "(ILjava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newFixedThreadPool",
                    "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newFixedThreadPool",
                    "(ILjava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newCachedThreadPool",
                    "()Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newCachedThreadPool",
                    "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newCachedThreadPool",
                    "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newCachedThreadPool",
                    "(Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newSingleThreadScheduledExecutor",
                    "()Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newSingleThreadScheduledExecutor",
                    "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newSingleThreadScheduledExecutor",
                    "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newSingleThreadScheduledExecutor",
                    "(Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newScheduledThreadPool",
                    "(I)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newScheduledThreadPool",
                    "(ILjava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newScheduledThreadPool",
                    "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newScheduledThreadPool",
                    "(ILjava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newWorkStealingPool",
                    "()Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newWorkStealingPool",
                    "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            ).transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newWorkStealingPool",
                    "(I)Ljava/util/concurrent/ExecutorService;",
                ),
                to = Method(
                    "com/janbean/thread/Executors",
                    "newWorkStealingPool",
                    "(ILjava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                ),
                beforeReplace = { mv ->
                    putClassNameToArg(classContext, mv)
                }
            )
            .build()

    }
}