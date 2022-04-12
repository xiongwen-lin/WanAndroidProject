/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
@file:Suppress("unused", "underscores")

package com.apemans.dmapi.model

import com.apemans.dmapi.attrs.DeviceSupportAttrs
import com.apemans.dmapi.check.DeviceInfoCheck
import com.apemans.dmapi.info.*
import com.apemans.dmapi.schema.ApiSchema
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 08:18
 * 说明: 设备模型包括：
 * 1：设备基本信息
 * 2：设备支持属性
 * 3：设备协议描述
 *
 * 备注: 设备模型基础
 *
 ***********************************************************/
open class DeviceModel(val deviceInfo: BaseDeviceInfo) : Serializable {
    /*设备基本信息*/
    init {
        //安全检测
        DeviceInfoCheck.checkDeviceInfoSafely(deviceInfo)
    }

    /*设备支持属性*/
    var device_attrs: DeviceSupportAttrs? = null

    /*设备协议描述*/
    var protocol: ProtocolModel? = null

    /*Api指令集*/
    var apiSchema: ApiSchema? = null


}