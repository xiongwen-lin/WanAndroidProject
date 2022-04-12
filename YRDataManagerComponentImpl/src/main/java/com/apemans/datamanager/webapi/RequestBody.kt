package com.apemans.datamanager.webapi

/**
 * 更新设备名Body类
 */
data class UpdateDeviceNameBody(
    var uuid : String,
    var name : String
)

/**
 * 分享设备Body类
 */
data class SharingDeviceBody(
    var account : String,
    var uuid : String
)

/**
 * 分享设备反馈Body类
 */
data class SharingFeedbackBody(
    var msg_id : Int,
    var share_id : Int,
    var status : Int
)

/**
 * 设备排序Body类
 */
data class DeviceSortBody(
    var json : String
)

/**
 * 绑定设备Body类
 */
data class DeviceBindingBody(
    var uuid : String
)

/**
 * 更新设备配置Body类
 */
data class UpdateDeviceConfigureBody(
    var uuid : String,
    var app_timing_config : String
)

/**
 * 更新设备推送开关Body类
 */
data class UpdateDeviceNoticeBody(
    var uuid : String,
    var is_notice : Int
)

/**
 * 解绑设备Body类
 */
data class DeleteDeviceBindingBody(
    var uuid : String
)