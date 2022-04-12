package com.apemans.smartipcimpl.ui.ipcdevices.player.bean

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/2-14:38 播放区域支持功能
 *
 */
data class FunctionMenuItem (
    /**
     * 竖屏图标
     */
    @DrawableRes val icon: Int,

    /**
     * 横屏图标(切横屏时，警报和方位无图标)
     */
    @DrawableRes val iconLands: Int,
    @StringRes val text: Int,

    /**
     * 功能类型
     */
    val functionType: Int,
    )