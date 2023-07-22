package com.janbean.plugin.threadtrack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters

abstract class ThreadTrackPlugin: Plugin<Project>, TransformAction<TransformParameters.None> {
    override fun apply(project: Project) {
        println("Start Thread Track")
    }

    override fun transform(outputs: TransformOutputs) {

    }

}
