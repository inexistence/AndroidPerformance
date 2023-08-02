package com.janbean.plugin.okhttp

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.fileprovider.FileProviderTransform
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project

class OptimizeOkHttpPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Log.i("OptimizeOkHttpPlugin", "apply")
        project.dependencies.add("implementation", project.project(":optimize-okhttp"))
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            Log.d("OptimizeOkHttpPlugin", "onVariants $variant")
            variant.instrumentation.transformClassesWith(OkHttpTransform::class.java, InstrumentationScope.ALL) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}