/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.check

import com.apemans.dmapi.info.*
import com.apemans.dmapi.support.DeviceSupportMainCategory

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 10:50
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DeviceInfoCheck {
    fun checkDeviceInfoSafely(value:BaseDeviceInfo?){
        if (value == null) {
            return
        }
        when (value.main_category) {
            DeviceSupportMainCategory.light -> {
                if (value !is LightDeviceInfo) {
                    throw RuntimeException("Please set deviceInfo is LightDeviceInfo")
                }
            }
            DeviceSupportMainCategory.EA -> {
                if (value !is EADeviceInfo) {
                    throw RuntimeException("Please set deviceInfo is EADeviceInfo")
                }
            }
            DeviceSupportMainCategory.ipc -> {
                /*
                if (value !is IpcDeviceInfo && value !is BLEDeviceInfo && value !is GatewayDeviceInfo) {
                    throw RuntimeException("Please set deviceInfo is IpcDeviceInfo")
                }

                 */
            }
            DeviceSupportMainCategory.router -> {
                if (value !is RouterDeviceInfo) {
                    throw RuntimeException("Please set deviceInfo is RouterDeviceInfo")
                }
            }
        }
    }
}