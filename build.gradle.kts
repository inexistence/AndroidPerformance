// Top-level build file where you can add configuration options common to all sub-projects/modules.
//@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
//plugins {
//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.kotlinAndroid) apply false
//    alias(libs.plugins.androidLibrary) apply false
////    alias(libs.plugins.threadTrack) apply false
//}
//true // Needed to make the Suppress annotation work for the plugins block

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        // 需要同步修改buildSrc中的版本号?
//        classpath(libs.gradle)
//        classpath(libs.asm.util)
//        classpath(libs.asm.common)
        classpath(kotlin("gradle-plugin", version = "1.7.0"))
    }
}