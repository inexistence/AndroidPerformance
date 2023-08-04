package com.janbean.plugin.firebase

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.janbean.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Firebase CloudMessagingReceiver 线程池优化,
 * 该Receiver是静态注册的，每收到一次消息就会新建一个Receiver对象，
 * 内部的线程池也是跟随对象创建的且存活60s，相当于每收到一个Receiver就会创建一个存活 60s 的线程
 */
class OptimizeFirebasePlugin : Plugin<Project> {
    companion object {
        const val TAG = "OptimizeFirebasePlugin"
    }

    override fun apply(project: Project) {
        Log.i(TAG, "apply")
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        project.dependencies.add("implementation", project.project(":optimize-firebase"))

        project.afterEvaluate {
            for (config in this.configurations) {
                val firebaseMessaging =
                    config.dependencies.firstOrNull { it.group == "com.google.firebase" && it.name == "firebase-messaging" }
                        ?: continue
                val firebaseVersion = firebaseMessaging.version
                Log.i(TAG, "find com.google.firebase.firebase-messaging:$firebaseVersion")
                if (firebaseVersion != "23.2.0") {
                    throw IllegalArgumentException("不确定是否支持该版本(com.google.firebase.firebase-messaging:${firebaseVersion})，可以修改此处代码允许执行后检查`com.google.android.gms.cloudmessaging.CloudMessagingReceiver#getBroadcastExecutor`是否符合预期")
                }
            }
        }

        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                FirebaseTransform::class.java,
                InstrumentationScope.ALL
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_CLASSES)
        }
    }
}