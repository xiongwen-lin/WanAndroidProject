package com.apemans.smartipcimpl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceManagerItemIpcNormalBinding

class IpcNormalItemView(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    private val binding = DeviceManagerItemIpcNormalBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    fun refreshView(device: IpcDevice) {
        device.device.deviceInfo.let {
            var deviceInfo = it as IpcDeviceInfo
            binding.tvDeviceManagerItemIpcNormalName.text = deviceInfo.name
        }
    }

    private fun init() {
    }
}