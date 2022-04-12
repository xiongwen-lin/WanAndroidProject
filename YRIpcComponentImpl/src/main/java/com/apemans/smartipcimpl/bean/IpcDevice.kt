package com.apemans.smartipcimpl.bean

import com.apemans.quickui.multitype.ICheckable
import com.apemans.dmapi.model.DeviceModel
import com.apemans.smartipcapi.webapi.PackageInfoResult

data class IpcDevice(
    override var isChecked: Boolean = false,
    var itemType : Int = ITEM_TYPE_IPC_PREVIEW,
    var device : DeviceModel
) : ICheckable {

    var packInfo : PackageInfoResult? = null
    var deviceType : Int = 0

    companion object {
        const val ITEM_TYPE_IPC_PREVIEW = 1
        const val ITEM_TYPE_IPC_DRAGGING = 2

        const val DEVICE_TYPE_IPC = 1
        const val DEVICE_TYPE_BLE_IPC = 2
        const val DEVICE_TYPE_GATEWAY = 3

        fun convertDeviceType(model : String) : Int {
            return when(model) {
                "EC810-HUB", "W1-HUB" -> DEVICE_TYPE_GATEWAY
                else -> DEVICE_TYPE_IPC
            }
        }
    }

}
