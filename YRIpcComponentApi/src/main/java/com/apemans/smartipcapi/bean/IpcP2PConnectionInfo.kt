package com.apemans.smartipcapi.bean

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 4:45 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcP2PConnectionInfo {

    var deviceId: String? = ""
    var parentDeviceId: String? = ""
    var model: String? = ""
    var uid: String? = ""
    var hbDomain: String? = ""
    var hbServer: Long = 0
    var hbPort: Int = 0
    var secret: String? = ""
    var online: Boolean = false
}