package com.apemans.quickui.superdialog

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/9 15:13
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
/**
 * 状态栏高度
 */
val Context?.statusBarHeight: Int
    get() {
        this ?: return 0
        var result = 24
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

fun Activity.isDarkMode(): Boolean {
    val value = window.decorView.getSystemUiVisibility()
    Log.i("StatusBar","isDarkMode value == $value")
    return value == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}
