package com.apemans.smartipcimpl.ui.ipcdevices.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcapi.webapi.DeviceRelation
import com.apemans.smartipcapi.webapi.DeviceUpgradeStatusResult
import com.apemans.smartipcapi.webapi.DeviceUploadConfigure
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.ui.actioncameralist.IpcControlViewModel
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.define.IPC_SCHEME_DP_AUTHORITY_R
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcchipproxy.scheme.util.parseIpcDPCmdResult
import com.apemans.logger.YRLog
import com.apemans.smartipcapi.webapi.FirmwareVersion
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.db.DeviceManagerDbHelper
import com.apemans.smartipcimpl.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/5 8:01 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcSettingViewModel : IpcControlViewModel() {

    val uiState = MutableLiveData(IpcSettingUIState())
    val deviceRelationState = MutableLiveData(listOf<DeviceRelation>())
    val upgradeInfoState = MutableLiveData(FirmwareVersion("", "", "", "", "", "", ""))
    val upgradeState = MutableLiveData(DeviceDefine.UPGRADE_TYPE_NORMAL)
    val detectionSchedules = MutableLiveData(listOf<DetectionPlanSchedule>())

    fun  loadDeviceInfo(deviceId: String) {
        viewModelScope.launch {
            DeviceManagerRepository.getIpcDevice(deviceId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
                ?.let {
                    (it.deviceInfo as? IpcDeviceInfo)
                }
                ?.let { info ->
                    uiState.value = uiState.value?.apply {
                        ipcDeviceInfo = info
                    }
                }
        }
    }

    fun loadDeviceNetSpotConfigure(deviceId: String) {
        DeviceControlHelper.getNetSpotDeviceConfigure()?.let {
            uiState.value = uiState?.value?.apply {
                ipcDeviceInfo = it.ipcDevice?.deviceInfo as? IpcDeviceInfo
            }
        }
    }

    fun loadDeviceSetting(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(getDeviceSetting(deviceId).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadDeviceSetting code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var settingInfo : DeviceSettingInfo? = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_DEVICE_SETTING) {
                    object : TypeToken<Map<String, DeviceSettingInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadDeviceSetting settingInfo ${settingInfo.toString()}" }
                uiState.value = uiState.value?.apply {
                    deviceSettingInfo = settingInfo
                }
            } else {
            }
        }
    }

    fun loadLedState(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(switchLed(deviceId, IPC_SCHEME_DP_AUTHORITY_R, false).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadLedState code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var ledOn = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_SWITCH_LED) {
                    object : TypeToken<Map<String, Boolean>>(){}
                } ?: false
                YRLog.d { "-->> IpcSettingViewModel loadLedState state $ledOn" }
                uiState.value = uiState.value?.apply {
                    ledState = ledOn
                }
            } else {
            }
        }
    }

    fun setLedState(deviceId : String, ledOn: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(switchLed(deviceId, IPC_SCHEME_DP_AUTHORITY_W, ledOn).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setLedState code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun setAudioRecord(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendAudioRecordCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setAudioRecord code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun setRotate(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendRotateCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setRotate code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun setMotionTrack(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendMotionTrackCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setMotionTrack code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun loadEnergyMode(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendEnergyModeCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, false).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadEnergyMode code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var on = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_ENERGY_SAVING_MODE) {
                    object : TypeToken<Map<String, Boolean>>(){}
                } ?: false
                YRLog.d { "-->> IpcSettingViewModel loadEnergyMode state $on" }
                uiState.value = uiState.value?.apply {
                    energyModeState = on
                }
            } else {
            }
        }
    }

    fun setEnergyMode(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendEnergyModeCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setEnergyMode code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun loadWaterMark(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendWaterMarkCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, false).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadLedState code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var on = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_WATER_MARK) {
                    object : TypeToken<Map<String, Boolean>>(){}
                } ?: false
                YRLog.d { "-->> IpcSettingViewModel loadWaterMark state $on" }
                uiState.value = uiState.value?.apply {
                    waterMarkState = on
                }
            } else {
            }
        }
    }

    fun setWaterMark(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendWaterMarkCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setWaterMark code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun setNightVisionIR(deviceId : String, mode: Int) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendNightVisionIRCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, mode).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setNightVisionIR code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun setNightVisionLight(deviceId : String, mode: Int) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendNightVisionLightCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, mode).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setNightVisionLight code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun loadDetectionInfo(dtType: Int, deviceId: String) {
        viewModelScope.launch {
            var detectionInfoResult: IpcSchemeResult? = null
            var dpId = ""
            when (dtType) {
                DETECTION_TYPE_MOTION -> {
                    detectionInfoResult = JsonConvertUtil.convertData(sendMotionDetectionInfo(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_MOTION_DETECTION_INFO
                }
                DETECTION_TYPE_SOUND -> {
                    detectionInfoResult = JsonConvertUtil.convertData(sendSoundDetectionInfo(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_SOUND_DETECTION_INFO
                }
                DETECTION_TYPE_HUMAN -> {
                    detectionInfoResult = JsonConvertUtil.convertData(sendHumanDetectionInfo(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_PIR_DETECTION_INFO
                }
            }
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadDetectionInfo detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var detectInfo : DetectionInfo? = parseIpcDPCmdResult(detectionInfoResult?.result, dpId) {
                    object : TypeToken<Map<String, DetectionInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadDetectionInfo detectionInfo ${detectInfo.toString()}" }
                uiState.value = uiState.value?.apply {
                    detectionInfo = detectInfo
                }
            } else {
            }
        }
    }

    fun loadDetectionPlanInfo(dtType: Int, deviceId: String) {
        viewModelScope.launch {
            var detectionPlanInfoResult: IpcSchemeResult? = null
            var dpId = ""
            when (dtType) {
                DETECTION_TYPE_MOTION -> {
                    detectionPlanInfoResult = JsonConvertUtil.convertData(sendMotionDetectionPlan(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_MOTION_DETECTION_PLAN
                }
                DETECTION_TYPE_SOUND -> {
                    detectionPlanInfoResult = JsonConvertUtil.convertData(sendSoundDetectionPlan(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_SOUND_DETECTION_PLAN
                }
                DETECTION_TYPE_HUMAN -> {
                    detectionPlanInfoResult = JsonConvertUtil.convertData(sendHumanDetectionPlan(deviceId).single(), IpcSchemeResult::class.java)
                    dpId = IPC_SCHEME_DP_PIR_DETECTION_PLAN
                }
            }
            var detectionInfoCode = detectionPlanInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadDetectionPlanInfo detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var planInfo : DetectionPlanInfo? = parseIpcDPCmdResult(detectionPlanInfoResult?.result, dpId) {
                    object : TypeToken<Map<String, DetectionPlanInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadDetectionPlanInfo detectionInfo ${planInfo.toString()}" }
                uiState.value = uiState.value?.apply {
                    detectionPlanInfo = planInfo
                }
                detectionSchedules.value = planInfo?.planSchedules.orEmpty()
            } else {
            }
        }
    }

    fun loadPirDetectionPlanInfo(deviceId: String) {
        runScopeTask<DeviceUploadConfigure, Any>(viewModelScope,
            flowBlock = {
                DeviceManagerRepository.getDeviceUploadConfigure(deviceId)
            },
            resultBlock = {
                var detectionPlanSchedules = DeviceControlHelper.convertDetectionSchedule(it?.PIRPlanList)
                uiState.value = uiState.value?.apply {
                    detectionPlanInfo = DetectionPlanInfo().apply {
                        planSchedules = detectionPlanSchedules
                    }
                }
                detectionSchedules.value = detectionPlanSchedules.orEmpty()
                null
            }
        )
    }

    fun switchDetection(dtType: Int, deviceId: String, enable: Boolean) {
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_SWITCH
            }
            when (dtType) {
                DETECTION_TYPE_MOTION -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_MOTION
                    }
                }
                DETECTION_TYPE_SOUND -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_SOUND
                    }
                }
                DETECTION_TYPE_HUMAN -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    }
                }
            }
            detectionParam.info?.enable = enable
            var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel switchDetection detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                uiState.value = uiState.value?.apply {
                    detectionInfo?.apply {
                        this.enable = enable
                    }
                }
            } else {
            }
            loadDetectionInfo(dtType, deviceId)
        }
    }

    fun setDetectionSensitivity(dtType: Int, deviceId: String, level: Int) {
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_SENSITIVITY
            }
            when (dtType) {
                DETECTION_TYPE_MOTION -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_MOTION
                    }
                }
                DETECTION_TYPE_SOUND -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_SOUND
                    }
                }
                DETECTION_TYPE_HUMAN -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    }
                }
            }
            detectionParam.info?.level = level
            var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel switchDetectionSensitivity detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                uiState.value = uiState.value?.apply {
                    detectionInfo?.apply {
                        this.level = level
                    }
                }
            } else {
            }
            loadDetectionInfo(dtType, deviceId)
        }
    }

    fun setDetectionAlarm(deviceId: String, enable: Boolean) {
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_ALARM
                info = DetectionInfo().apply {
                    detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    alarmOn = enable
                }
            }
            var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setFlashLightMode detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                uiState.value = uiState.value?.apply {
                    detectionInfo?.apply {
                        this.alarmOn = enable
                    }
                }
            } else {
            }
        }
    }

    fun setDetectionPlan(dtType: Int, deviceId: String, planSchedule: DetectionPlanSchedule) {
        if (dtType == DETECTION_TYPE_HUMAN) {
            setPirDetectionPlan(deviceId, planSchedule)
            return
        }
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN
            }
            when (dtType) {
                DETECTION_TYPE_MOTION -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_MOTION
                    }
                }
                DETECTION_TYPE_SOUND -> {
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_SOUND
                    }
                }
            }
            detectionParam.info?.planInfo = DetectionPlanInfo().apply {
                planSchedules = mutableListOf(planSchedule)
            }
            var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setDetectionPlan detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                //uiState.value = uiState.value?.apply {}
            } else {
            }
            loadDetectionPlanInfo(dtType, deviceId)
        }
    }

    fun setFlashLightMode(deviceId: String, lightMode: Int) {
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_LIGHT
                info = DetectionInfo().apply {
                    detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    lightInfo = LightInfo().apply {
                        mode = lightMode
                    }
                }
            }
            var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setFlashLightMode detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                uiState.value = uiState.value?.apply {
                    detectionInfo?.apply {
                        this.enable = enable
                    }
                }
            } else {
            }
        }
    }

    fun loadDetectionAreaInfo(deviceId: String) {
        viewModelScope.launch {
            var result: IpcSchemeResult? = JsonConvertUtil.convertData(sendDetectionAreaInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadDetectionAreaInfo code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var areaInfo : DetectionAreaInfo? = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_DETECTION_AREA_INFO) {
                    object : TypeToken<Map<String, DetectionAreaInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadDetectionAreaInfo info ${areaInfo.toString()}" }
                uiState.value = uiState.value?.apply {
                    detectionAreaInfo = areaInfo
                }
            } else {
            }
        }
    }

    fun setDetectionAreaInfo(operationType: Int, deviceId : String, on: Boolean = false, areaRect: List<Int>? = null) {

        viewModelScope.launch {
            var resultOfInfo = JsonConvertUtil.convertData(sendDetectionAreaInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var info: DetectionAreaInfo? = null
            YRLog.d { "-->> IpcSettingViewModel setDetectionAreaInfo loadInfo code ${resultOfInfo?.code}" }
            if (resultOfInfo?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                info = parseIpcDPCmdResult(resultOfInfo?.result, IPC_SCHEME_DP_DETECTION_AREA_INFO) {
                    object : TypeToken<Map<String, DetectionAreaInfo>>(){}
                }
                if (info != null) {
                    when (operationType) {
                        DETECTION_AREA_OPERATION_TYPE_SWITCH -> {
                            info?.apply {
                                enable = on
                            }
                        }
                        DETECTION_AREA_OPERATION_TYPE_CONFIGURE -> {
                            info?.apply {
                            }
                        }
                    }
                    var result = JsonConvertUtil.convertData(sendDetectionAreaInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, info).single(), IpcSchemeResult::class.java)
                    var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                    YRLog.d { "-->> IpcSettingViewModel setDetectionAreaInfo code $code" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        when (operationType) {
                            DETECTION_AREA_OPERATION_TYPE_SWITCH -> {
                                uiState.value?.detectionAreaInfo = uiState.value?.detectionAreaInfo?.apply {
                                    enable = on
                                }
                            }
                            DETECTION_AREA_OPERATION_TYPE_CONFIGURE -> {
                            }
                        }
                    } else {
                    }
                } else {
                }
            } else {
            }
        }

    }

    fun loadStorageInfo(deviceId: String) {
        viewModelScope.launch {
            var result: IpcSchemeResult? = JsonConvertUtil.convertData(sendStorageInfoCmd(deviceId).single(), IpcSchemeResult::class.java)
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadStorageInfo code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info : StorageInfo? = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_INFO) {
                    object : TypeToken<Map<String, StorageInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadStorageInfo info ${info.toString()}" }
                uiState.value = uiState.value?.apply {
                    storageInfo = info
                }
            } else {
            }
        }
    }

    fun startFormatStorage(deviceId: String) {
        viewModelScope.launch {
            var result: IpcSchemeResult? = JsonConvertUtil.convertData(sendFormatStorageCmd(deviceId).single(), IpcSchemeResult::class.java)
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel startFormatStorage code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }
    }

    fun loadLoopRecord(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendLoopRecordCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, false).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadLoopRecord code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var on = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_LOOP_RECORD) {
                    object : TypeToken<Map<String, Boolean>>(){}
                } ?: false
                YRLog.d { "-->> IpcSettingViewModel loadLoopRecord state $on" }
                uiState.value = uiState.value?.apply {
                    loopRecordState = on
                }
            } else {
            }
        }
    }

    fun switchLoopRecord(deviceId : String, on: Boolean) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendLoopRecordCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, on).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel switchLoopRecord code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }

    }

    fun loadMediaModeInfo(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendMediaModeInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadMediaModeInfo code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_MEDIA_MODE_INFO) {
                    object : TypeToken<Map<String, MediaModeInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadMediaModeInfo info ${info?.toString()}" }
                uiState.value = uiState.value?.apply {
                    mediaModeInfo = info
                }
            } else {
            }
        }

    }

    fun setMediaModeInfo(operationType: Int, deviceId : String, mediaMode: Int = 0, imageNum: Int = 0, recordDuration: Int = 0) {

        viewModelScope.launch {
            var resultOfInfo = JsonConvertUtil.convertData(sendMediaModeInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var info: MediaModeInfo? = null
            YRLog.d { "-->> IpcSettingViewModel setMediaModeInfo loadInfo code ${resultOfInfo?.code}" }
            if (resultOfInfo?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                info = parseIpcDPCmdResult(resultOfInfo?.result, IPC_SCHEME_DP_MEDIA_MODE_INFO) {
                    object : TypeToken<Map<String, MediaModeInfo>>(){}
                }
                if (info != null) {
                    when (operationType) {
                        MEDIA_MODE_OPERATION_TYPE_MODE -> {
                            info?.apply {
                                mode = mediaMode
                            }
                        }
                        MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM -> {
                            info?.apply {
                                picNum = imageNum
                            }
                        }
                        MEDIA_MODE_OPERATION_TYPE_DURATION -> {
                            info?.apply {
                                vidDur = recordDuration
                            }
                        }
                    }
                    var result = JsonConvertUtil.convertData(sendMediaModeInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, info).single(), IpcSchemeResult::class.java)
                    var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                    YRLog.d { "-->> IpcSettingViewModel setMediaModeInfo code $code" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                    } else {
                    }
                } else {
                }
            } else {
            }
        }

    }

    fun loadNetSpotInfo(deviceId : String) {

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendNetSpotInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadNetSpotInfo code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_NET_SPOT_INFO) {
                    object : TypeToken<Map<String, NetSpotInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel loadNetSpotInfo info ${info?.toString()}" }
                uiState.value = uiState.value?.apply {
                    netSpotInfo = info
                }
            } else {
            }
        }

    }

    fun setNetSpotPw(deviceId : String, oldPw: String, pw: String, loadingState: MutableLiveData<ScopeTaskResult<Int>>? = null) {

        runScopeTask<Int, Int>(viewModelScope, loadingState, flowBlock = {
            var settingResultCode: Int = 0
            var resultOfNetSpotInfo = JsonConvertUtil.convertData(sendNetSpotInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var netSpotInfo: NetSpotInfo? = null
            YRLog.d { "-->> IpcSettingViewModel setNetSpotPw loadNetSpotInfo code ${resultOfNetSpotInfo?.code}" }
            if (resultOfNetSpotInfo?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                netSpotInfo = parseIpcDPCmdResult(resultOfNetSpotInfo?.result, IPC_SCHEME_DP_NET_SPOT_INFO) {
                    object : TypeToken<Map<String, NetSpotInfo>>(){}
                }
                if (!(netSpotInfo?.ssid.isNullOrEmpty() || netSpotInfo?.psd.isNullOrEmpty())) {
                    if (netSpotInfo?.psd != oldPw) {
                        settingResultCode = 2
                    } else {
                        netSpotInfo?.apply {
                            psd = pw
                        }
                        var result = JsonConvertUtil.convertData(sendNetSpotInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, netSpotInfo).single(), IpcSchemeResult::class.java)
                        var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                        YRLog.d { "-->> IpcSettingViewModel setNetSpotPw code $code" }
                        if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                            settingResultCode = 1
                        } else {
                            settingResultCode = 0
                        }
                    }
                } else {
                    settingResultCode = 0
                }
            } else {
                settingResultCode = 0
            }

            flow {
                emit(settingResultCode)
            }
        }, resultBlock = {
            val isSuccess: Boolean = it == 1
            ScopeTaskResult<Int>().apply {
                state = if (isSuccess) { SCOPE_TASK_STATE_RESULT_SUCCESS } else { SCOPE_TASK_STATE_RESULT_ERROR }
                data = it
            }
        })

        viewModelScope.launch {
            var resultOfNetSpotInfo = JsonConvertUtil.convertData(sendNetSpotInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_R, null).single(), IpcSchemeResult::class.java)
            var netSpotInfo: NetSpotInfo? = null
            YRLog.d { "-->> IpcSettingViewModel setNetSpotPw loadNetSpotInfo code ${resultOfNetSpotInfo?.code}" }
            if (resultOfNetSpotInfo?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                netSpotInfo = parseIpcDPCmdResult(resultOfNetSpotInfo?.result, IPC_SCHEME_DP_NET_SPOT_INFO) {
                    object : TypeToken<Map<String, NetSpotInfo>>(){}
                }
                if (!(netSpotInfo?.ssid.isNullOrEmpty() || netSpotInfo?.psd.isNullOrEmpty())) {
                    netSpotInfo?.apply {
                        psd = pw
                    }
                    var result = JsonConvertUtil.convertData(sendNetSpotInfoCmd(deviceId, IPC_SCHEME_DP_AUTHORITY_W, netSpotInfo).single(), IpcSchemeResult::class.java)
                    var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                    YRLog.d { "-->> IpcSettingViewModel setNetSpotPw code $code" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                    } else {
                    }
                } else {
                }
            } else {
            }
        }

    }

    fun startUpgrade(deviceId: String, model: String, version: String, pkt: String, md5: String) {
        runScopeTask<IpcSchemeRetryCmdResult<IpcSchemeResult>, Any>(viewModelScope,
            flowBlock = {
                sendUpgradeCmd(deviceId, model, version, pkt, md5)
            },
            resultBlock = {
                YRLog.d { "-->> startUpgrade isSuccess ${it?.isSuccess} code ${it?.data?.code} result ${it?.data?.result}" }
                val isSuccess = it?.data?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS
                if (isSuccess) {
                    upgradeState.value = DeviceDefine.UPGRADE_TYPE_DOWNLOADING
                    checkUpgradeState(deviceId, true)
                } else {
                }
                null
            }
        );
    }

    fun startSyncTime(deviceId: String) {
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(syncTime(deviceId).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel startResetDevice code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
            } else {
            }
        }
    }

    fun startResetDevice(deviceId: String) {
        viewModelScope.launch {
            var response = DeviceManagerRepository.updateDeviceUploadConfigure(deviceId, JsonConvertUtil.convertToJson(DeviceUploadConfigure()).orEmpty())
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            if (response.code == HttpCode.SUCCESS_CODE) {
                var result = JsonConvertUtil.convertData(resetDevice(deviceId).single(), IpcSchemeResult::class.java)
                var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                YRLog.d { "-->> IpcSettingViewModel startResetDevice code $code" }
            } else {
            }
        }
    }

    fun startRemoveDevice(deviceId: String, userId: String, parentDeviceId: String, online: Boolean, isGatewayDevice: Boolean, loadingState: MutableLiveData<ScopeTaskResult<Any>>? = null) {
        runScopeTask(viewModelScope, loadingState, flowBlock = {
            if (online) {
                var result = JsonConvertUtil.convertData(sendUnbindDevice(deviceId, userId, parentDeviceId).single(), IpcSchemeResult::class.java)
                var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                YRLog.d { "-->> startRemoveDevice code $code" }
                val isSuccess = code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS
                var isRetryChecking = true
                flow<Boolean> {
                    var response = DeviceManagerRepository.getRemoteDeviceInfo(deviceId)
                        .single()
                    isRetryChecking = response.code == HttpCode.SUCCESS_CODE
                    if (isRetryChecking) {
                        throw RuntimeException("Retry")
                    } else {
                        emit(true)
                    }
                }
                    .retry(3) {
                        delay(3 * 1000)
                        isRetryChecking
                    }
                    .catch { e ->
                        emit(false)
                    }
            } else {
                flow {
                    val isRemoveSuccess = removeDeviceForServer(userId, deviceId, isGatewayDevice)
                    emit(isRemoveSuccess)
                }.catch {
                    emit(false)
                }
            }

        }, resultBlock = {
            val isSuccess = it ?: false
            YRLog.d { "-->> startRemoveDevice result $it" }
            ScopeTaskResult<Any>().apply {
                state = if (isSuccess) { SCOPE_TASK_STATE_RESULT_SUCCESS } else { SCOPE_TASK_STATE_RESULT_ERROR }
            }
        })
    }

    fun updateDeviceNotice(deviceId: String, noticeOn: Boolean) {
        viewModelScope.launch {
            val isNotice = if (noticeOn) DeviceDefine.NOTICE_ON else DeviceDefine.NOTICE_OFF
            val responseSuccess = DeviceManagerRepository.updateDeviceNotice(deviceId, isNotice)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
                .let {
                    it.code == HttpCode.SUCCESS_CODE
                }
        }
    }

    fun loadPackageInfo(deviceId: String, isOwner: Boolean) {
        viewModelScope.launch {
            DeviceManagerRepository.getPackageInfo(deviceId, isOwner)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .collect {
                    val responseSuccess = it.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        uiState.value = uiState.value?.apply {
                            cloudInfo = it.data
                        }
                    }
                }
        }
    }

    fun startUnsubscribe(deviceId: String) {
        viewModelScope.launch {
            var response = DeviceManagerRepository.cancelOrder(deviceId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            val responseSuccess = response.code == HttpCode.SUCCESS_CODE
            if (responseSuccess) {
                loadPackageInfo(deviceId, true)
            } else {
            }
        }
    }

    fun startShareDevice(deviceId: String, shares: List<String>) {
        viewModelScope.launch {
            var responses = DeviceManagerRepository.shareDevice(deviceId, shares)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            val failResponses = responses.filterNot {
                YRLog.d { "->> IpcSettingViewModel startShareDevice account ${it.key} code ${it.value?.code}" }
                it.value?.code == HttpCode.SUCCESS_CODE
            }
        }
    }

    fun loadDeviceRelation(deviceId: String) {
        viewModelScope.launch {
            var response = DeviceManagerRepository.getDeviceRelation(deviceId, 1, 100)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            var responseSuccess = response.code == HttpCode.SUCCESS_CODE
            if (responseSuccess) {
                var owner = response.data?.data?.filter {
                    it.type == DeviceDefine.BIND_TYPE_OWNER
                }.orEmpty()

                var sharers = response.data?.data?.filter {
                    it.type != DeviceDefine.BIND_TYPE_OWNER
                }.orEmpty()

                var binders = mutableListOf<DeviceRelation>()
                binders.addAll(owner)
                binders.addAll(sharers)
                deviceRelationState.value = binders
            }
        }
    }

    fun startRemoveShares(deviceId: String, id: Int) {
        viewModelScope.launch {
            var response = DeviceManagerRepository.deleteSharing(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            val responseSuccess = response.code == HttpCode.SUCCESS_CODE
            if (responseSuccess) {
                loadDeviceRelation(deviceId)
            } else {
            }
        }
    }

    fun loadUpgradeInfo(deviceId: String, model: String) {
        runScopeTask<FirmwareVersion, Any>(viewModelScope,
            flowBlock = {
                DeviceManagerRepository.getUpgradeDetailInfo(deviceId, model)
            },
            resultBlock = {
                val isSuccess = it?.currentVersionCode?.isNotEmpty() ?: false
                if (isSuccess) {
                    upgradeInfoState.value = it
                }
                ScopeTaskResult<Any>().apply {
                    state = if (isSuccess) { SCOPE_TASK_STATE_RESULT_SUCCESS } else { SCOPE_TASK_STATE_RESULT_ERROR }
                }
            }
        )
    }

    fun checkUpgradeState(deviceId: String, upgrading: Boolean) {
        var isRetryChecking = true
        viewModelScope.launch {
            flow<YRApiResponse<DeviceUpgradeStatusResult>?> {
                while (isRetryChecking) {
                    var response = DeviceManagerRepository.getUpgradeStatus(deviceId)
                        .catch { e ->
                            YRLog.d { e }
                            emit(null)
                        }
                        .single()
                    emit(response)
                    delay(3 * 1000)
                }
            }
                .flowOn(Dispatchers.IO)
                .collect {
                    YRLog.d { "->> IpcSettingViewModel checkUpgradeState code ${it?.code} type ${it?.data?.type}" }
                    var responseSuccess = it?.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        var state = it?.data?.type ?: DeviceDefine.UPGRADE_TYPE_UNKNOWN
                        updateUpgradeState(state, upgrading)
                        isRetryChecking = if (upgrading) {
                            !(it?.data?.type == DeviceDefine.UPGRADE_TYPE_FINISH || it?.data?.type == DeviceDefine.UPGRADE_TYPE_FAIL)
                        } else {
                            !(it?.data?.type == DeviceDefine.UPGRADE_TYPE_NORMAL || it?.data?.type == DeviceDefine.UPGRADE_TYPE_FINISH || it?.data?.type == DeviceDefine.UPGRADE_TYPE_FAIL)
                        }
                    }
                }
        }
    }

    fun startUpdateDeviceName(deviceId: String, name: String, loadingState: MutableLiveData<ScopeTaskResult<Any>>? = null) {
        runScopeTask<YRApiResponse<Any>, Any>(viewModelScope, loadingState = loadingState,
            flowBlock = {
                DeviceManagerRepository.updateDeviceName(deviceId, name)
            },
            resultBlock = {
                val isSuccess = it?.code == HttpCode.SUCCESS_CODE
                ScopeTaskResult<Any>().apply {
                    state = if (isSuccess) { SCOPE_TASK_STATE_RESULT_SUCCESS } else { SCOPE_TASK_STATE_RESULT_ERROR }
                }
            }
        )
    }

    private fun updateUpgradeState(state: Int, upgrading: Boolean) {
        if (upgrading && (state == DeviceDefine.UPGRADE_TYPE_NORMAL || state == DeviceDefine.UPGRADE_TYPE_UNKNOWN)) {
            return
        }
        upgradeState.value = state
    }

    private fun setPirDetectionPlan(deviceId: String, planSchedule: DetectionPlanSchedule) {
        runScopeTask<DeviceUploadConfigure, Any>(viewModelScope,
            flowBlock = {
                var uploadConfigure = DeviceManagerRepository.getDeviceUploadConfigure(deviceId).single() ?: DeviceUploadConfigure()
                uploadConfigure.PIRPlanList = DeviceControlHelper.convertPIRPlanConfigures(planSchedule, uploadConfigure?.PIRPlanList)
                val uploadSuccess = DeviceManagerRepository.updateDeviceUploadConfigure(deviceId, JsonConvertUtil.convertToJson(uploadConfigure).orEmpty()).single().code == HttpCode.SUCCESS_CODE
                if (uploadSuccess) {
                    var detectionParam = DetectionParam().apply {
                        operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN
                    }
                    detectionParam.info = DetectionInfo().apply {
                        detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    }
                    detectionParam.info?.planInfo = DetectionPlanInfo().apply {
                        planSchedules = DeviceControlHelper.convertDetectionSchedule(uploadConfigure.PIRPlanList)
                    }
                    var detectionInfoResult = JsonConvertUtil.convertData(sendDetectionInfoCmd(deviceId, detectionParam).single(), IpcSchemeResult::class.java)
                    var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
                }
                flow<DeviceUploadConfigure?> {
                    if (uploadSuccess) {
                        emit(uploadConfigure)
                    } else {
                        emit(null)
                    }
                }
            },
            resultBlock = {
                var detectionPlanSchedules = DeviceControlHelper.convertDetectionSchedule(it?.PIRPlanList)
                uiState.value = uiState.value?.apply {
                    detectionPlanInfo = DetectionPlanInfo().apply {
                        planSchedules = detectionPlanSchedules
                    }
                }
                detectionSchedules.value = detectionPlanSchedules.orEmpty()
                null
            }
        )
    }

    private suspend fun removeDeviceForServer(uid: String, deviceId: String, isGatewayDevice: Boolean) : Boolean {
        if (isGatewayDevice) {
            val isResSuccess = DeviceManagerRepository.deleteRemoteGatewayDevice(deviceId).single()?.let {
                it.code == HttpCode.SUCCESS_CODE
            }
            if (isResSuccess) {
                DeviceManagerDbHelper.deleteIpcDevice(uid, deviceId)
            }
            return isResSuccess
        } else {
            val isResSuccess = DeviceManagerRepository.deleteRemoteDevice(deviceId).single()?.let {
                it.code == HttpCode.SUCCESS_CODE
            }
            if (isResSuccess) {
                DeviceManagerDbHelper.deleteIpcDevice(uid, deviceId)
            }
            return isResSuccess
        }
    }

    private fun checkDeviceRemovedAfterUnbinding(deviceId: String, block: ((Boolean) -> Unit)? = null) {
        var isRetryChecking = true
        viewModelScope.launch {
            flow<Boolean> {
                var response = DeviceManagerRepository.getRemoteDeviceInfo(deviceId)
                    .single()
                isRetryChecking = response.code == HttpCode.SUCCESS_CODE
                if (isRetryChecking) {
                    throw RuntimeException("Retry")
                } else {
                    emit(true)
                }
            }
                .retry(3) {
                    delay(3 * 1000)
                    isRetryChecking
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    block?.invoke(true)
                }
                .collect {
                    YRLog.d { "->> IpcSettingViewModel checkDeviceRemovedAfterUnbinding result $it" }
                    block?.invoke(it)
                }
        }
    }

    fun testNetSpotTcp() {
        startNetSpotTcp("Victure_a09f10c5ec5b") { result ->
            YRLog.d { "-->> debug NetSpotTcp" }
        }
    }

    fun testSendCmd(deviceId: String) {
        /*
        viewModelScope.launch {
            flow<Boolean> {
                delay(5000)
                emit(true)
            }
                .flowOn(Dispatchers.IO)
                .collect {

                    ipcSchemeCore()?.sendCmd(DeviceControlHelper.createDpCmd("102", "55ffc4135dcc4fb1029a2c98e8096c22", "", null), object :
                        IIpcSchemeResultCallback {
                        override fun onSuccess(code: Int, result: String?) {
                            YRLog.d { "-->> ActionCameraListViewModel sendCmd code $code result $result" }
                        }

                        override fun onError(code: Int, error: String?) {
                            YRLog.d { "-->> ActionCameraListViewModel sendCmd code $code error $error" }
                        }
                    })
                }
        }

         */

        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendStorageRecordVideoDateCmd(deviceId).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel StorageRecordDateInfo code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_DATE) {
                    object : TypeToken<Map<String, StorageRecordDateInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel StorageRecordDateInfo info ${info?.toString()}" }

            } else {
            }
        }

        /*
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendStorageRecordVideoInfoCmd(deviceId, 1641254400).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel StorageRecordVideoInfo code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO) {
                    object : TypeToken<Map<String, StorageRecordVideoInfo>>(){}
                }
                YRLog.d { "-->> IpcSettingViewModel StorageRecordVideoInfo info ${info?.toString()}" }

            } else {
            }
        }

         */
    }
}