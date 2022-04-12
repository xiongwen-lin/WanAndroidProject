package com.apemans.dmcomponent.ui.pairdevice.adddevice.items

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.dmcomponent.databinding.DeviceItemAddDeviceTitleBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder

/**
 * @author Dylan Cai
 */
class TitleViewDelegate :
    ItemViewDelegate<String, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemAddDeviceTitleBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemAddDeviceTitleBinding>(parent)

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemAddDeviceTitleBinding>,
        item: String
    ) {
        with(holder.binding) {
            tvTitle.text = item
        }
    }
}