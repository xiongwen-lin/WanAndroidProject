package com.apemans.yruibusiness.utils

import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/18 10:13
 * 说明:
 * Loading
 *
 * 备注:
 *
 ***********************************************************/
interface LoadingEvent : Serializable {

    /**
     * 显示Loading
     */
    fun showLoading(message: String = "")

    /**
     * @param delay 单位秒
     */
    fun showMessage(message: String = "", delay: Int = 2)

    /**
     * 关闭Loading
     */
    fun dismissLoading(delay: Int = 0)
}