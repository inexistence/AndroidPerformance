package com.janbean.plugin.util

import com.android.build.api.variant.Variant
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.result.ResolvedDependencyResult

object LibVersionChecker {
    fun checkDependienceLibVersion(
        variant: Variant,
        group: String,
        name: String,
        versions: Array<String>,
        onError: ((e: Exception) -> Unit)? = null
    ) {
        val configure = variant.compileConfiguration

        configure.incoming.afterResolve {
            this.resolutionResult.allDependencies {
                val module = (this as? ResolvedDependencyResult)?.selected?.moduleVersion
                if (module?.group == group && module?.name == name) {
                    if (!versions.contains(module.version)) {
                        val error = IllegalArgumentException("不确定是否支持该版本($group:$name:${module.version})，可以修改此处代码允许执行后检查`FileProvider`是否符合预期")
                        if (onError != null) {
                            onError.invoke(error)
                        } else {
                            throw error
                        }
                    } else {
                        Log.i("LibVersionChecker", "find $group:$name:${module.version}")
                    }
                }
            }
        }
    }
}