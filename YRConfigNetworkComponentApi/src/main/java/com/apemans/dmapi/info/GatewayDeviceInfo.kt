/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 09:54
 * 说明: 网关类设备类信息
 *
 * 备注:
 *
 ***********************************************************/
class GatewayDeviceInfo(
    model: String,
    name: String,
    category: Int,
    sub_category: Int,
    device_id: String,
    platform: Int,
    icon_url: Any,
    brand: String,
    communication_type:Int
) : BaseDeviceInfo(model, name, category, sub_category,device_id, platform, icon_url, brand,communication_type),
    Serializable {
    /*网关类型*/
    var gwType: String? = null

    var sn : String = ""
    var mac : String = ""
    var version : String = ""
    var local_ip : Int = 0
    var wanip : Int = 0
    var type : String = ""
    var open_status : Int = 0
    var online : Int = 0
    var time : Long = 0
    var hb_domain : String = ""
    var hb_server : Int = 0
    var hb_port : Int = 0
    var zone : Float = 0f
    var region : String = ""
    var secret : String = ""
    var mqtt_online : Int = 0
    var sort : Int = 0
    var child : MutableList<IpcDeviceInfo>? = null
}