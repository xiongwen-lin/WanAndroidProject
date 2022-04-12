package com.apemans.smartipcimpl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apemans.smartipcapi.info.GatewayDeviceInfo
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceManagerItemIpcDraggingBinding

class CameraDraggingItemView(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    private val binding = DeviceManagerItemIpcDraggingBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    fun refreshView(device : IpcDevice) {
        device.device.deviceInfo.let {
            val deviceInfo = it as GatewayDeviceInfo
            binding.tvDeviceManagerItemIpcDraggingName.text = deviceInfo.name
        }
    }

    private fun init() {
    }
}