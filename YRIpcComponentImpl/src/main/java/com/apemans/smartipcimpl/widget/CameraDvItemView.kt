package com.apemans.smartipcimpl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceManagerItemIpcBinding

class CameraDvItemView(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    private val binding = DeviceManagerItemIpcBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    public fun refreshView(device: IpcDevice) {
        binding.tvDeviceManagerItemIpcTip1.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.text = resources.getString(R.string.home_video_history)
        binding.ivDeviceManagerItemIpcCloud.visibility = GONE
        device.device.deviceInfo.let {
            var deviceInfo = it as IpcDeviceInfo
            binding.tvDeviceManagerItemIpcName.text = deviceInfo.name
            val isOpen = deviceInfo.openStatus == DeviceDefine.SWITCH_ON
            binding.ivDeviceManagerItemIpcStatus.setImageResource(R.drawable.device_status_online)
            binding.ivDeviceManagerItemThumbnailCover.visibility = if (!isOpen) VISIBLE else GONE
            var actionIconOnRes = R.drawable.device_action_play_icon
            var actionIconOffRes = R.drawable.device_action_play_icon
            binding.sbtnDeviceManagerItemIpcSleep.toggleEnable = !isOpen
            if (!isOpen) {
                binding.tvDeviceManagerItemIpcTip1.visibility = VISIBLE
                binding.tvDeviceManagerItemIpcTip1.text = resources.getString(R.string.home_item_off_tip)
                actionIconOnRes = R.drawable.device_public_switch_on
                actionIconOffRes = R.drawable.device_public_switch_off
            }
            binding.sbtnDeviceManagerItemIpcSleep.initBtn(actionIconOnRes, actionIconOffRes)
            if (!isOpen && isOpen != binding.sbtnDeviceManagerItemIpcSleep.isOn) {
                binding.sbtnDeviceManagerItemIpcSleep.toggleNoCallback()
            }
        }
    }

    private fun init() {
    }
}