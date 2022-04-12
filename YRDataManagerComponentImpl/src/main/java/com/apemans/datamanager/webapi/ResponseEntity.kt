package com.apemans.datamanager.webapi

/* -- 设备相关 -- */

/**
 * 设备绑定状态数据类
 */
data class DeviceBindStatusResult(
    var type : Int,
    var msg : String,
    var data : DeviceBindStateInfo,
    var uuid : String
)

/**
 * 设备绑定结果数据类
 */
data class DeviceBindStateInfo(
    var account : String,
    var product_name : String,
    var product_model : String,
    var model : String
)

/**
 * 设备信息数据类
 */
data class BindDevice(
    var id : Int,
    var bind_type : Int,
    var name : String,
    var uuid : String,
    var sn : String,
    var mac : String,
    var version : String,
    var local_ip : Long,
    var wanip : Long,
    var type : String,
    var time : Long,
    var hb_server : Long,
    var hb_port : Int,
    var hb_domain : String,
    var open_status : Int,
    var online : Int,
    var zone : Float,
    var is_google : Int,
    var is_alexa : Int,
    var puuid : String,
    var wifi_level : Int,
    var battery_level : Int,
    var sort : Int,
    var p_model : String,
    var p_version : String,
    var model_type : Int,
    var secret : String,
    var is_theft : Int,
    var is_notice : Int,
    var app_timing_config : String,
    var mqtt_online : Int,
    var account: String? = "",
    var nickname: String? = ""
)

/**
 * 页码数据类
 */
data class PageInfo(
    var current_page : Int,
    var total_page : Int,
    var total : Int
)

/**
 * 以绑定Ipc数据类
 */
data class BindDeviceListResult(
    var page_info : PageInfo,
    var data : MutableList<BindDevice>
)

/**
 * 设备绑定关系数据类
 */
data class DeviceRelation(
    var id : Int,
    var account : String,
    var type : Int
)

/**
 * 设备绑定关系列表数据类
 */
data class DeviceRelationListResult(
    var page_info : PageInfo,
    var data : MutableList<DeviceRelation>
)

/**
 * 分享结果数据类
 */
data class ShareDeviceResult(
    var num : String
)

/**
 * 设备更新状态数据类
 */
data class DeviceUpgradeStatusResult(
    var type : Int
)

/**
 * 网关设备数据类
 */
data class GatewayDevice(
    var name : String,
    var uuid : String,
    var sn : String,
    var mac : String,
    var version : String,
    var local_ip : Long,
    var wanip : Long,
    var type : String,
    var open_status : Int,
    var online : Int,
    var time : Long,
    var hb_domain : String,
    var hb_server : Long,
    var hb_port : Int,
    var zone : Float,
    var region : String,
    var secret : String,
    var mqtt_online : Int,
    var sort : Int,
    var child : MutableList<BindDevice>,
    var account: String? = "",
    var nickname: String? = ""
)

class PresetPointConfigure {
    var id: Int = 0
    var name: String? = null
    var position: Int = 0
}

class PIRPlanConfigure {
    var id: Int = 0
    var startTime: Int = 0
    var endTime: Int = 0
    var weekArr: List<Int>? = null
}

class DeviceUploadConfigure {
    var PIRPlanList: List<PIRPlanConfigure>? = null
    var PresetPointList: List<PresetPointConfigure>? = null
}

/**
 * 套餐信息数据类
 */
data class PackageInfoResult(
    var id : Int,
    var uid : String,
    var uuid : String,
    var start_time : Long,
    var end_time : Long,
    var total_time : Long,
    var file_time : Int,
    var status : Int,
    var owner : String,
    var is_event : Int,
    var is_free : Int,
    var exist_cloud : Int,
    var model : String,
    var zone : Float
)

/**
 * 固件版本数据类
 */
data class FirmwareVersion(
    var type : String,
    var model : String,
    var version_code : String,
    var key : String,
    var log : String,
    var currentVersionCode : String,
    var md5 : String
)

/**
 * 网关和子设备固件版本数据类
 */
data class GatewayAndChildFirmwareVersion(
    var gateway : FirmwareVersion,
    var child : FirmwareVersion
)

/**
 * 云适配侦测记录类型数据类
 */
data class AwsFileRecord(
    var m : Int,
    var s : Int,
    var p : Int,
    var r : Int,
    var a : Int
)

/**
 * 云适配侦测文件信息数据类
 */
data class AwsFileInfo(
    var s : Int,
    var l : Int,
    var e : AwsFileRecord
)

/**
 * 云适配侦测文件列表数据类
 */
data class AwsFileListResult(
    var filetype : String,
    var pictype : String,
    var file_prefix : String,
    var storage : Int,
    var expirationtime : Long,
    var deviceid : String,
    var userid : String,
    var filedate : String,
    var fileinfo : MutableList<AwsFileInfo>
)

/**
 * 文件预签名数据类
 */
data class AwsFilePreSign(
    var url : String
)

/* -- 设备相关 -- */