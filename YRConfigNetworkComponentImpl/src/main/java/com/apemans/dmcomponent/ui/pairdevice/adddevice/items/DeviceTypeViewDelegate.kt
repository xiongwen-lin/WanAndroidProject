package com.apemans.dmcomponent.ui.pairdevice.adddevice.items

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.quickui.multitype.getItem
import com.apemans.dmcomponent.databinding.DeviceItemDeviceTypeBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
data class DeviceType(@DrawableRes val icon: Int, val name: String)

class DeviceTypeViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceType>
) : ItemViewDelegate<DeviceType, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTypeBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTypeBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTypeBinding>,
        item: DeviceType
    ) {
        with(holder.binding) {
            tvName.text = item.name
//            ivIcon.isVisible = item.icon == 0
//            if (item.icon > 0) {
//                ivIcon.load(item.icon)
//            }
        }
    }
}