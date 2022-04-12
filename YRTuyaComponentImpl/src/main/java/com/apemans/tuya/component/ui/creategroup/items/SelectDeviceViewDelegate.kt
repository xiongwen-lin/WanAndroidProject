package com.apemans.tuya.component.ui.creategroup.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import com.apemans.quickui.multitype.CheckType
import com.apemans.quickui.multitype.CheckableItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaItemSelectDeviceBinding

/**
 * @author Dylan Cai
 */
class SelectDeviceViewDelegate :
    CheckableItemViewDelegate<GroupDevice, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemSelectDeviceBinding>>(CheckType.MULTIPLE) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemSelectDeviceBinding>(parent)
            .onItemClick {
                checkItem(it)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemSelectDeviceBinding>,
        item: GroupDevice
    ) {
        with(holder.binding) {
            tvDeviceName.text = item.device.deviceBean.name
            ivDeviceIcon.load(item.device.deviceBean.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
            }
            checkbox.isChecked = item.isChecked
        }
    }
}