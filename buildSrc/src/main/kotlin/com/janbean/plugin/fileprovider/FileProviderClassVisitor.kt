package com.janbean.plugin.fileprovider

import com.janbean.plugin.util.LibVersionChecker
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

class FileProviderClassVisitor(nextVisitor: ClassVisitor, val fileProviderVersion: Property<String>) :
    ClassVisitor(Opcodes.ASM5, nextVisitor) {
    companion object {
        val ATTACH_INFO = arrayOf(
            "attachInfo",
            "(Landroid/content/Context;Landroid/content/pm/ProviderInfo;)V"
        )
        val QUERY = arrayOf(
            "query",
            "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;"
        )
        val GET_TYPE = arrayOf(
            "getType",
            "(Landroid/net/Uri;)Ljava/lang/String;"
        )
        val DELETE = arrayOf(
            "delete",
            "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I"
        )
        val OPEN_FILE = arrayOf(
            "openFile",
            "(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;"
        )
        val NEED_CREATE_STRATEGY = arrayOf(QUERY, GET_TYPE, DELETE, OPEN_FILE)
    }

    override fun visitEnd() {
        // 生成成员变量authority
        val fv = this.cv.visitField(Opcodes.ACC_PRIVATE, "authority", "Ljava/lang/String;", null, "")
        fv?.visitEnd()
        super.visitEnd()
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val newMethodVisitor = if (name == ATTACH_INFO[0] && descriptor == ATTACH_INFO[1]) {
            AttachInfoMethodVisitor(methodVisitor, access, name, descriptor)
        } else if (NEED_CREATE_STRATEGY.firstOrNull { it[0] == name && it[1] == descriptor } != null) {
            CreateStrategyMethodVisitor(methodVisitor, access, name, descriptor, fileProviderVersion)
        } else {
            methodVisitor
        }
        return newMethodVisitor
    }


    class AttachInfoMethodVisitor(
        methodVisitor: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?
    ) : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {


        private val L0 = Label()
        private val L1 = Label()
        private val L2 = Label()
        private val L3 = Label()
        private val L_CHECK_0 = Label()
        private val L_CHECK_END = Label()

        override fun onMethodEnter() {
            visitLdcInsn("HookFileProvider")
            visitLdcInsn(name ?: "unknown method")
            visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)

            // 如果ProviderInfo.exported==true,抛异常
            visitVarInsn(ALOAD, 2)
            getField(Type.getType("Landroid/content/pm/ProviderInfo;"),"exported", Type.BOOLEAN_TYPE)
            visitJumpInsn(IFEQ, L_CHECK_0) // 如果没跳去L_CHECK_0，会继续往下走
            // throw SecurityException("Provider must not be exported")
            visitTypeInsn(NEW, "java/lang/SecurityException")
            visitInsn(DUP)
            visitLdcInsn("Provider must not be exported")
            visitMethodInsn(INVOKESPECIAL, "java/lang/SecurityException","<init>","(Ljava/lang/String;)V", false)
            visitInsn(ATHROW)

            visitLabel(L_CHECK_0)
            // 如果ProviderInfo.grantUriPermissions==false,抛异常
            visitVarInsn(ALOAD, 2)
            getField(Type.getType("Landroid/content/pm/ProviderInfo;"),"grantUriPermissions", Type.BOOLEAN_TYPE)
            visitJumpInsn(IFNE, L_CHECK_END) // 如果没跳去L_CHECK_END，会继续往下走
            // throw SecurityException("Provider must grant uri permissions")
            visitTypeInsn(NEW, "java/lang/SecurityException")
            visitInsn(DUP)
            visitLdcInsn("Provider must grant uri permissions")
            visitMethodInsn(INVOKESPECIAL, "java/lang/SecurityException","<init>","(Ljava/lang/String;)V", false)
            visitInsn(ATHROW)

            visitLabel(L_CHECK_END) // label走完还会继续往下走，并不是只走这一段label，label只是用来定义跳转的

            // 设置ProviderInfo.grantUriPermissions=false
            visitVarInsn(ALOAD, 2)
            push(0)
            putField(Type.getType("Landroid/content/pm/ProviderInfo;"),"grantUriPermissions", Type.BOOLEAN_TYPE)

            // try catch super.attach
            visitTryCatchBlock(L0, L1, L2, "java/lang/Throwable")
            visitLabel(L0)
            super.onMethodEnter()
        }

        // 访问局部变量和操作数栈
        // 方法结束前
        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            // try结束
            visitLabel(L1)
            visitJumpInsn(GOTO, L3)
            // catch开始
            visitLabel(L2)
            // Throwable.printStackTrace()
            visitFrame(F_NEW, 0, null, 1, arrayOf<Any>("java/lang/Throwable"))
            val local = newLocal(Type.DOUBLE_TYPE)
            visitVarInsn(ASTORE, local)
            visitVarInsn(ALOAD, local)
            visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Throwable",
                "printStackTrace",
                "()V",
                false
            )
            visitJumpInsn(GOTO, L3)
            visitLabel(L3)
            // 存储ProviderInfo.authority
            loadThis()
            visitVarInsn(ALOAD, 2)
            getField(Type.getType("Landroid/content/pm/ProviderInfo;"), "authority", Type.getType("Ljava/lang/String;"))
            visitLdcInsn(";")
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "split", "(Ljava/lang/String;)[Ljava/lang/String;", false)
            push(0)
            visitInsn(AALOAD)
            putField(Type.getType("Landroidx/core/content/FileProvider;"),"authority", Type.getType("Ljava/lang/String;"))

            // 设置ProviderInfo.grantUriPermissions = true
            visitVarInsn(ALOAD, 2)
            push(1)
            putField(Type.getType("Landroid/content/pm/ProviderInfo;"),"grantUriPermissions", Type.BOOLEAN_TYPE)
            visitInsn(RETURN)
            visitEnd()
//            super.visitMaxs(maxStack, maxLocals)
        }

        // 方法退出时，return前
        override fun onMethodExit(opcode: Int) {
            super.onMethodExit(opcode)
        }
    }

    class CreateStrategyMethodVisitor(
        val methodVisitor: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?,
        val fileProviderVersion: Property<String>,
    ) : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {
        private val L0 = Label()

        override fun onMethodEnter() {
            visitLdcInsn("HookFileProvider")
            visitLdcInsn(name ?: "unknown method")
            visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)

            loadThis()
            getField(Type.getType("Landroidx/core/content/FileProvider;"), "mStrategy", Type.getType("Landroidx/core/content/FileProvider${'$'}PathStrategy;"))
            visitJumpInsn(IFNONNULL, L0)
            // mStrategy为null，需要初始化
            loadThis()
            loadThis()
            visitMethodInsn(
                INVOKEVIRTUAL,
                "androidx/core/content/FileProvider",
                "getContext",
                "()Landroid/content/Context;",
                false
            )
            loadThis()
            getField(Type.getType("Landroidx/core/content/FileProvider;"), "authority", Type.getType("Ljava/lang/String;"))
            if (LibVersionChecker.compareVersion(fileProviderVersion.get(), "1.8.0") >= 0) {
                loadThis()
                getField(
                    Type.getType("Landroidx/core/content/FileProvider;"),
                    "mResourceId",
                    Type.INT_TYPE
                )
                visitMethodInsn(
                    INVOKESTATIC,
                    "androidx/core/content/FileProvider",
                    "getPathStrategy",
                    "(Landroid/content/Context;Ljava/lang/String;I)Landroidx/core/content/FileProvider${'$'}PathStrategy;",
                    false
                )
            } else {
                visitMethodInsn(
                    INVOKESTATIC,
                    "androidx/core/content/FileProvider",
                    "getPathStrategy",
                    "(Landroid/content/Context;Ljava/lang/String;)Landroidx/core/content/FileProvider${'$'}PathStrategy;",
                    false
                )
            }
            putField(Type.getType("Landroidx/core/content/FileProvider;"), "mStrategy", Type.getType("Landroidx/core/content/FileProvider${'$'}PathStrategy;"))

            visitLabel(L0)
            super.onMethodEnter()
        }

        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(maxStack, maxLocals)
        }

        override fun onMethodExit(opcode: Int) {
            super.onMethodExit(opcode)
        }
    }
}