/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.smartipcimpl.utils

import com.apemans.dmapi.status.DeviceDefine
import com.apemans.dmapi.support.SupportCommunicationType
import com.apemans.dmapi.support.ipc.*
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcimpl.bean.DeviceInfoModel
import com.apemans.smartipcimpl.constants.*

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/16 11:23 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DeviceManagerHelper {

    fun convertIpcSubCategory(model : String) : Int {
        return when(model) {
            IpcIndoorCardCameraSupportModel.C1, IpcIndoorCardCameraSupportModel.A1 -> IpcSupportCategory.indoor_card_camera_type
            IpcIndoorCameraPANSupportModel.P1, IpcIndoorCameraPANSupportModel.P2 -> IpcSupportCategory.indoor_pan_camera_type
            IpcIndoorMiniCameraSupportModel.M1 -> IpcSupportCategory.indoor_mini_camera_type
            IpcBatteryCameraSupportModel.W1 -> IpcSupportCategory.camera_battery_type
            IpcHubSupportModel.W1_HUB -> IpcSupportCategory.ipc_hub_type
            else -> IpcSupportCategory.indoor_card_camera_type
        }
    }

    fun convertIpcCommunicationType(model : String) : Int {
        return when(model) {
            IpcBatteryCameraSupportModel.HC320 -> SupportCommunicationType.WIFI_BLE
            else -> SupportCommunicationType.WIFI
        }
    }

//    fun convertDeviceInfoAsMap(device: IpcDeviceInfo) : Map<String, String> {
//        val deviceInfoMap = mutableMapOf<String, String>()
//        deviceInfoMap[KEY_DEVICE_ID] = device.device_id
//        deviceInfoMap[KEY_DEVICE_MODEL] = device.model
//        deviceInfoMap[KEY_DEVICE_NAME] = device.name
//        return deviceInfoMap
//    }

    fun convertDeviceInfoAsMap(device: IpcDeviceInfo) : DeviceInfoModel {
        return DeviceInfoModel().apply {
            deviceId = device.device_id
            model = device.model
            name = device.name
            bindType = if (device.bindType == DeviceDefine.BIND_TYPE_OWNER) DEVICE_BIND_TYPE_OWNER else DEVICE_BIND_TYPE_SHARER
        }
    }

}