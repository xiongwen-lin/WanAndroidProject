package com.apemans.quickui.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/17 11:07
 * 说明:
 * RecyclerView中Item间距设置
 * @param space sp 定义2个Item之间的距离
 * @param topAndBottom 第一个和最后一个是否需要padding
 *
 * 备注:
 *
 ***********************************************************/
class VerticalItemDecoration(private val space: Int, private val topAndBottom: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val totalCount = parent.adapter!!.itemCount
        if (position == 0) { //第一个
            if (topAndBottom) {
                outRect.top = space
            } else {
                outRect.top = 0
            }
            outRect.bottom = space / 2
        } else if (position == totalCount - 1) { //最后一个
            outRect.top = space / 2
            if (topAndBottom) {
                outRect.bottom = space
            } else {
                outRect.bottom = 0
            }
        } else { //中间其它的
            outRect.top = space / 2
            outRect.bottom = space / 2
        }
    }
}