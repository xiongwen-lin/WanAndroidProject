/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui

import android.content.Context
import android.util.TypedValue
import android.view.View

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/17 10:20
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

/**
 * 屏幕的密度
 */
private inline val Context.density: Float get() = resources.displayMetrics.density

/**
 * dp 转为 px
 */
private fun Context.dp2px(value: Int): Int = (density * value).toInt()

/**
 * dp 转为 px
 */
fun Context.dp2px(value: Float): Int = (density * value).toInt()

/**
 * Font
 *
 */
inline val Context.scaledDensity: Float get() = resources.displayMetrics.scaledDensity

fun Context.sp2px(value: Float): Int = (scaledDensity * value+ 0.5f).toInt()

fun View.dp(value: Int) = dp(value.toFloat()).toInt()

fun View.dp(value: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
)

fun View.sp(value: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, value.toFloat(), resources.displayMetrics
).toInt()

/**
 * 将px值转换为sp值，保证文字大小不变
 */
fun px2sp(context: Context, pxValue: Float): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return pxValue / fontScale
}