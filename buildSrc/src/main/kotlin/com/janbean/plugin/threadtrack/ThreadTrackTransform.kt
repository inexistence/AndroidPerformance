package com.janbean.plugin.threadtrack

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.janbean.plugin.util.Log
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

abstract class ThreadTrackTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    companion object {
        const val TAG = "ThreadTrackTransform"
        internal const val MARK = "\u200B"
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
//        return classData.className == "com.janbean.androidperformance.Test2"
        return !classData.classAnnotations.contains("com.janbean.thread.KeepThread")
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        Log.i(TAG, "createClassVisitor")
        return MethodReplaceVisitor
            .Builder()
            .api(Opcodes.ASM7)
            .nextClassVisitor(nextClassVisitor)
            .transferConstructor(
                from = Method("java/lang/Thread", "<init>", "()V", false),
                to = Method("com/janbean/thread/PThread", "<init>", "()V", false),
                beforeReplace = { mv ->
                    mv.visitLdcInsn(MARK + classContext.currentClassData.className)
                }
            )
            .transferConstructor(
                from = Method("java/lang/Thread", "<init>", "(Ljava/lang/Runnable;)V", false),
                to = Method(
                    "com/janbean/thread/PThread",
                    "<init>",
                    "(Ljava/lang/Runnable;Ljava/lang/String;)V",
                    false
                ),
                beforeReplace = { mv ->
                    mv.visitLdcInsn(MARK + classContext.currentClassData.className)
                }
            )
            .transferConstructor(
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
                    mv.visitLdcInsn(MARK + classContext.currentClassData.className)
                })
            .transferConstructor(
                from = Method(
                    "android/os/HandlerThread",
                    "<init>",
                    "(Ljava/lang/String;)V",
                    false
                ),
                to = Method(
                    "com/janbean/thread/PHandlerThread",
                    "<init>",
                    "(Ljava/lang/String;)V",
                    false
                )
            )
            .transferConstructor(
                from = Method(
                    "android/os/HandlerThread",
                    "<init>",
                    "(Ljava/lang/String;I)V",
                    false
                ),
                to = Method(
                    "com/janbean/thread/PHandlerThread",
                    "<init>",
                    "(Ljava/lang/String;I)V",
                    false
                )
            )
            .transferConstructor(
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
                    mv.visitLdcInsn(MARK + classContext.currentClassData.className)
                }
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "java/util/concurrent/Executors",
                    "newSingleThreadExecutor",
                    "()Ljava/util/concurrent/ExecutorService;",
                    false
                ),
                to = Method(
                    "com/janbean/thread/util/Executors",
                    "newSingleThreadExecutor",
                    "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;",
                    false
                ),
                beforeReplace = { mv ->
                    mv.visitLdcInsn(MARK + classContext.currentClassData.className)
                }
            )
            .build()
    }
}