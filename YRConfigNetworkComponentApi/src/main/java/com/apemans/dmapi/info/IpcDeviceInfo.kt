/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

import com.apemans.dmapi.webapi.BindDevice
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 09:54
 * 说明: Ipc类设备类信息
 *
 * 备注:
 *
 ***********************************************************/
open class IpcDeviceInfo(
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

    var id : Int = 0
    var bindType : Int = 0
    var sn : String = ""
    var mac : String = ""
    var version : String = ""
    var localIp : Long = 0
    var wanIp : Long = 0
    var time : Long = 0
    var hbServer : Long = 0
    var hbPort : Int = 0
    var hbDomain : String = ""
    var openStatus : Int = 0
    var online : Int = 0
    var zone : Float = 0f
    var isGoogle : Int = 0
    var isAlexa : Int = 0
    var pDeviceId : String = ""
    var wifiLevel : Int = 0
    var batteryLevel : Int = 0
    var sort : Int = 0
    var pModel : String = ""
    var pVersion : String = ""
    var modelType : Int = 0
    var secret : String = ""
    var isTheft : Int = 0
    var isNotice : Int = 0
    var appTimingConfig : String = ""
    var mqttOnline : Int = 0
    var region : String = ""
    var subDevices : MutableList<BindDevice>? = null
    var ownerNickname: String? = ""
    var ownerAccount: String? = ""

}