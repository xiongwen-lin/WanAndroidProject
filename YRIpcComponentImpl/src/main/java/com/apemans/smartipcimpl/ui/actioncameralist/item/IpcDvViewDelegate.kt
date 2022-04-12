package com.apemans.smartipcimpl.ui.actioncameralist.item

import android.content.Context
import android.view.ViewGroup
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceItemIpcDvBinding
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.quickui.multitype.getItem

/**
 * @author Dylan Cai
 */
class IpcDvViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice>
) :
    ItemViewDelegate<IpcDevice, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDvBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDvBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDvBinding>,
        item: IpcDevice
    ) {
        with(holder.binding) {
            civDeviceItemIpcDv.refreshView(item)
        }
    }
}