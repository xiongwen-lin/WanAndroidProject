package com.apemans.smartipcimpl.utils

import android.view.View

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 9:46 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

fun View.visibilityEnable(enable: Boolean) {
    visibility = if (enable) View.VISIBLE else View.GONE
}

fun View.visibilityEnable(block: () -> Boolean) {
    visibility = if (block.invoke()) View.VISIBLE else View.GONE
}