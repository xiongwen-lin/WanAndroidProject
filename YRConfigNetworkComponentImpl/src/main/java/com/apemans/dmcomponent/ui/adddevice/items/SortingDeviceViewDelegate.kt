package com.apemans.dmcomponent.ui.adddevice.items

import android.content.Context
import android.view.ViewGroup
import coil.loadAny
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.quickui.multitype.getItem
import com.apemans.dmapi.model.DeviceModel
import com.apemans.dmcomponent.databinding.DeviceItemTypeDeviceBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class SortingDeviceViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceModel>
) : ItemViewDelegate<DeviceModel, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemTypeDeviceBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemTypeDeviceBinding>(parent)
            .onItemClick(listener) { getItem(it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemTypeDeviceBinding>,
        item: DeviceModel
    ) {
        with(holder.binding) {
            ivDevice.loadAny(item.deviceInfo.icon_url)
            tvDevice.text = item.deviceInfo.name
        }
    }
}