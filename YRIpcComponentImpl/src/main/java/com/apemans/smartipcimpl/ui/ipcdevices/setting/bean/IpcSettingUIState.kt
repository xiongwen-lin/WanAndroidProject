package com.apemans.smartipcimpl.ui.ipcdevices.setting.bean

import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.smartipcapi.webapi.PackageInfoResult

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/9 5:47 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcSettingUIState {

    var ipcDeviceInfo: IpcDeviceInfo? = null
    var deviceSettingInfo: DeviceSettingInfo? = null
    var detectionInfo: DetectionInfo? = null
    var detectionAreaInfo: DetectionAreaInfo? = null
    var detectionPlanInfo: DetectionPlanInfo? = null
    var storageInfo : StorageInfo? = null
    var cloudInfo : PackageInfoResult? = null
    var mediaModeInfo: MediaModeInfo? = null
    var netSpotInfo: NetSpotInfo? = null
    var ledState: Boolean = false
    var loopRecordState: Boolean = false
    var energyModeState: Boolean = false
    var waterMarkState: Boolean = false

}