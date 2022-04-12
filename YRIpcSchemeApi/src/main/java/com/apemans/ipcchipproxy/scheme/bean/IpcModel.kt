/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.bean

import com.apemans.ipcchipproxy.define.IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/20 10:51 上午
 * 说明:
 *
 * 备注: Ipc设备命令数据结构模型
 *
 ***********************************************************/

class IpcSchemeRetryCmdResult<T> {
    var isSuccess = false
    var data : T? = null
}

class IpcSchemeResult {
    var code: Int = 0
    var result : String? = null
    var error : String? = null
}

class DeviceSettingInfo {
    var deviceId : String? = null
    var model : String? = null
    var ledOn : Boolean = false
    var loopRecordOn : Boolean = false
    var sleep : Boolean = false
    var rotate : Boolean = false
    var audioRecordOn : Boolean = false
    var motionTrackingOn : Boolean = false
    var detectCryOn : Boolean = false
    var detectHumanOn : Boolean = false
    var ir : Int = 0
    var battery : Int = 0
    var version : String? = ""
    var storageInfo : StorageInfo? = null
    var detectionInfoList : List<DetectionInfo>? = null
}

class StorageInfo {
    var total : Int = 0
    var free : Int = 0
    var process : Int = 0
    var status : Int = 0
}

class StorageRecordQueryParam {
    var startTime : Long = 0
}

class StorageRecordDateInfo {
    var today : Int = 0;
    var recordDataList : List<Int> ? = null
}

class StorageRecordVideoInfo {
    var recordList : List<RecordVideoInfo>? = null
}

class RecordVideoInfo {
    var start : Int = 0
    var length : Int = 0
}

class StorageRecordImageInfo {
    var recordList : List<RecordImageInfo>? = null
}

class RecordImageInfo {
    var start : Int = 0
    var offset : Int = 0
}

class DetectionInfo {
    var detectionType : Int = 0
    var enable : Boolean = false
    var level : Int = IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE
    var alarmOn : Boolean = false
    var lightInfo : LightInfo? = null
    var planInfo : DetectionPlanInfo? = null
}

class DetectionPlanInfo {
    var planSchedules: List<DetectionPlanSchedule>? = null
    var pirPlanSchedules: List<Boolean>? = null
}

class DetectionPlanSchedule {
    var id: Int = 0
    var startTime: Int = 0
    var endTime: Int = 0
    var weekArr: List<Int>? = null
    var effective: Boolean = true
}

class DetectionParam {
    var operationType : Int = 0
    var info : DetectionInfo? = null
}

class LightInfo {
    var mode : Int = 0
    var duration : Int = 0
    var frequency : Int = 0
}

class DetectionAreaInfo {
    var width : Int = 0
    var height : Int = 0
    var level : Int = IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE
    var enable : Boolean = false
    var areaPoint : Array<DetectionAreaPoint>? = null
}

class DetectionAreaPoint {
    var ltX : Int = 0;
    var ltY : Int = 0;
    var rbX : Int = 0;
    var rbY : Int = 0;
}

class TimeConfigure {
    var mode : Int = 0
    var timeZone : Float = 0f
    var timeOffset : Int = 0
}

class SoundStateInfo {
    var alarmOn : Boolean = false
    var talkOn : Boolean = false
}

class AlarmStateInfo {
    var open : Int = 0
    var type : Int = 0
    var dur : Int = 0
    var times : Int = 0
}

class UpgradeParam {
    var model : String? = ""
    var version : String? = ""
    var pkt : String? = ""
    var md5 : String? = ""
}

class RotatePresetPoint {
    var pointIndex : Int = 0
}

class MediaModeInfo {
    var mode : Int = 0
    var picNum : Int = 0
    var vidDur : Int = 0
}

class NetSpotInfo {
    var psd : String? = ""
    var ssid : String? = ""
}

class NetSpotConfigure {
    var uid : String? = null
    var ssid : String? = null
    var psd : String? = null
    var region : String? = null
    var zone : String? = null
    var encrypt : String? = null
}

class ConnectionConfigure {
    var userName : String? = null
    var uuid : String? = null
    var secret : String? = null
    var serverDomain : String? = null
    var serverPort : Int = 0
    var modeType : Int = 0
    var apConnType : Int = 0
    var connType : Int = 0

    var sub1UUID : String? = null
    var sub2UUID : String? = null
    var sub3UUID : String? = null
    var sub4UUID : String? = null
    var cmdConfig : String? = null
}

class NetSpotPairInfo {
    var status : Int = 0;
    var deviceId : String? = null
}

class NetSpotDeviceInfo {
    var deviceId : String? = ""
    var model : String? = ""
    var apDeviceId : String? = ""
    var ssid : String? = ""
    var bleDeviceId : String? = ""
}

class UnbindParam {
    var uid : String? = ""
    var parentId : String? = ""
}