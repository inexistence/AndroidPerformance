package com.janbean.plugin.crashfix.dispatcher

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

abstract class FixDispatcherCrashTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return !classData.className.equals("com.janbean.crashfix.AppDispatchers")
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return MethodReplaceVisitor
            .Builder()
            .api(Opcodes.ASM7)
            .nextClassVisitor(nextClassVisitor)
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "kotlinx/coroutines/Dispatchers",
                    "getMain",
                    "()Lkotlinx/coroutines/MainCoroutineDispatcher;",
                ),
                to = Method(
                    "com/janbean/crashfix/AppDispatchers",
                    "getMain",
                    "()Lkotlinx/coroutines/MainCoroutineDispatcher;",
                ),
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "kotlinx/coroutines/Dispatchers",
                    "getIO",
                    "()Lkotlinx/coroutines/CoroutineDispatcher;",
                ),
                to = Method(
                    "com/janbean/crashfix/AppDispatchers",
                    "getIO",
                    "()Lkotlinx/coroutines/CoroutineDispatcher;",
                ),
            )
            .transferMethod(
                Opcodes.INVOKESTATIC,
                from = Method(
                    "kotlinx/coroutines/Dispatchers",
                    "getDefault",
                    "()Lkotlinx/coroutines/CoroutineDispatcher;",
                ),
                to = Method(
                    "com/janbean/crashfix/AppDispatchers",
                    "getDefault",
                    "()Lkotlinx/coroutines/CoroutineDispatcher;",
                ),
            )
            .build()
    }
}