package com.apemans.tuya.component.ui.homemanage.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.tuya.component.R
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.tuya.component.databinding.TuyaItemHomeOwnerBinding
import com.tuya.smart.sdk.bean.DeviceBean

/**
 * @author Dylan Cai
 */
class HomeDeviceViewDelegate :
    ItemViewDelegate<DeviceBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>(parent)

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>,
        item: DeviceBean
    ) {
        with(holder.binding) {
            tvName.text = item.name
            ivIcon.load(item.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
                transformations(CircleCropTransformation())
            }
        }
    }
}