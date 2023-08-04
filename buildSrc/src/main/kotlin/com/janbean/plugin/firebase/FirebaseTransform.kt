package com.janbean.plugin.firebase

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.janbean.plugin.firebase.OptimizeFirebasePlugin.Companion.TAG
import com.janbean.plugin.util.Log
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

abstract class FirebaseTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className == "com.google.android.gms.cloudmessaging.CloudMessagingReceiver"
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return object : ClassVisitor(Opcodes.ASM5, nextClassVisitor) {
            override fun visitMethod(
                access: Int,
                name: String?,
                descriptor: String?,
                signature: String?,
                exceptions: Array<out String>?
            ): MethodVisitor {
                val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
                if (name == "getBroadcastExecutor" && descriptor == "()Ljava/util/concurrent/Executor;") {
                    return OptimizeFirebaseExecutorAdapter(methodVisitor, access, name, descriptor)
                }
                return methodVisitor
            }
        }
    }
}

class OptimizeFirebaseExecutorAdapter(
    methodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {
    override fun onMethodExit(opcode: Int) {
        visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "com/janbean/optimize/firebase/FirebaseExecutors",
            "getExecutorForCloudMessagingReceiver",
            "(Ljava/util/concurrent/Executor;)Ljava/util/concurrent/Executor;",
            false
        )
        super.onMethodExit(opcode)
    }
}