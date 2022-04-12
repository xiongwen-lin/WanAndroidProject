/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui.preference

import androidx.annotation.DrawableRes
import com.apemans.quickui.R

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/6 19:52
 * 说明: 全局配置
 * selector_rv_item_press_state.xml
 * 备注:
 *
 ***********************************************************/
object PreferenceGlobalConfig {

    @DrawableRes
    var itemBackground: Int? = null
        set(value) {
            if (field != null) {
                //throw RuntimeException("You have config Preference global itemBackground value")
            } else {
                field = value
            }
        }

    /**
     * 默认seekbar Preference item
     */
    var preferenceBarViewLayoutId = R.layout.layout_preference_bar

    /**
     * 默认normal Preference item
     */
    var preferenceViewViewLayoutId = R.layout.layout_preferenceview

    /**
     * 默认Category Preference item
     */
    var preferenceCategoryViewLayoutId = R.layout.layout_preference_category
}