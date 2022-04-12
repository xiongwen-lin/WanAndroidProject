package com.apemans.build

/***********************************************************
 * @Author : caro
 * @Date   : 2021/8/7
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
object KotlinDependency {
    private const val Coroutines = "1.5.2"
    const val kotlin_version = "1.5.32"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlin_version}"
    const val CoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Coroutines}"
    const val CoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Coroutines}"
}