package com.apemans.tuyahome.component.ui.mixhome.items

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import coil.load
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.tuyahome.component.R
import com.apemans.tuyahome.component.bean.Device
import com.apemans.tuyahome.component.databinding.TuyahomeLayoutSmartNormalDeviceBinding
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.doOnClick
import com.dylanc.longan.dp
import com.dylanc.longan.getString

class DeviceViewDelegate : ItemViewDelegate<Device, BindingViewHolder<TuyahomeLayoutSmartNormalDeviceBinding>>() {
    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        BindingViewHolder<TuyahomeLayoutSmartNormalDeviceBinding>(parent)

    override fun onBindViewHolder(holder: BindingViewHolder<TuyahomeLayoutSmartNormalDeviceBinding>, item: Device) {
        holder.binding.apply {
            tvItemSmartNormalDeviceName.text = item.name
            ivIcon.load(item.iconUrl)
            btnItemSmartNormalDeviceSwitchButton.isVisible = item.showSwitch
            btnItemSmartNormalDeviceSwitchButton.isChecked = item.switchOn
            btnItemSmartNormalDeviceSwitchButton.setOnCheckedChangeListener { _, isChecked ->

            }

            if (item.category == "CAMERA") {
                ivIcon.updateLayoutParams {
                    width = 108.dp.toInt()
                    height = 80.dp.toInt()
                }
            } else {
                ivIcon.updateLayoutParams {
                    width = 60.dp.toInt()
                    height = 60.dp.toInt()
                }
            }

            tvState.text = if (item.online) "" else root.getString(R.string.offline)

            root.doOnClick {
                YRMiddleServiceManager.request(item.eventSchema?.get("click")?.url, mapOf("deviceId" to item.productId))
            }
        }
    }
}