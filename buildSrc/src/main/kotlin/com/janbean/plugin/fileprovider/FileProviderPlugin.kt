package com.janbean.plugin.fileprovider

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.util.LibVersionChecker
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project

class FileProviderPlugin : Plugin<Project> {
    companion object {
        private val supportVersion = arrayOf("1.6.*", "1.7.*", "1.8.*", "1.9.*")
        private const val TAG = "FileProviderPlugin"
    }

    override fun apply(project: Project) {
        Log.i(TAG, "apply")
        project.extensions.create("fileProviderConfig", FileProviderConfig::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val config = project.extensions.getByType(FileProviderConfig::class.java)
            val targetVersion = config.version
            if (targetVersion == null) {
                throw IllegalArgumentException(
                    "Cannot apply FileProviderPlugin, please setup `fileProviderConfig { version = \"you wanted\" }` on build.gradle"
                )
            }
            LibVersionChecker.checkDependienceLibVersion(
                variant,
                "androidx.core",
                "core",
                supportVersion,
                onFind = { group, name, version ->
                    if (!LibVersionChecker.versionEquals(targetVersion, version)) {
                        throw IllegalArgumentException("you want $group:$name with version $targetVersion but got $version")
                    }
                },
                onEnd = {
                    Log.d("FileProviderPlugin", "onEnd version check")
                }
            ) { e ->
                Log.i("FileProviderPlugin", e.message ?: "unknown error")
                throw e
            }
            variant.instrumentation.transformClassesWith(
                FileProviderTransform::class.java,
                InstrumentationScope.ALL
            ) {
                it.versionValue.set(targetVersion)
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}