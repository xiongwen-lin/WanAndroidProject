package com.apemans.smartipcimpl.ui.ipcdevices.player.bean

/**
 * @Author:dongbeihu
 * @Description: 日期
 * @Date: 2021/12/2-14:38
 *
 */
data class DateTimeBean(
    val date: String,
    /**
     * -1是无数据，1是有数据
     */
    var dateStatus: Int
)