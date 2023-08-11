package com.janbean.plugin.fileprovider

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface FileProviderParams : InstrumentationParameters {
    @get:Input
    val versionValue: Property<String>
}