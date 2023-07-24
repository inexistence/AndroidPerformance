package com.janbean.plugin.util.replace

import com.janbean.plugin.util.Log
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class MethodReplaceVisitor(
    api: Int,
    classVisitor: ClassVisitor?,
    val transferMethodList: List<TransferMethod>,
    val transferTypeList: List<TransferType>,
) : ClassVisitor(api, classVisitor) {
    companion object {
        const val TAG = "MethodReplaceVisitor"
    }

    class Builder {
        private var api: Int = Opcodes.ASM7
        private var classVisitor: ClassVisitor? = null
        private val transferMethodList = ArrayList<TransferMethod>()
        private val transferTypeList = ArrayList<TransferType>()

        fun api(api: Int): Builder {
            this.api = api
            return this
        }

        fun nextClassVisitor(classVisitor: ClassVisitor?): Builder {
            this.classVisitor = classVisitor
            return this
        }

        /**
         * auto transfer NEW
         */
        fun transferConstructor(
            from: Method,
            to: Method,
            beforeReplace: ((mv: MethodVisitor) -> Unit)? = null,
            afterReplace: ((mv: MethodVisitor) -> Unit)? = null
        ): Builder {
            if (from.name != "<init>" || to.name != "<init>") {
                throw IllegalArgumentException("should be init function")
            }
            transferType(Opcodes.NEW, from.owner, to.owner)
            transferMethod(Opcodes.INVOKESPECIAL, from, to, beforeReplace, afterReplace)
            return this
        }

        fun transferMethod(
            opcode: Int,
            from: Method,
            to: Method,
            beforeReplace: ((mv: MethodVisitor) -> Unit)? = null,
            afterReplace: ((mv: MethodVisitor) -> Unit)? = null
        ): Builder {
            transferMethodList.add(TransferMethod(opcode, from, to, beforeReplace, afterReplace))
            return this
        }

        fun transferType(opcode: Int, type: String, to: String): Builder {
            transferTypeList.add(TransferType(opcode, type, to))
            return this
        }

        fun transferMethod(transferMethod: TransferMethod): Builder {
            transferMethodList.add(transferMethod)
            return this
        }

        fun build(): MethodReplaceVisitor {
            return MethodReplaceVisitor(
                this.api,
                this.classVisitor,
                transferMethodList,
                transferTypeList
            )
        }
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        Log.i(TAG, "visitField $access $name $descriptor $signature $value")
        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        Log.i(TAG, "visitMethod $access $name $descriptor $signature $exceptions")
        return MethodReplaceAdapter(
            api,
            transferMethodList,
            transferTypeList,
            super.visitMethod(access, name, descriptor, signature, exceptions),
            access, name, descriptor
        )
    }

    class MethodReplaceAdapter(
        api: Int,
        private val transferMethodList: List<TransferMethod>,
        private val transferTypeList: List<TransferType>,
        mv: MethodVisitor,
        access: Int, name: String?, descriptor: String?
    ) : AdviceAdapter(api, mv, access, name, descriptor) {

        override fun visitTypeInsn(opcode: Int, type: String?) {
            Log.d(TAG, "visitTypeInsn $opcode $type")
            val transferType = transferTypeList.find { it.opcode == opcode && it.from == type }
            if (transferType?.to != null) {
                Log.d(TAG, "visitTypeInsn $opcode $type to ${transferType.to}")
                super.visitTypeInsn(opcode, transferType.to)
            } else {
                super.visitTypeInsn(opcode, type)
            }
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            Log.d(TAG, "visitMethodInsn $opcode $owner $name $descriptor $isInterface")
            val transferMethod = transferMethodList.find {
                val targetOpcode = it.opcode
                val fromMethod = it.from
                targetOpcode == opcode && owner == fromMethod.owner && fromMethod.descriptor == descriptor
            }
            val to = transferMethod?.to

            if (to != null) {
                Log.d(TAG, "visitMethodInsn to $to")
                transferMethod.beforeReplace?.invoke(this)
                super.visitMethodInsn(
                    opcode,
                    to.owner,
                    to.name,
                    to.descriptor,
                    to.isInterface ?: (opcode == Opcodes.INVOKEINTERFACE)
                )
                transferMethod.afterReplace?.invoke(this)
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }
    }
}