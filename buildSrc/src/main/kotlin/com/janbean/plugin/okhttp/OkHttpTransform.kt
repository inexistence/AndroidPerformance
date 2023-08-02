package com.janbean.plugin.okhttp

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.janbean.plugin.fileprovider.FileProviderClassVisitor
import com.janbean.plugin.fileprovider.FileProviderTransform
import com.janbean.plugin.util.Log
import com.janbean.plugin.util.replace.Method
import com.janbean.plugin.util.replace.MethodReplaceVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

abstract class OkHttpTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className == "okhttp3.OkHttpClient${'$'}Builder"
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
                if (name == "<init>" && descriptor == "()V") {
                    return OkHttpClientBuilderInitAdapter(methodVisitor, access, name, descriptor)
                }
                return methodVisitor
            }
        }
    }
}

class OkHttpClientBuilderInitAdapter(
    methodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {

    override fun onMethodExit(opcode: Int) {
        loadThis()
        visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "com/janbean/optimize/okhttp/GlobalOkHttpBuilder",
            "configBuilder",
            "(Lokhttp3/OkHttpClient${'$'}Builder;)V",
            false
        )
    }
}