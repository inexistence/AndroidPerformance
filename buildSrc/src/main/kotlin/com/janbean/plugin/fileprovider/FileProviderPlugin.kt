package com.janbean.plugin.fileprovider

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project

class FileProviderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Log.i("FileProviderPlugin", "apply")
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            Log.i("FileProviderPlugin", "onVariants $variant")
            variant.instrumentation.transformClassesWith(FileProviderTransform::class.java, InstrumentationScope.ALL) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}