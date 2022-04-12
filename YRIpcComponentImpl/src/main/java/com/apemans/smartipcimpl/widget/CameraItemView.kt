package com.apemans.smartipcimpl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apemans.smartipcapi.info.BLEDeviceInfo
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceManagerItemIpcBinding

class CameraItemView(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    private val binding = DeviceManagerItemIpcBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    fun refreshView(device : IpcDevice) {
        device.device.let {
            when (device.deviceType) {
                IpcDevice.DEVICE_TYPE_IPC -> refreshIpcView(device)
                IpcDevice.DEVICE_TYPE_BLE_IPC -> refreshBleIpcView(device)
            }
        }
    }

    private fun refreshIpcView(device: IpcDevice) {
        binding.tvDeviceManagerItemIpcTip1.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.text = resources.getString(R.string.home_video_history)
        device.device.deviceInfo.let {
            var deviceInfo = it as IpcDeviceInfo
            binding.tvDeviceManagerItemIpcName.text = deviceInfo.name
            val isOnline = deviceInfo.online == DeviceDefine.ONLINE
            val isOpen = deviceInfo.openStatus == DeviceDefine.SWITCH_ON
            val statusRes = if (isOnline) R.drawable.device_status_online else R.drawable.device_status_offline
            binding.ivDeviceManagerItemIpcStatus.setImageResource(statusRes)
            binding.ivDeviceManagerItemThumbnailCover.visibility = if (!isOnline || !isOpen) VISIBLE else GONE
            var actionIconOnRes = R.drawable.device_action_play_icon
            var actionIconOffRes = R.drawable.device_action_play_icon
            binding.sbtnDeviceManagerItemIpcSleep.toggleEnable = isOnline && !isOpen
            if (isOnline) {
                if (!isOpen) {
                    binding.tvDeviceManagerItemIpcTip1.visibility = VISIBLE
                    binding.tvDeviceManagerItemIpcTip1.text = resources.getString(R.string.home_item_off_tip)
                    actionIconOnRes = R.drawable.device_public_switch_on
                    actionIconOffRes = R.drawable.device_public_switch_off
                }
            } else {
                binding.tvDeviceManagerItemIpcTip1.visibility = VISIBLE
                binding.tvDeviceManagerItemIpcTip1.text = resources.getString(R.string.home_item_offline_tip)
                actionIconOnRes = R.drawable.device_action_offline_icon
                actionIconOffRes = R.drawable.device_action_offline_icon
            }
            binding.sbtnDeviceManagerItemIpcSleep.initBtn(actionIconOnRes, actionIconOffRes)
            if (isOnline && !isOpen && isOpen != binding.sbtnDeviceManagerItemIpcSleep.isOn) {
                binding.sbtnDeviceManagerItemIpcSleep.toggleNoCallback()
            }
            val isCloudValid = device.packInfo?.exist_cloud == 1
            binding.tvDeviceManagerItemIpcTip2.visibility = if (isCloudValid && !isOnline) VISIBLE else GONE
            val cloudRes = if (isCloudValid) R.drawable.device_cloud_state_valid_icon else R.drawable.device_cloud_state_invalid_icon
            binding.ivDeviceManagerItemIpcCloud.setImageResource(cloudRes)
        }
    }

    private fun refreshBleIpcView(device: IpcDevice) {
        binding.ivDeviceManagerItemThumbnailCover.visibility = VISIBLE
        binding.ivDeviceManagerItemIpcStatus.setImageResource(R.drawable.device_status_offline)
        binding.sbtnDeviceManagerItemIpcSleep.toggleEnable = false
        binding.sbtnDeviceManagerItemIpcSleep.initBtn(R.drawable.device_action_offline_icon, R.drawable.device_action_offline_icon)
        binding.tvDeviceManagerItemIpcTip1.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.visibility = VISIBLE
        binding.ivDeviceManagerItemIpcCloud.visibility = GONE
        binding.tvDeviceManagerItemIpcTip2.text = resources.getString(R.string.home_item_ble_ap_access)
        device.device.deviceInfo.let {
            var deviceInfo = it as BLEDeviceInfo
            binding.tvDeviceManagerItemIpcName.text = deviceInfo.name
        }
    }

    private fun init() {
    }
}