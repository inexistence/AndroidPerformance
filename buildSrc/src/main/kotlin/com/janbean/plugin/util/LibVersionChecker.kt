package com.janbean.plugin.util

import com.android.build.api.variant.Variant
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.result.ResolvedDependencyResult

object LibVersionChecker {
    const val TAG = "LibVersionChecker"

    fun versionEquals(version: String, checkVersion: String): Boolean {
        val versionA = version.split(".")
        val versionB = checkVersion.split(".")

        val compare0 = if (versionB[0] == "*" || versionA[0] == "*") { 0 } else {
            versionA[0].toInt() - versionB[0].toInt()
        }
        val compare1 = if (versionB[1] == "*" || versionA[1] == "*") { 0 } else {
            versionA[1].toInt() - versionB[1].toInt()
        }
        val compare2 = if (versionB[2] == "*" || versionA[2] == "*") { 0 } else {
            versionA[2].toInt() - versionB[2].toInt()
        }

        return compare0 == 0 && compare1 == 0 && compare2 == 0
    }

    fun checkDependienceLibVersion(
        variant: Variant,
        group: String,
        name: String,
        versions: Array<String>,
        onFind: ((group: String, name: String, version: String) -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onError: ((e: Exception) -> Unit)? = null,
    ) {
        val configure = variant.compileConfiguration

        configure.incoming.afterResolve {
            this.resolutionResult.allDependencies {
                val module = (this as? ResolvedDependencyResult)?.selected?.moduleVersion
                val foundVersion = module?.version
                if (module?.group == group && module.name == name && foundVersion != null) {
                    if (versions.firstOrNull { versionEquals(foundVersion, it) } == null) {
                        val error = IllegalArgumentException("不确定是否支持该版本($group:$name:${foundVersion})，可以修改此处代码允许执行后检查`FileProvider`是否符合预期")
                        if (onError != null) {
                            onError.invoke(error)
                        } else {
                            throw error
                        }
                    } else {
                        onFind?.invoke(group, name, foundVersion ?: "unknown")
                    }
                }
            }
            onEnd?.invoke()
        }
    }

    fun compareVersion(a: String, b: String): Int {
        Log.d(TAG, "compareVersion $a $b")
        val versionA = a.split(".")
        val versionB = b.split(".")

        val compare0 = versionA[0].toInt() - versionB[0].toInt()
        val compare1 = versionA[1].toInt() - versionB[1].toInt()
        val compare2 = versionA[2].toInt() - versionB[2].toInt()

        if (compare0 > 0) return 1
        if (compare0 < 0) return -1

        if (compare1 > 0) return 1
        if (compare1 < 0) return -1

        if (compare2 > 0) return 1
        if (compare2 < 0) return -1

        return 0
    }
}