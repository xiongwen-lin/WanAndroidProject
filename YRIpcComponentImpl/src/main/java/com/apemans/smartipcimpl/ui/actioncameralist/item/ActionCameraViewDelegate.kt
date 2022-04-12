package com.apemans.smartipcimpl.ui.actioncameralist.item

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.quickui.multitype.getItem
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceItemCameraPreviewBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class ActionCameraViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice>
) :
    ItemViewDelegate<IpcDevice, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraPreviewBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraPreviewBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemCameraPreviewBinding>,
        item: IpcDevice
    ) {
        with(holder.binding) {
            civDeviceItemCameraPreview.refreshView(item)
        }
    }
}