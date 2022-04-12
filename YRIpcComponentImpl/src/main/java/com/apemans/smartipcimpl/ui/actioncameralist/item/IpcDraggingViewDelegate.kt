package com.apemans.smartipcimpl.ui.actioncameralist.item

import android.content.Context
import android.view.ViewGroup
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceItemCameraDraggingPreviewBinding
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder

/**
 * @author Dylan Cai
 */
class IpcDraggingViewDelegate :
    ItemViewDelegate<IpcDevice, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraDraggingPreviewBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraDraggingPreviewBinding>(parent)

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraDraggingPreviewBinding>,
        item: IpcDevice
    ) {
        with(holder.binding) {
            civDeviceItemCameraDraggingPreview.refreshView(item)
        }
    }
}