package com.apemans.build

import com.apemans.build.BuildConfig.grade_version
import com.apemans.build.KotlinDependency.kotlin_version

/**
 * 项目级插件管理
 */
object PluginManagerDependency {
    const val AndroidGradeToolsPlugin = "com.android.tools.build:gradle:$grade_version"
    const val KotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    const val GoogleServicesGradle = "com.google.gms:google-services:4.3.10"
    const val FirebaseCrashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:2.7.1"

    const val GoogleServicesPlugin = "com.google.gms.google-services"
}