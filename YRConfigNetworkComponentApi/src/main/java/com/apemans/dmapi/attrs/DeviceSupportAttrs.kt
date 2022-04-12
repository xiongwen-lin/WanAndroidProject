/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.attrs

import com.apemans.dmapi.support.DevicePariTypeSupport
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 09:00
 * 说明: 设备属性支持
 *
 * 备注:
 *
 ***********************************************************/
class DeviceSupportAttrs : Serializable {
    /**
     * 设备配网支持，描述设备配网支持多少种类型
     */
    @DevicePariTypeSupport
    var pair_device_type_support: MutableList<Int> = mutableListOf()

    /**
     * 动态面板 UIType【 RN / Flutter / 原生 】
     */
    var dynamic_panel_support: Any? = null

    /**
     * 家庭管理支持
     */
    var home_support: Boolean = true

    /**
     * 房间管理支持
     */
    var room_support: Boolean = true

    /**
     * 群组支持
     */
    var group_support: Boolean = true

    /**
     * 自动化支持
     */
    var autoci_support: Boolean = true

    /**
     * 分享支持
     */
    var share_support: Boolean = true

    /**
     * 自动升级支持
     */
    var auto_upgrade_support: Boolean = true


    /**
     * Http 通信支持
     */
    var http_communication_support = false

    /**
     * MQTT 通信支持
     */
    var mqtt_communication_support = false

    /**
     * BLE 通信支持
     */
    var ble_communication_support = false

    /**
     * WLan 网线通信支持
     */
    var lan_communication_support = false

    /**
     * Sig mesh 通信支持
     */
    var sigmesh_communication_support = false

    /**
     * tuya mesh 通信支持
     */
    var tuyamesh_communication_support = false
}