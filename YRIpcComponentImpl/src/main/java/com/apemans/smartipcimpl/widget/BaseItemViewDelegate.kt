package com.apemans.smartipcimpl.widget

import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/26 11:28 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
abstract class BaseItemViewDelegate<T, VH : RecyclerView.ViewHolder>(var listenerBlock: ((Int, T?, Any?) -> Unit)? = null) : ItemViewDelegate<T, VH>() {

    fun handleAction(code: Int, data: T? = null, extra: Any? = null) {
        listenerBlock?.invoke(code, data, extra)
    }
}