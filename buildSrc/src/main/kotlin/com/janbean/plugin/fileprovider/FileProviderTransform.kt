package com.janbean.plugin.fileprovider

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class FileProviderTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    companion object {
        private const val FILE_PROVIDER_NAME = "androidx.core.content.FileProvider"
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className == FILE_PROVIDER_NAME
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return FileProviderClassVisitor(nextClassVisitor)
    }
}