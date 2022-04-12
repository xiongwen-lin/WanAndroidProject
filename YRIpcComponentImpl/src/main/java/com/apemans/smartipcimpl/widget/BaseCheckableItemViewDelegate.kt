package com.apemans.smartipcimpl.widget

import androidx.recyclerview.widget.RecyclerView
import com.apemans.quickui.multitype.CheckType
import com.apemans.quickui.multitype.CheckableItemViewDelegate
import com.apemans.quickui.multitype.ICheckable

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/26 11:28 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
abstract class BaseCheckableItemViewDelegate<T: ICheckable, VH : RecyclerView.ViewHolder>(type: CheckType = CheckType.SINGLE, var listenerBlock: ((Int, T?, Any?) -> Unit)? = null) : CheckableItemViewDelegate<T, VH>(type) {

    fun handleAction(code: Int, data: T? = null, extra: Any? = null) {
        listenerBlock?.invoke(code, data, extra)
    }
}