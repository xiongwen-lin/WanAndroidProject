package com.apemans.base.utils

/**
 * @AUTHOR : By caro
 * @DATE : On 2020-09-23-10-25-10:25
 * @FUNCTION ：网络状态
 * @REMARK ：
 */
@Target(AnnotationTarget.TYPE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class NetType {
    companion object {
        // WIFI
        const val WIFI = "WIFI"
        // 手机网络
        const val NET = "NET"
        // 未识别网络
        const val NET_UNKNOWN = "NET_UNKNOWN"
        // 没有网络
        const val NONE = "NONE"
    }
}