/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

import com.apemans.dmapi.support.*
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 09:51
 * 说明: 设备基础信息模型
 * @param model 产品型号
 * @param name 设备名称
 * @param main_category 设备分类类型
 * @param sub_category 子分类,例如：电工主分类_排插子分类_具体设备(WiFi排插/Ble蓝牙排插/Iot排插)
 * @param device_id 设备ID
 * @param platform 支持平台
 * @param icon_url 设备图标地址(占位图，icon，可以是url 可以是本地资源Resource)
 * @param brand 所属品牌
 * @param communication_type 支持协议如：WIFI 、BLE
 *
 * #### product_id 产品id  部分产品可能不会存在产品ID
 * #### device_id  设备ID 部分设备可能不会存在设备ID
 * #### device_uuid 设备唯一识别码 部分设备可能不会存在设备ID
 * #### firmware_version
 * #### chip_solution
 * #### active_time
 * #### ip_address
 * #### mac_address
 *
 * 备注:
 *
 ***********************************************************/
abstract class BaseDeviceInfo(
    @SupportModel
    val model: String,
    val name: String,
    @DeviceSupportMainCategory
    val main_category: Int,
    val sub_category: Int,
    val device_id: String,
    @SupportPlatform
    var platform: Int,
    val icon_url: Any,
    @SupportBrand
    val brand: String,
    @SupportCommunicationType
    val communication_type: Int,
) : Serializable {

    /*产品id  部分产品可能不会存在产品ID */
    var pid: String = ""

    /*Option:设备唯一识别码 部分设备可能不会存在设备ID*/
    var device_uuid = ""

    /*固件版本*/
    var firmware_version = ""

    /*<芯片>方案<商>*/
    @SupportChip
    var chip_solution: Int = -1

    /*Option:设备激活时间*/
    var active_time: Long = 0L

    /*设备IP地址*/
    var ip_address = ""

    /*设备mac地址*/
    var mac_address = ""
}