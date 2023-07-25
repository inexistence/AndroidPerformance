package com.janbean.plugin.threadtrack

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.janbean.plugin.threadtrack.configure.ExecutorConverter
import com.janbean.plugin.threadtrack.configure.HandlerConverter
import com.janbean.plugin.threadtrack.configure.HandlerThreadConverter
import com.janbean.plugin.threadtrack.configure.ThreadConverter
import com.janbean.plugin.threadtrack.configure.ThreadPoolExecutorConverter
import com.janbean.plugin.threadtrack.configure.ScheduledThreadPoolConverter
import com.janbean.plugin.util.Log
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.util.concurrent.atomic.AtomicInteger

abstract class ThreadTrackTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    companion object {
        const val TAG = "ThreadTrackTransform"
        internal const val MARK = "\u200B"
        val map = HashMap<String, AtomicInteger>()
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
//        return classData.className == "com.janbean.androidperformance.test.ExecutorScheduler"
        return !classData.classAnnotations.contains("com.janbean.thread.KeepThread")
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        Log.d(TAG, "createClassVisitor")

        val builder = MethodReplaceVisitor
            .Builder()
            .api(Opcodes.ASM7)
            .nextClassVisitor(nextClassVisitor)

        HandlerConverter().build(classContext, builder)
        ExecutorConverter().build(classContext, builder)
        HandlerThreadConverter().build(classContext, builder)
        ThreadConverter().build(classContext, builder)
        ThreadPoolExecutorConverter().build(classContext, builder)
        ScheduledThreadPoolConverter().build(classContext, builder)

        return builder.build()
    }
}
