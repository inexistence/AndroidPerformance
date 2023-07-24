package com.janbean.plugin.threadtrack

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class ThreadTrackPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        Log.i("ThreadTrackPlugin", "apply")
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        // TODO dependiences
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(ThreadTrackTransform::class.java, InstrumentationScope.ALL) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_CLASSES)
        }
    }
}
