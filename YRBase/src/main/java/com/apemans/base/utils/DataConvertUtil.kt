package com.apemans.base.utils

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/18 5:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DataConvertUtil {

    fun toInt(value: String?): Int {
        try {
            return value?.toIntOrNull() ?: 0
        } catch (e: Exception) {
        }
        return 0
    }

    fun toLong(value: String?): Long {
        try {
            return value?.toLongOrNull() ?: 0
        } catch (e: Exception) {
        }
        return 0
    }

    fun toFloat(value: String?): Float {
        try {
            return value?.toFloatOrNull() ?: 0f
        } catch (e: Exception) {
        }
        return 0f
    }

    fun toDouble(value: String?): Double {
        try {
            return value?.toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
        }
        return 0.0
    }
}