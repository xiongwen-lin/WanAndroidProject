package com.apemans.smartipcimpl.utils

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/10 11:42 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DateTimeToolUtil {

    const val HOUR_NUM_OF_DAY = 24
    const val MIN_NUM_OF_HOUR = 60
    const val SECOND_NUM_OF_MIN = 60
    const val DAY_NUM_OF_WEEK = 7

    fun convertFormatTimeByMin(time: Int) : String {
        val hour = time / 60
        val min = time % 60
        return convertFormatHourOrMin(hour).plus(":").plus(convertFormatHourOrMin(min))
    }

    private fun convertFormatHourOrMin(length: Int) : String {
        if (length < 0) {
            return "0$length"
        }
        return if (length < 10) "0$length" else length.toString()
    }
}