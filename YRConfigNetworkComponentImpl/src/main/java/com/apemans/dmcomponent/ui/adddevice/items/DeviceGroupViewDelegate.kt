package com.apemans.dmcomponent.ui.adddevice.items

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.dmcomponent.databinding.DeviceItemDeviceGroupBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder

/**
 * @author Dylan Cai
 */
class DeviceGroupViewDelegate : ItemViewDelegate<DeviceGroup, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceGroupBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceGroupBinding>(parent)

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceGroupBinding>,
        item: DeviceGroup
    ) {
        with(holder.binding) {
            tvGroupName.text = item.groupingTitle
        }
    }
}