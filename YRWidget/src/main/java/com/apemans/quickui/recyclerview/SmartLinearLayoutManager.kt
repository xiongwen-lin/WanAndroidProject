/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui.recyclerview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/6 19:34
 * 说明: 横竖可滑动控制
 *
 * 备注:
 *
 ***********************************************************/
class SmartLinearLayoutManager  constructor(
    context: Context, orientation: Int, reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    private var canScrollVertical = true

    override fun canScrollVertically(): Boolean {
        return canScrollVertical
    }

    fun setCanScrollVertical(can:Boolean){
        canScrollVertical = can
    }

}