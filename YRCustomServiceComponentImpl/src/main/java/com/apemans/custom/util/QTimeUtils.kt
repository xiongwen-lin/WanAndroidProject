package com.apemans.custom.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author
 * @date 2021/2/20 15:52
 * @description 时间工具类
 * @connected 526416248@qq.com
 */
 object  QTimeUtils {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val date = Date(System.currentTimeMillis())

    fun getToday():String{
        return simpleDateFormat.format(date)
    }

    /**
     *
     */
     fun dateToTimestamp(time: String?): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return try {
            val date: Date = simpleDateFormat.parse(time)
            date.time / 1000
        } catch (e: ParseException) {
            0
        }
    }
    /**
     * 获取前一天的时间
     */
     fun getLastDay(): String {
        var mDate = date
        val calendar = Calendar.getInstance()
        calendar.time = mDate
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        mDate = calendar.time
        return simpleDateFormat.format(mDate)

    }
}