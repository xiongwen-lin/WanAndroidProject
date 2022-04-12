package com.apemans.tdprintercomponentimpl.webapi

/* -- 设备相关 -- */

/**
 * 设备信息数据类
 */
data class BindDevice(
    var id : Int = 0,
    var bind_type : Int = 0,
    var name : String? = null,
    var uuid : String? = null,
    var sn : String? = null,
    var mac : String? = null,
    var version : String? = null,
    var local_ip : Long = 0,
    var wanip : Long = 0,
    var type : String? = null,
    var time : Long = 0,
    var hb_server : Long = 0,
    var hb_port : Int = 0,
    var hb_domain : String? = null,
    var open_status : Int = 0,
    var online : Int = 0,
    var zone : Float,
    var is_google : Int = 0,
    var is_alexa : Int = 0,
    var puuid : String? = null,
    var wifi_level : Int = 0,
    var battery_level : Int = 0,
    var sort : Int = 0,
    var p_model : String? = null,
    var p_version : String? = null,
    var model_type : Int = 0,
    var secret : String? = null,
    var is_theft : Int = 0,
    var is_notice : Int = 0,
    var app_timing_config : String? = null,
    var mqtt_online : Int = 0,
    var account: String? = "",
    var nickname: String? = ""
)

/**
 * 页码数据类
 */
data class PageInfo(
    var current_page : Int = 0,
    var total_page : Int = 0,
    var total : Int = 0
)

/**
 * 设备绑定关系数据类
 */
data class DeviceRelation(
    var id : Int = 0,
    var account : String? = null,
    var type : Int = 0
)

/**
 * 设备绑定关系列表数据类
 */
data class DeviceRelationListResult(
    var page_info : PageInfo? = null,
    var data : MutableList<DeviceRelation>? = null
)

/**
 * 分享结果数据类
 */
data class ShareDeviceResult(
    var num : String? = null
)

/**
 * 设备更新状态数据类
 */
data class DeviceUpgradeStatusResult(
    var type : Int = 0
)

/* -- 设备相关 -- */