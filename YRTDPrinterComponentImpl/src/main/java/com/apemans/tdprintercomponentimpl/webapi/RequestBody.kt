package com.apemans.tdprintercomponentimpl.webapi

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