/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/17 11:07
 * 说明:
 * RecyclerView中Item间距设置
 * @param padding sp
 * @param horizontal 是横向列表还是纵向列表
 * @param paddingFirst 第一个是否需要padding
 *
 * 备注:
 *
 ***********************************************************/
class ListItemDecoration(private val padding: Int, private val horizontal: Boolean = false, private val paddingFirst: Boolean = true) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        parent.adapter?.also {
            val pos = parent.getChildAdapterPosition(view)

            var l = padding / 2
            var r = padding / 2
            if (pos == 0) {
                l = padding
            } else if (pos == it.itemCount) {
                r = padding
            }
            if (horizontal) {
                if (pos == 0) {
                    if (paddingFirst) {
                        outRect.left = l
                    } else {
                        outRect.left = 0
                    }
                }

                outRect.right = r
            } else {
                if (pos == 0) {
                    if (paddingFirst) {
                        outRect.top = l
                    } else {
                        outRect.top = 0
                    }
                }
                outRect.bottom = r
            }
        }
    }
}