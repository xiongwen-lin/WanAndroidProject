/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.util

import com.nooie.sdk.base.Constant
import com.nooie.sdk.device.bean.*
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcselfdevelopscheme.define.*
import com.nooie.sdk.device.bean.hub.*

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/14 4:22 下午
 * 说明:
 *
 * 备注: 自有Ipc方案设备工具类
 *
 ***********************************************************/
object YRIpcDeviceUtil {

    private const val NORMAL_DETECTION_PLAN_SCHEDULE_SIZE = 3
    private const val DETECTION_PLAN_WEEK_LENGTH = 7
    private const val DETECTION_PLAN_HOUR = 24
    private const val DETECTION_PLAN_MINUTE = 60
    private const val PIR_PLAN_DURATION_NUM = 48
    private const val PIR_PLAN_DURATION = 30

    fun convertModelType(modelType : Int?) : Int {
        return when (modelType) {
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_NORMAL -> {Constant.MODEL_TYPE_NOOIE_IPC007_1080P}
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_LP_CAM -> {Constant.MODEL_TYPE_MH_EC810_CAM}
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_HUB -> {Constant.MODEL_TYPE_NOOIE_IPC007_1080P}
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_ROTATE -> {Constant.MODEL_TYPE_NOOIE_IPC100}
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_GUN -> {Constant.MODEL_TYPE_NOOIE_IPC200}
            IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_MINI -> {Constant.MODEL_TYPE_MH_VP04}
            else -> {Constant.MODEL_TYPE_NOOIE_IPC007_1080P}
        }
    }

    fun convertConnType(connType : Int?) : ConnType {
        return when(connType) {
            IPC_SELF_DEVELOP_SCHEME_CONN_TYPE_HUB -> ConnType.CONN_TYPE_HUB
            else -> ConnType.CONN_TYPE_IPC
        }
    }

    fun convertConnType(connType : ConnType?) : Int {
        return when(connType) {
            ConnType.CONN_TYPE_HUB -> IPC_SELF_DEVELOP_SCHEME_CONN_TYPE_HUB
            else -> IPC_SELF_DEVELOP_SCHEME_CONN_TYPE_IPC
        }
    }

    fun convertCmdType(cmdType : Int?) : Int {
        return when(cmdType) {
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_NORMAL, IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM,
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_HUB, IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM_HUB -> {cmdType}
            else -> {IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN}
        }
    }

    fun convertNetSpotConnType(connType : Int?) : Int {
        return when(connType) {
            IPC_SCHEME_AP_CONN_TYPE_P2P -> Constant.AP_CONN_TYPE_P2P_AP
            IPC_SCHEME_AP_CONN_TYPE_TCP -> Constant.AP_CONN_TYPE_TCP
            IPC_SCHEME_AP_CONN_TYPE_HTTP-> Constant.AP_CONN_TYPE_HTTP
            else -> Constant.AP_CONN_TYPE_P2P_AP
        }
    }

    fun convertAPConnType(connType : Int?) : Int {
        return when(connType) {
            Constant.AP_CONN_TYPE_P2P_AP -> IPC_SCHEME_AP_CONN_TYPE_P2P
            Constant.AP_CONN_TYPE_TCP -> IPC_SCHEME_AP_CONN_TYPE_TCP
            Constant.AP_CONN_TYPE_HTTP -> IPC_SCHEME_AP_CONN_TYPE_HTTP
            else -> IPC_SCHEME_AP_CONN_TYPE_P2P
        }
    }

    fun convertSensitivity(level : MotionDetectLevel?) : Int {
        return when(level) {
            MotionDetectLevel.MOTION_DETECT_LOW -> { IPC_SCHEME_SENSITIVITY_LEVEL_LOW }
            MotionDetectLevel.MOTION_DETECT_MEDIUM -> { IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE }
            MotionDetectLevel.MOTION_DETECT_HIGH -> { IPC_SCHEME_SENSITIVITY_LEVEL_HIGH }
            else -> { IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE }
        }
    }

    fun convertSensitivity(level : SoundDetectLevel?) : Int {
        return when(level) {
            SoundDetectLevel.SOUND_DETECT_LOW -> { IPC_SCHEME_SENSITIVITY_LEVEL_LOW }
            SoundDetectLevel.SOUND_DETECT_MEDIUM -> { IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE }
            SoundDetectLevel.SOUND_DETECT_HIGH -> { IPC_SCHEME_SENSITIVITY_LEVEL_HIGH }
            else -> { IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE }
        }
    }

    fun convertSensitivity(level : SensitivityLevel?) : Int {
        return when(level) {
            SensitivityLevel.SENSITIVITY_LEVEL_LOW -> { IPC_SCHEME_SENSITIVITY_LEVEL_LOW }
            SensitivityLevel.SENSITIVITY_LEVEL_MIDDLE-> { IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE }
            SensitivityLevel.SENSITIVITY_LEVEL_HIGH -> { IPC_SCHEME_SENSITIVITY_LEVEL_HIGH }
            else -> { IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE }
        }
    }

    fun convertMotionDetectLevel(level : Int) : MotionDetectLevel {
        return when(level) {
            IPC_SCHEME_SENSITIVITY_LEVEL_LOW -> {MotionDetectLevel.MOTION_DETECT_LOW}
            IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE -> {MotionDetectLevel.MOTION_DETECT_MEDIUM}
            IPC_SCHEME_SENSITIVITY_LEVEL_HIGH -> {MotionDetectLevel.MOTION_DETECT_HIGH}
            else -> {MotionDetectLevel.MOTION_DETECT_CLOSE}
        }
    }

    fun convertSoundDetectLevel(level : Int) : SoundDetectLevel {
        return when(level) {
            IPC_SCHEME_SENSITIVITY_LEVEL_LOW -> {SoundDetectLevel.SOUND_DETECT_LOW}
            IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE -> {SoundDetectLevel.SOUND_DETECT_MEDIUM}
            IPC_SCHEME_SENSITIVITY_LEVEL_HIGH -> {SoundDetectLevel.SOUND_DETECT_HIGH}
            else -> {SoundDetectLevel.SOUND_DETECT_CLOSE}
        }
    }

    fun convertSensitivityLevel(level : Int) : SensitivityLevel {
        return when(level) {
            IPC_SCHEME_SENSITIVITY_LEVEL_LOW -> {SensitivityLevel.SENSITIVITY_LEVEL_LOW}
            IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE -> {SensitivityLevel.SENSITIVITY_LEVEL_MIDDLE}
            IPC_SCHEME_SENSITIVITY_LEVEL_HIGH -> {SensitivityLevel.SENSITIVITY_LEVEL_HIGH}
            else -> {SensitivityLevel.SENSITIVITY_LEVEL_LOW}
        }
    }

    fun convertIcrMode(mode : Int) : ICRMode {
        return when(mode) {
            IPC_SCHEME_LIGHT_MODE_IR -> ICRMode.ICR_MODE_AUTO
            else -> ICRMode.ICR_MODE_DAY
        }
    }

    fun convertIRMode(mode : Int) : IRMode {
        return when(mode) {
            IPC_SCHEME_LIGHT_MODE_IR -> IRMode.IR_MODE_AUTO
            else -> IRMode.IR_MODE_OFF
        }
    }

    fun convertLightInfo(mode : ICRMode?) : LightInfo {
        return LightInfo().apply {
            this.mode = if (mode == ICRMode.ICR_MODE_AUTO) IPC_SCHEME_LIGHT_MODE_IR else IPC_SCHEME_LIGHT_MODE_OFF
        }
    }

    fun convertLightInfo(mode : IRMode?) : LightInfo {
        return LightInfo().apply {
            this.mode = if (mode == IRMode.IR_MODE_AUTO) IPC_SCHEME_LIGHT_MODE_IR else IPC_SCHEME_LIGHT_MODE_OFF
        }
    }

    fun convertStorageInfo(info : FormatInfo?) : StorageInfo? {
        return info?.let {
            StorageInfo().apply {
                free = it.free
                total = it.total
                status = it.formatStatus
                process = it.progress
            }
        }
    }

    fun convertRecordDataList(list : List<Int>?) : List<Int>? {
        if (list.isNullOrEmpty()) {
            return null
        }
        val dateList = mutableListOf<Int>()
        var length = list.size
        var i = 0
        while (i < 92) {
            var dateValue = if (i < length) { list[i] } else { 0 }
            dateList.add(dateValue)
            i++
        }
        return dateList
    }

    fun  convertRecordVideoInfo(list : List<RecordFragment>?) : StorageRecordVideoInfo? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return StorageRecordVideoInfo().apply {
            recordList = list.map {
                RecordVideoInfo().apply {
                    start = it.start
                    length = it.len
                }
            }
        }
    }

    fun  convertRecordImageInfo(list : List<ImgItem>?) : StorageRecordImageInfo? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return StorageRecordImageInfo().apply {
            recordList = list.map {
                RecordImageInfo().apply {
                    start = it.start
                    offset = it.startMs
                }
            }
        }
    }

    fun convertDetectionAreaInfo(info : MTAreaInfo?) : DetectionAreaInfo? {
        return info?.let {
            DetectionAreaInfo().apply {
                width = it.horMaxSteps
                height = it.verMaxSteps
                enable = it.state
                level = convertSensitivity(it.level)
                areaPoint = convertDetectionAreaPoint(it.areaRects)
            }
        }
    }

    fun convertMtAreaInfo(info : DetectionAreaInfo?) : MTAreaInfo? {
        return info?.let {
            MTAreaInfo().apply {
                horMaxSteps = it.width
                verMaxSteps = it.height
                state = it.enable
                level = convertSoundDetectLevel(info.level)
                areaRects = convertAreaRect(info.areaPoint)
            }
        }
    }

    fun convertDetectionAreaPoint(points: Array<AreaRect>?) : Array<DetectionAreaPoint>? {
        return points?.map {
            DetectionAreaPoint().apply {
                ltX = it.ltX
                ltY = it.ltY
                rbX = it.rbX
                rbY = it .rbY
            }
        }?.toTypedArray()
    }

    fun convertAreaRect(points: Array<DetectionAreaPoint>?) : Array<AreaRect>? {
        return points?.map {
            AreaRect().apply {
                ltX = it.ltX
                ltY = it.ltY
                rbX = it.rbX
                rbY = it .rbY
            }
        }?.toTypedArray()
    }

    fun convertMediaMode(info : MediaModeInfo?) : NooieMediaMode? {
        return info?.let {
            NooieMediaMode(it.mode, it.picNum, it.vidDur)
        }
    }

    fun convertMediaModeInfo(info : NooieMediaMode?) : MediaModeInfo? {
        return info?.let {
            MediaModeInfo().apply {
                mode = it.mode
                picNum = it.picNum
                vidDur = it.vidDur
            }
        }
    }

    fun convertNetSpot(info : NetSpotInfo?) : NooieHotspot? {
        return info?.let {
            NooieHotspot(it.psd, it.ssid)
        }
    }

    fun convertNetSpotInfo(info : NooieHotspot?) : NetSpotInfo? {
        return info?.let {
            NetSpotInfo().apply {
                psd = it.psd
                ssid = it.ssid
            }
        }
    }

    fun convertDetectionInfo(state : PirState?) :DetectionInfo? {
        return state?.let {
            DetectionInfo().apply {
                enable = it.enable
                alarmOn = it.siren
                level = convertSensitivity(it.sensitivityLevel)
            }
        }
    }

    fun convertDetectionInfo(state : PirStateV2?) :DetectionInfo? {
        return state?.let {
            DetectionInfo().apply {
                enable = it.enable
                alarmOn = it.siren
                level = convertSensitivity(it.sensitivityLevel)
                lightInfo = LightInfo().apply {
                    mode = it.lightMode
                    duration = it.duration
                    frequency = it.lightFlashFrequency
                }
            }
        }
    }

    fun convertDetectionInfo(state : MotionDetectLevel?) :DetectionInfo? {
        return state?.let {
            DetectionInfo().apply {
                enable = checkDetectionEnable(convertSensitivity(it))
                level = convertSensitivity(it)
            }
        }
    }

    fun convertDetectionInfo(state : SoundDetectLevel?) :DetectionInfo? {
        return state?.let {
            DetectionInfo().apply {
                enable = checkDetectionEnable(convertSensitivity(it))
                level = convertSensitivity(it)
            }
        }
    }

    fun convertDeviceSettingInfo(deviceId : String?, setting : DevAllSettingsV2?) : DeviceSettingInfo? {
        return setting?.commSettings?.let {
            DeviceSettingInfo().apply {
                this.deviceId = deviceId
                ledOn = it.led == 1
                loopRecordOn = it.loopRec == 1
                sleep = it.sleep == 1
                rotate = it.flip == 1
                audioRecordOn = it.audioRec == 1
                motionTrackingOn = it.motTrack == 1
                detectCryOn = it.detectCry == 1
                ir = convertLightInfo(ICRMode.getICRMode(it.icr)).mode
            }
        }
    }

    fun convertDeviceSettingInfo(deviceId : String?, setting : HubInfo?) : DeviceSettingInfo? {
        return setting?.let {
            DeviceSettingInfo().apply {
                this.deviceId = it.uuid
                ledOn = it.led
                version = it.softVer
                storageInfo = it.fmtInfo?.let {
                    StorageInfo().apply {
                        free = it.free
                        total = it.total
                        status = it.formatStatus
                    }
                }
            }
        }
    }

    fun convertDeviceSettingInfo(deviceId : String?, setting : CameraInfo?) : DeviceSettingInfo? {
        return setting?.let {
            DeviceSettingInfo().apply {
                this.deviceId = it.uuid
                version = it.softVer
                battery = it.batteryLevel
                rotate = it.videoRotate
                detectHumanOn = it.ai
                ir = convertLightInfo(it.ir).mode
                var detectionInfo = it?.pir.let {
                    DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                        enable = it.enable
                        level = convertSensitivity(it.sensitivityLevel)
                        alarmOn = it.siren
                    }
                }
                detectionInfoList = if (detectionInfo != null) listOf(detectionInfo) else null
            }
        }
    }

    fun convertApNetCfg(configure : NetSpotConfigure?) : APNetCfg? {
        return configure?.let {
            APNetCfg().apply {
                uid = it.uid
                ssid = it.ssid
                psd = it.psd
                region = it.region
                zone = it.zone
                encrypt = it.encrypt
            }
        }
    }

    fun convertDeviceConnInfo(configure : ConnectionConfigure?) : DeviceConnInfo? {
        return configure?.let {
            DeviceConnInfo().apply {
                userName = it.userName
                uuid = it.uuid
                secret = it.secret
                hbServer = it.serverDomain
                hbPort = it.serverPort
                modeType = it.modeType
                apConnType = convertNetSpotConnType(it.apConnType)
                connType = convertConnType(it.connType)
                sub1UUID = it.sub1UUID
                sub2UUID = it.sub2UUID
                sub3UUID = it.sub3UUID
                sub4UUID = it.sub4UUID
                cmdConfig = it.cmdConfig
            }
        }
    }

    fun convertConnectionConfigure(connInfo: DeviceConnInfo?) : ConnectionConfigure? {
        return connInfo?.let {
            ConnectionConfigure().apply {
                userName = it.userName
                uuid = it.uuid
                secret = it.secret
                serverDomain = it.hbServer
                serverPort = it.hbPort
                modeType = it.modeType
                apConnType = convertAPConnType(it.apConnType)
                connType = convertConnType(it.connType)
                sub1UUID = it.sub1UUID
                sub2UUID = it.sub2UUID
                sub3UUID = it.sub3UUID
                sub4UUID = it.sub4UUID
                cmdConfig = it.cmdConfig
            }
        }
    }

    fun convertDetectionPlanInfo(planList: List<AlertPlanItem>?) : DetectionPlanInfo? {

        if (planList.isNullOrEmpty() || planList.size != NORMAL_DETECTION_PLAN_SCHEDULE_SIZE * DETECTION_PLAN_WEEK_LENGTH) {
            return null
        }

        val detectionPlanInfo = DetectionPlanInfo()
        var detectionPlanSchedules = mutableListOf<DetectionPlanSchedule>()
        val planItemSize = planList.size

        var planItem: AlertPlanItem? = null
        for (i in 0 until NORMAL_DETECTION_PLAN_SCHEDULE_SIZE) {
            var detectionPlanSchedule: DetectionPlanSchedule? = null
            var detectionPlanScheduleWeek = mutableListOf<Int>()
            var firstStart = 0
            var firstEnd = 0;
            for (j in 0 until DETECTION_PLAN_WEEK_LENGTH) {
                var index = j + DETECTION_PLAN_WEEK_LENGTH * i
                if (index < 0 || index >= planItemSize) {
                    continue
                }
                planItem = null
                planItem = planList[index]
                if (planItem != null) {
                    if (j == 0) {
                        detectionPlanSchedule = DetectionPlanSchedule().apply {
                            id = i + 1
                            startTime = planItem.startTime
                            endTime = planItem.endTime
                        }
                        firstStart = planItem.startTime
                        firstEnd = planItem.endTime
                    }

                    if (planItem.status == 1) {
                        detectionPlanScheduleWeek.add(1)
                    } else {
                        detectionPlanScheduleWeek.add(0)
                    }

                    if (firstStart != planItem.startTime || firstEnd != planItem.endTime) {
                        detectionPlanSchedule?.effective = false
                    }
                }
            }

            if (detectionPlanSchedule != null) {
                detectionPlanSchedule.weekArr = detectionPlanScheduleWeek
                detectionPlanSchedules.add(detectionPlanSchedule)
            }
        }

        detectionPlanInfo.planSchedules = detectionPlanSchedules
        return detectionPlanInfo

    }

    fun convertDetectionPlanInfo(planList: BooleanArray?) : DetectionPlanInfo? {

        if (planList == null || planList.size != PIR_PLAN_DURATION_NUM * DETECTION_PLAN_WEEK_LENGTH) {
            return null
        }

        val detectionPlanInfo = DetectionPlanInfo()
        detectionPlanInfo.pirPlanSchedules = planList.map {
            it
        }
        return detectionPlanInfo
    }

    fun convertDetectionPlan(detectionPlanInfo: DetectionPlanInfo?, sourceAlertPlans: List<AlertPlanItem>?) : List<AlertPlanItem> {
        val alertPlans = mutableListOf<AlertPlanItem>()
        if (sourceAlertPlans.isNullOrEmpty() || sourceAlertPlans?.size != NORMAL_DETECTION_PLAN_SCHEDULE_SIZE * DETECTION_PLAN_WEEK_LENGTH) {
            for (i in 0 until NORMAL_DETECTION_PLAN_SCHEDULE_SIZE * DETECTION_PLAN_WEEK_LENGTH) {
                alertPlans.add(AlertPlanItem().apply {
                    startTime = 0
                    endTime = DETECTION_PLAN_HOUR * DETECTION_PLAN_MINUTE - 1
                    status = 0
                })
            }
        } else {
            alertPlans.addAll(sourceAlertPlans)
        }
        var planSchedule = detectionPlanInfo?.planSchedules?.getOrNull(0)
        if (planSchedule == null || planSchedule.id < 1 || planSchedule?.weekArr?.size != DETECTION_PLAN_WEEK_LENGTH || planSchedule.startTime < 0 || planSchedule.endTime < planSchedule.startTime) {
            return alertPlans
        }
        val startIndex = (planSchedule.id - 1) * DETECTION_PLAN_WEEK_LENGTH
        val subAlertPlans = convertDetectionPlan(planSchedule)
        for (i in 0 until DETECTION_PLAN_WEEK_LENGTH) {
            var alertPlan = subAlertPlans.getOrNull(i)
            if (alertPlan == null) {
                continue
            }
            alertPlans.getOrNull(startIndex + i)?.apply {
                startTime = alertPlan.startTime
                endTime = alertPlan.endTime
                status = alertPlan.status
            }
        }
        return alertPlans
    }

    fun convertPirPlan(detectionPlanInfo: DetectionPlanInfo?) : PirPlan {
        return PirPlan().apply {
            plan = convertPirDetectionPlan(detectionPlanInfo)
        }
    }

    fun convertNetSportState(state: Int) : Int {
        return when(state) {
            APPairStatus.AP_PAIR_NO_RECV_WIFI.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_NO_RECV_WIFI }
            APPairStatus.AP_PAIR_RECVED_WIFI.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_RECVED_WIFI }
            APPairStatus.AP_PAIR_CONNECTING_WIFI.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_CONNECTING_WIFI }
            APPairStatus.AP_PAIR_CONN_WIFI_SUCC.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_CONN_WIFI_SUCC }
            APPairStatus.AP_PAIR_CONN_WIFI_FAILED.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_CONN_WIFI_FAILED }
            APPairStatus.AP_PAIR_START_ONLINE.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_START_ONLINE }
            APPairStatus.AP_PAIR_GOT_NET_TIME.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_GOT_NET_TIME }
            APPairStatus.AP_PAIR_P2P_CONNECTED.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_P2P_CONNECTED }
            APPairStatus.AP_PAIR_ONLINE_FAILED.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_ONLINE_FAILED }
            APPairStatus.AP_PAIR_ONLINE_SUCC.intValue -> { IPC_SCHEME_NET_SPOT_PAIR_ONLINE_SUCCESS }
            else -> { IPC_SCHEME_NET_SPOT_PAIR_UNKNOWN }
        }
    }

    fun isReadAuthority(authority : String?) : Boolean {
        return authority == IPC_SCHEME_DP_AUTHORITY_R
    }

    fun isWriteAuthority(authority : String?) : Boolean {
        return authority == IPC_SCHEME_DP_AUTHORITY_W
    }

    fun checkIsChildDevice(parentDeviceId: String?): Boolean {
        return !parentDeviceId.isNullOrEmpty() && parentDeviceId != "1"
    }

    private fun checkDetectionEnable(level : Int) : Boolean {
        return level == IPC_SCHEME_SENSITIVITY_LEVEL_LOW || level == IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE || level == IPC_SCHEME_SENSITIVITY_LEVEL_HIGH
    }

    private fun checkMatchModel(model : String, targetModel : String) : Boolean {
        return !model.isNullOrEmpty() && !targetModel.isNullOrEmpty() && model.startsWith(targetModel, true)
    }


    private fun convertPirDetectionPlan(detectionPlanInfo: DetectionPlanInfo?) : BooleanArray {
        val pirPlanList = BooleanArray(PIR_PLAN_DURATION_NUM  * DETECTION_PLAN_WEEK_LENGTH)
        detectionPlanInfo?.planSchedules?.forEach { planSchedule ->
            val tmpPirPlanSchedule = convertPirPlanSchedule(planSchedule)
            if (tmpPirPlanSchedule.size == pirPlanList.size) {
                val planSize = pirPlanList.size
                for (i in 0 until planSize) {
                    pirPlanList[i] = pirPlanList[i] || tmpPirPlanSchedule[i]
                }
            }
        }
        return pirPlanList
    }

    /**
     * Pir侦测日程表转化为Pir命令格式
     * startTime 大于等于0，单位：分
     * endTime 小于等于1440即24：00， 单位分
     * weekArr 长度7（即一周七天），weekArr从1到7代表星期一到星期天，某一天开启为1，关闭为0。但Pir命令格式的排列从1到7代表星期天到星期六
     */
    private fun convertPirPlanSchedule(planSchedule: DetectionPlanSchedule?): BooleanArray {
        var pirPlanSchedule = BooleanArray(PIR_PLAN_DURATION_NUM * DETECTION_PLAN_WEEK_LENGTH)
        if (planSchedule == null) {
            return pirPlanSchedule
        }

        var pirPlanTimes = convertPirPlanTimes(planSchedule.startTime, planSchedule.endTime)
        planSchedule.weekArr = convertPirPlanWeek(planSchedule.weekArr)

        for (i in 0 until DETECTION_PLAN_WEEK_LENGTH) {
            val isDaySelected = planSchedule.weekArr?.getOrNull(i) == 1
            if (!isDaySelected) {
                continue
            }
            for (j in i * PIR_PLAN_DURATION_NUM until (i + 1) * PIR_PLAN_DURATION_NUM) {
                pirPlanSchedule[j] = pirPlanTimes[j % PIR_PLAN_DURATION_NUM]
            }
        }
        return pirPlanSchedule
    }

    private fun convertPirPlanTimes(start: Int, end: Int): BooleanArray {
        val startIndex: Int = start / PIR_PLAN_DURATION
        val endIndex: Int = end / PIR_PLAN_DURATION
        val pirPlanTimes = BooleanArray(PIR_PLAN_DURATION_NUM)
        if (startIndex > endIndex) {
            return pirPlanTimes
        }
        if (startIndex == endIndex) {
            pirPlanTimes[startIndex] = true
            return pirPlanTimes
        }
        for (i in pirPlanTimes.indices) {
            pirPlanTimes[i] = i >= startIndex && i < endIndex
        }
        return pirPlanTimes
    }

    private fun convertPirPlanWeek(weekArr: List<Int>?) : List<Int> {
        val result = mutableListOf<Int>()
        if (weekArr == null || weekArr?.size != DETECTION_PLAN_WEEK_LENGTH) {
            for (i in 0 until DETECTION_PLAN_WEEK_LENGTH) {
                result.add(0)
            }
            return result
        }
        var lastDay = weekArr.getOrElse((DETECTION_PLAN_WEEK_LENGTH - 1)) { 0 }
        result.add(lastDay)
        for (i in 0 until (DETECTION_PLAN_WEEK_LENGTH -1)) {
            result.add(weekArr.getOrElse(i) { 0 })
        }
        return result
    }

    /**
     * 常规Ipc侦测日程表转化为对应命令格式
     * startTime 大于等于0，单位：分
     * endTime 小于等于1439即23：59， 单位分
     * weekArr 长度7（即一周七天），weekArr从1到7代表星期一到星期天，某一天开启为1，关闭为0。但对应命令格式的排列与weekArr一致
     */
    private fun convertDetectionPlan(planSchedule: DetectionPlanSchedule?) : List<AlertPlanItem> {
        var alertPlans = mutableListOf<AlertPlanItem>()
        for (i in 0 until DETECTION_PLAN_WEEK_LENGTH) {
            alertPlans.add(AlertPlanItem().apply {
                startTime = 0
                endTime = DETECTION_PLAN_HOUR * DETECTION_PLAN_MINUTE - 1
                status = 0
            })
        }
        if (planSchedule == null || planSchedule?.weekArr?.size != DETECTION_PLAN_WEEK_LENGTH || planSchedule.startTime < 0 || planSchedule.endTime < planSchedule.startTime) {
            return alertPlans
        }
        val planScheduleSize = planSchedule.weekArr?.size ?: 0
        for (i in 0 until planScheduleSize) {
            alertPlans.getOrNull(i)?.apply {
                startTime = planSchedule.startTime
                endTime = planSchedule.endTime
                status = if (planSchedule.weekArr?.getOrNull(i) == 1) {
                    1
                } else {
                    0
                }
            }
        }
        return alertPlans
    }
}