package com.apemans.smartipcimpl.bean

import com.apemans.ipcchipproxy.scheme.bean.DataPoint

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/29 5:16 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class DeviceConfigureModel {

    var dps : List<DataPoint>? = null
    var model : String? = null
    var cmdType : Int = 0
    var modelType : Int = 0
    var connType : Int = 0

}