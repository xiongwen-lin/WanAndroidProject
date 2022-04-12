/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 10:06
 * 说明: 设备协议模型
 *
 * 备注:
 *
 ***********************************************************/
class ProtocolModel {

    /**
     * 说明:网关支持协议版本 gateway_protocol_version
     * 如：涂鸦的DeviceBean[com.tuya.smart.sdk.bean.DeviceBean]中的pv网关协议版本
     * 可对应涂鸦pv参数
     */
    var gateway_pv = ""

    /**
     * 说明:网关通用固件版本。gateway_general_firmvare_version
     * 如：涂鸦的DeviceBean[com.tuya.smart.sdk.bean.DeviceBean]中的pv网关协议版本
     * 可对应涂鸦bv参数
     */
    var gateway_gfv = ""
}