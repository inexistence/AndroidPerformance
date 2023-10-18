package com.janbean.plugin.crashfix

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.crashfix.dispatcher.FixDispatcherCrashTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrashFixPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.dependencies.add("implementation", project.project(":crashfix"))
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                FixDispatcherCrashTransform::class.java,
                InstrumentationScope.ALL
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}