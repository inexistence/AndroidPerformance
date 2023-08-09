package com.janbean.plugin.fileprovider

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentSelector
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.kotlin.dsl.configure
import com.janbean.plugin.util.LibVersionChecker

class FileProviderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Log.i("FileProviderPlugin", "apply")
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            LibVersionChecker.checkDependienceLibVersion(
                variant,
                "androidx.core",
                "core",
                arrayOf("1.9.0")
            ) { e ->
                Log.i("FileProviderPlugin", e.message ?: "unknown error")
                throw e
            }
            variant.instrumentation.transformClassesWith(FileProviderTransform::class.java, InstrumentationScope.ALL) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}