package com.apemans.tuya.component.ui.group.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.quickui.multitype.getItem
import com.apemans.tuya.component.R
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.topActivity
import com.apemans.tuya.component.constants.isOpen
import com.apemans.tuya.component.databinding.TuyaItemGroupManageBinding
import com.tuya.smart.api.MicroContext
import com.tuya.smart.panelcaller.api.AbsPanelCallerService
import com.tuya.smart.sdk.bean.DeviceBean

/**
 * @author Dylan Cai
 */
class GroupViewDelegate(
    private val onOpen: (DeviceBean, Boolean) -> Unit
) : ItemViewDelegate<DeviceBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemGroupManageBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemGroupManageBinding>(parent)
            .onItemClick {
                val deviceBean: DeviceBean = getItem(it)
                val service = MicroContext.getServiceManager().findServiceByInterface<AbsPanelCallerService>(AbsPanelCallerService::class.java.name)
                service.goPanelWithCheckAndTip(topActivity, deviceBean.devId)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemGroupManageBinding>,
        item: DeviceBean
    ) {
        with(holder.binding) {
            tvDeviceName.text = item.name
            ivDeviceIcon.load(item.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
            }
            switchButton.isChecked = item.isOpen
            switchButton.setOnCheckedChangeListener { view, isChecked ->
                onOpen(item, isChecked)
            }
        }
    }
}