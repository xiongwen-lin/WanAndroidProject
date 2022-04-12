package com.apemans.smartipcimpl.ui.actioncameralist

import androidx.lifecycle.viewModelScope
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.smartipcapi.info.BLEDeviceInfo
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.model.DeviceModel
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.yruibusiness.base.BaseViewModel
import com.apemans.ipccontrolapi.define.IPC_SCHEME_TYPE_SELF_DEVELOP
import com.apemans.ipccontrolapi.services.IpcControlManagerService
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcchipproxy.scheme.cache.IIpcConnectionCache
import com.apemans.ipcchipproxy.scheme.cache.IIpcSchemeDPTool
import com.apemans.ipcchipproxy.scheme.util.retrySendCmd
import com.apemans.ipcchipproxy.scheme.util.runCallbackFlow
import com.apemans.ipcchipproxy.scheme.util.subscribeCallbackFlow
import com.apemans.logger.YRLog
import com.apemans.router.routerServices
import com.apemans.smartipcapi.webapi.PackageInfoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 */
open class IpcControlViewModel : com.apemans.yruibusiness.base.BaseViewModel() {

    private val ipcControlService by routerServices<IpcControlManagerService>()

    fun ipcSchemeCore() : IIpcSchemeCore? {
        return ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)
    }

    fun getDpTool(deviceId: String) : IIpcSchemeDPTool? {
        return ipcSchemeCore()?.configureCache()?.getDpTool(deviceId)
    }

    fun getConnectionCache() : IIpcConnectionCache? {
        return ipcSchemeCore()?.connectionCache()
    }

    /**
     * 进行远程P2P连接
     * 1、更新设备配置信息IIpcSchemeCore.configureCache.updateDeviceConfigure，必须
     * 2、判断设备是否在线和是否已连接，若是则跳过
     * 3、调用IIpcSchemeCore.connect进行连接
     */
    fun startP2PConnection(deviceInfo: IpcDeviceInfo?) {
        deviceInfo?.let {
            ipcSchemeCore()
                ?.configureCache()
                ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            var connectionDeviceId: String? = if (!DeviceControlHelper.checkIsChildDevice(it.pDeviceId)) it.device_id else it.pDeviceId
            val connectable = it.online == DeviceDefine.ONLINE && !(getConnectionCache()?.checkConnectionExist(connectionDeviceId) ?: false)
            if (connectable) {
                var param = mutableMapOf<String, Any>()
                param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_P2P
                param[IPC_SCHEME_KEY_DEVICE_ID] = it.device_id
                ipcSchemeCore()?.connect(param)
            }
        }
    }

    fun startP2PConnections(deviceInfos: List<IpcDeviceInfo>?) {
        deviceInfos?.forEach {
            ipcSchemeCore()
                ?.configureCache()
                ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            var connectionDeviceId: String? = if (!DeviceControlHelper.checkIsChildDevice(it.pDeviceId)) it.device_id else it.pDeviceId
            val connectable = it.online == DeviceDefine.ONLINE && !(getConnectionCache()?.checkConnectionExist(connectionDeviceId) ?: false)
            if (connectable) {
                var param = mutableMapOf<String, Any>()
                param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_P2P
                param[IPC_SCHEME_KEY_DEVICE_ID] = it.device_id
                ipcSchemeCore()?.connect(param)
            }
        }
    }

    /**
     * 移除远程P2P连接
     */
    fun removeP2PConnection(deviceId: String) {
        var param = mutableMapOf<String, Any>()
        param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_P2P
        param[IPC_SCHEME_KEY_DEVICE_ID] = deviceId
        ipcSchemeCore()?.disconnect(param, object : IIpcSchemeResultCallback {

            override fun onSuccess(code: Int, result: String?) {
            }

            override fun onError(code: Int, error: String?) {
            }
        })
    }

    /**
     * 进行TCP协议的热点连接
     * 1、设置连接配置ConnectionConfigure，uuid默认victure_ap， apConnType为IPC_SCHEME_AP_CONN_TYPE_TCP，其余参数默认，并且非空
     * 2、调用IIpcSchemeCore.connect进行连接
     */
    fun startNetSpotTcp(ssid : String?, callback : ((Boolean) -> Unit)? = null) {
        var configure = ConnectionConfigure().apply {
            uuid = IPC_SCHEME_NET_SPOT_AP_DEVICE_ID
            apConnType = IPC_SCHEME_AP_CONN_TYPE_TCP

            serverDomain = ""
            serverPort = 0
            secret = ""
            userName = ""
        }
        var param = mutableMapOf<String, Any>()
        param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP
        param[IPC_SCHEME_KEY_CONNECTION_CONFIGURE] = JsonConvertUtil.convertToJson(configure).orEmpty()
        param[IPC_SCHEME_KEY_SSID] = ssid.orEmpty()
        ipcSchemeCore()?.connect(param, object : IIpcSchemeResultCallback {

            override fun onSuccess(code: Int, result: String?) {
                dealOnNetSpotConnected(true, callback)
            }

            override fun onError(code: Int, error: String?) {
                dealOnNetSpotConnected(false, callback)
            }

        })
    }

    /**
     * 移除TCP或P2P热点连接
     */
    fun removeNetSpot(deviceId: String? = null) {
        var param = mutableMapOf<String, Any>()
        param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP
        param[IPC_SCHEME_KEY_DEVICE_ID] = IPC_SCHEME_NET_SPOT_AP_DEVICE_ID
        ipcSchemeCore()?.disconnect(param, object : IIpcSchemeResultCallback {

            override fun onSuccess(code: Int, result: String?) {
            }

            override fun onError(code: Int, error: String?) {
            }
        })
    }

    /**
     * 进行P2P协议的热点连接
     * 1、设置连接配置ConnectionConfigure，uuid默认victure_ap， apConnType为IPC_SCHEME_AP_CONN_TYPE_P2P，
     * serverDomain为默认ip："192.168.43.1"， serverPort为默认端口23000，其余参数默认，并且非空
     * 2、调用IIpcSchemeCore.connect进行连接
     */
    fun startNetSpotP2p(ssid : String?, bleDeviceId : String?, model : String?, callback : ((Boolean) -> Unit)? = null) {
        var configure = ConnectionConfigure().apply {
            uuid = IPC_SCHEME_NET_SPOT_AP_DEVICE_ID
            apConnType = IPC_SCHEME_AP_CONN_TYPE_P2P

            serverDomain = IPC_SCHEME_NET_SPOT_SERVER_DOMAIN
            serverPort = IPC_SCHEME_NET_SPOT_SERVER_PORT
            secret = ""
            userName = ""
        }
        var param = mutableMapOf<String, Any>()
        param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_P2P
        param[IPC_SCHEME_KEY_CONNECTION_CONFIGURE] = JsonConvertUtil.convertToJson(configure).orEmpty()
        param[IPC_SCHEME_KEY_MODEL] = model.orEmpty()
        param[IPC_SCHEME_KEY_SSID] = ssid.orEmpty()
        param[IPC_SCHEME_KEY_BLE_DEVICE_ID] = bleDeviceId.orEmpty()
        ipcSchemeCore()?.connect(param, object : IIpcSchemeResultCallback {

            override fun onSuccess(code: Int, result: String?) {
                dealOnNetSpotConnected(true, callback)
            }

            override fun onError(code: Int, error: String?) {
                dealOnNetSpotConnected(false, callback)
            }

        })
    }

    fun checkIsNetSpotMode() : Boolean {
        return getConnectionCache()?.checkNetSpotConnectionExist() ?: false
    }

    fun getNetSpotDeviceInfo() : NetSpotDeviceInfo? {
        return getConnectionCache()?.getNetSpotDeviceInfo()
    }

    fun checkIsSupportLed(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_SWITCH_LED)
    }

    fun checkIsSupportAudioRecord(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_AUDIO_RECORD)
    }

    fun checkIsSupportSound(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_SOUND_STATE)
    }
    fun checkIsSupportRotate(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_ROTATE)
    }

    fun checkIsSupportSleep(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_SLEEP)
    }

    fun checkIsSupportDetectHuman(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_DETECT_HUMAN)
    }

    fun checkIsSupportNightVision(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_IR) || checkIsSupportDp(deviceId, IPC_SCHEME_DP_LIGHT_MODE)
    }

    fun checkIsSupportNightVisionWithLight(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_LIGHT_MODE)
    }

    fun checkIsSupportEnergyMode(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_ENERGY_SAVING_MODE)
    }

    fun checkIsSupportWaterMark(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_WATER_MARK)
    }

    fun checkIsSupportMotionTrack(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_MOTION_TRACK)
    }

    fun checkIsSupportFlashLight(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_FLASH_LIGHT_STATE)
    }

    fun checkIsSupportSyncTime(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_TIME_CONFIGURE)
    }

    fun checkIsSupportDetectionArea(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_DETECTION_AREA_INFO)
    }

    fun checkIsSupportFormatStorage(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_FORMAT_STORAGE)
    }

    fun checkIsSupportStorageVideo(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO)
    }

    fun checkIsSupportStorageImage(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_INFO)
    }

    fun checkIsSupportAlarmState(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_ALARM_STATE)
    }
//  警报
    suspend fun sendAlarmStateCmd(deviceId: String, alarmStateInfo :AlarmStateInfo, authority: String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_ALARM_STATE, deviceId, authority, JsonConvertUtil.convertToJson(alarmStateInfo)), it)
        }
    }

    suspend fun setLight(deviceId: String, isOn :Boolean, authority: String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_FLASH_LIGHT_STATE, deviceId, authority, isOn), it)
        }
    }

    fun checkIsSupportReset(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_RESET_DEVICE)
    }

    fun checkIsSupportPowerPoint(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_POWER_ROTATE_PRESET_POINT)
    }

    fun checkIsSupportShooting(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_MEDIA_MODE_INFO)
    }

    fun checkIsSupportChangeNetSpotPw(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_NET_SPOT_INFO)
    }

    fun checkIsSupportMotionDetect(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_MOTION_DETECTION_INFO)
    }

    fun checkIsSupportSoundDetect(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_SOUND_DETECTION_INFO)
    }

    fun checkIsSupportHumanDetect(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_PIR_DETECTION_INFO)
    }

    fun checkIsSupportSnapShot(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_SNAPSHOT)
    }

    fun checkIsSupportRecording(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_RECORDING)
    }

    fun checkIsSupportTalk(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_TALK)
    }

    fun checkIsSupportAudio(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_AUDIO)
    }

    fun checkIsSupportRotationDirectionLR(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_ROTATION_DIRECTION_LEFT_RIGHT)
    }

    fun checkIsSupportRotationDirectionUD(deviceId: String) : Boolean {
        return checkIsSupportDp(deviceId, IPC_SCHEME_DP_ROTATION_DIRECTION_UP_DOWN)
    }

    fun checkIsSupportDp(deviceId: String, dpId: String) : Boolean {
        return getDpTool(deviceId)?.isSupport(dpId) ?: false
    }

    suspend fun getIpcDeviceForNetSpot(uid : String) : DeviceModel? {
        var deviceId = getNetSpotDeviceInfo()?.deviceId
        if (deviceId.isNullOrEmpty()) {
            return null
        }
        var bleDeviceInfo = (DeviceManagerRepository.getBleIpcDevice(uid, deviceId.orEmpty()).single()?.device?.deviceInfo as? BLEDeviceInfo)
        return DeviceControlHelper.convertIpcDevice(getNetSpotDeviceInfo(), bleDeviceInfo)
    }

    suspend fun switchLed(deviceId: String, authority: String, ledOn: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SWITCH_LED, deviceId, authority, ledOn), it)
        }
    }

    suspend fun getDeviceSetting(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_DEVICE_SETTING, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendAudioRecordCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_AUDIO_RECORD, deviceId, authority, on), it)
        }
    }

    suspend fun sendRotateCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_ROTATE, deviceId, authority, on), it)
        }
    }

    suspend fun sendWaterMarkCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_WATER_MARK, deviceId, authority, on), it)
        }
    }

    suspend fun sendEnergyModeCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_ENERGY_SAVING_MODE, deviceId, authority, on), it)
        }
    }

    suspend fun sendFlashLightStateCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_FLASH_LIGHT_STATE, deviceId, authority, on), it)
        }
    }

    suspend fun sendMotionTrackCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_MOTION_TRACK, deviceId, authority, on), it)
        }
    }

    suspend fun sendNightVisionIRCmd(deviceId: String, authority: String, mode: Int) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_IR, deviceId, authority,
                    JsonConvertUtil.convertToJson(
                        LightInfo().apply {
                        this.mode = mode
                        }
                    )), it)
        }
    }

    suspend fun sendNightVisionLightCmd(deviceId: String, authority: String, mode: Int) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_LIGHT_MODE, deviceId, authority,
                    JsonConvertUtil.convertToJson(
                        LightInfo().apply {
                            this.mode = mode
                        }
                    )), it)
        }
    }

    suspend fun sendMotionDetectionInfo(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_MOTION_DETECTION_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendSoundDetectionInfo(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SOUND_DETECTION_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendHumanDetectionInfo(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_PIR_DETECTION_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendMotionDetectionPlan(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_MOTION_DETECTION_PLAN, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendSoundDetectionPlan(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SOUND_DETECTION_PLAN, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendHumanDetectionPlan(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_PIR_DETECTION_PLAN, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendDetectionInfoCmd(deviceId: String, param: DetectionParam?) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_DETECTION_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_W,
                    JsonConvertUtil.convertToJson(param)), it)
        }
    }

    suspend fun sendDetectionAreaInfoCmd(deviceId: String, authority: String, areaInfo: DetectionAreaInfo?) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_DETECTION_AREA_INFO, deviceId, authority,
                    JsonConvertUtil.convertToJson(areaInfo)), it)
        }
    }

    suspend fun sendStorageInfoCmd(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendFormatStorageCmd(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_FORMAT_STORAGE, deviceId, IPC_SCHEME_DP_AUTHORITY_W, null), it)
        }
    }

    suspend fun sendPtzSavePos(deviceId : String , position  :Int) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SAVE_ROTATE_PRESET_POINT, deviceId, IPC_SCHEME_DP_AUTHORITY_W, position), it)
        }
    }
    suspend fun sendStorageRecordVideoDateCmd(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_DATE, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendStorageRecordVideoInfoCmd(deviceId : String, start :Long) : Flow<String> {
        return subscribeCallbackFlow {
            val param = StorageRecordQueryParam().apply {
                startTime = start
            }
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, JsonConvertUtil.convertToJson(param)), it)
        }
    }

    suspend fun sendStorageRecordImageDateCmd(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_DATE, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun sendStorageRecordImageInfoCmd(deviceId : String, start :Long) : Flow<String> {
        return subscribeCallbackFlow {
            val param = StorageRecordQueryParam().apply {
                startTime = start
            }
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, JsonConvertUtil.convertToJson(param)), it)
        }
    }

    suspend fun sendLoopRecordCmd(deviceId: String, authority: String, on: Boolean) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_LOOP_RECORD, deviceId, authority, on), it)
        }
    }

    suspend fun sendMediaModeInfoCmd(deviceId: String, authority: String, info: MediaModeInfo?) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_MEDIA_MODE_INFO, deviceId, authority,
                    JsonConvertUtil.convertToJson(info)), it)
        }
    }

    suspend fun sendNetSpotInfoCmd(deviceId: String, authority: String, info: NetSpotInfo?) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(
                    IPC_SCHEME_DP_NET_SPOT_INFO, deviceId, authority,
                    JsonConvertUtil.convertToJson(info)), it)
        }
    }

    suspend fun sendUpgradeCmd(deviceId : String, model: String, version: String, pkt: String, md5: String) : Flow<IpcSchemeRetryCmdResult<IpcSchemeResult>> {
        return retrySendCmd(2, 1000) {
            var callbackResultJson = runCallbackFlow {
                var upgradeParam = UpgradeParam().apply {
                    this.model = model
                    this.version = version
                    this.pkt = pkt
                    this.md5 = md5
                }
                ipcSchemeCore()?.sendCmd(
                    DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_UPGRADE, deviceId, IPC_SCHEME_DP_AUTHORITY_W, JsonConvertUtil.convertToJson(upgradeParam)), it)
            }.single()

            var callbackResult = JsonConvertUtil.convertData(callbackResultJson, IpcSchemeResult::class.java)
            var sendCmdIsSuccess = callbackResult?.let { it.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS } ?: false
            IpcSchemeRetryCmdResult<IpcSchemeResult>().apply {
                isSuccess = sendCmdIsSuccess
                data = callbackResult
            }
        }
    }

    suspend fun syncTime(deviceId: String) : Flow<String> {
        var timeConfigureData = JsonConvertUtil.convertToJson(
            TimeConfigure().apply {
                mode = 1
                timeZone = 8.0f
                timeOffset = 28800
            }
        )
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_TIME_CONFIGURE, deviceId, data = timeConfigureData), it)
        }
    }

    suspend fun sendAlarmStateCmd(deviceId: String, open: Int, type: Int , dur: Int, times: Int) : Flow<String> {
        var timeConfigureData = JsonConvertUtil.convertToJson(
            AlarmStateInfo().apply {
                this.open = open
                this.type = type
                this.dur = dur
                this.times = times
            }
        )
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_TIME_CONFIGURE, deviceId, data = timeConfigureData), it)
        }
    }

    suspend fun sendGetSoundStateCmd(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SOUND_STATE, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), it)
        }
    }

    suspend fun resetDevice(deviceId : String) : Flow<String> {
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd<Any>(IPC_SCHEME_DP_RESET_DEVICE, deviceId), it)
        }
    }

    suspend fun sendUnbindDevice(deviceId: String, userId: String, parentDeviceId: String?) : Flow<String> {
        var unbindParam = JsonConvertUtil.convertToJson(
            UnbindParam().apply {
                uid = userId
                parentId = parentDeviceId
            }
        )
        return subscribeCallbackFlow {
            ipcSchemeCore()?.sendCmd(
                DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_UNBIND_DEVICE, deviceId, data = unbindParam), it)
        }
    }

    private fun dealOnNetSpotConnected(isSuccess : Boolean, callback : ((Boolean) -> Unit)? = null) {
        if (!isSuccess) {
            callback?.invoke(false)
            return
        }
        var netSpotConnectionIsExist = ipcSchemeCore()?.connectionCache()?.checkNetSpotConnectionExist() ?: false
        var netSpotDeviceInfo = ipcSchemeCore()?.connectionCache()?.getNetSpotDeviceInfo()
        if (!netSpotConnectionIsExist || netSpotDeviceInfo == null) {
            callback?.invoke(false)
            return
        }
        viewModelScope.launch {
            DeviceManagerRepository.updateBleIpcDevice(obtainUid(), netSpotDeviceInfo)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                    callback?.invoke(false)
                }
                .collect {
                    DeviceControlHelper.updateNetSpotDeviceConfigure(obtainUid()) {
                        callback?.invoke(it)
                    }
                }
        }
    }

    /**
     * 获取云套餐信息
     */
    fun loadPackageInfo(deviceId: String, isOwner: Boolean,block: (packageInfoResult: PackageInfoResult?) -> Unit)  {
        viewModelScope.launch {
            DeviceManagerRepository.getPackageInfo(deviceId, isOwner)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .collect {
                    val responseSuccess = it.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        block(it.data)
                    }else {
                        block(null)
                    }
                }
        }
    }

    private fun obtainUid() : String {
        return ""
    }

}