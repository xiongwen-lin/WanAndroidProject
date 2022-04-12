/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.base.utils

import android.content.Context

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/17 10:59
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

/**
 * 根据名称获取Drawable对应资源名称ResID
 * etc:
 * //根据名字找匹配
 * val placeholderLoadingResId = "placeholder_loading".toDrawableResId(context)
 * val placeholderErrorResId: Int = "placeholder_loading".toDrawableResId(context)
 * @return ResID
 */
fun String.toDrawableResId(context: Context): Int {
    val applicationID = context.applicationInfo.processName
    return context.resources.getIdentifier(this, "drawable", applicationID)
}

/**
 * 根据名称获取String对应资源名称ResID
 * @return ResID
 */
fun String.toStringResId(context: Context): Int {
    val applicationID = context.applicationInfo.processName
    return context.resources.getIdentifier(this, "string", applicationID)
}