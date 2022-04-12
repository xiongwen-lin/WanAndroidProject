package com.apemans.smartipcimpl.ui.ipcdevices.player.model

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcapi.webapi.DeviceUpgradeStatusResult
import com.apemans.smartipcapi.webapi.DeviceUploadConfigure
import com.apemans.smartipcapi.webapi.PresetPointConfigure
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcchipproxy.scheme.util.parseIpcDPCmdResult
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.INTENT_KEY_DATA_PARAM_1
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.ui.actioncameralist.IpcControlViewModel
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType

import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayFunctionHelper

import com.dylanc.longan.application
import com.google.gson.reflect.TypeToken
import com.nooie.common.bean.CConstant
import com.nooie.common.utils.configure.CountryUtil
import com.nooie.common.utils.file.FileUtil
import com.nooie.sdk.media.NooieMediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * @Author:dongbeihu
 * @Description: 播放器拍照录像等功能事件处理
 * @Date: 2021/12/2-15:27
 */
class IpcPlayFunctionModel : IpcControlViewModel() {

    /**
     * 支持录像、拍照等操作
     */
    var functionMenuList = ArrayList<FunctionMenuItem>()



    /**
     * 获取IPC支持的操作
     */
    fun getFunctionMenuList(
        mDeviceId: String?,
        mModel: String?,
        mConnectionMode: Int?,
        mDeviceType: IpcType = IpcType.IPC_1080,
        mIsOwner:Boolean,
        block: (functionList: ArrayList<FunctionMenuItem>) -> Unit
    ) {
        functionMenuList.clear()
        val takePhotoView = FunctionMenuItem(
            R.drawable.ic_viewfinder_takephoto_off,
            R.drawable.ic_viewfinder_takephoto_on,
            R.string.ipc_panel_button_screenshot,
            IPC_FUNCTION_TYPE_TAKE_PHOTO
        )
        val recordView = FunctionMenuItem(
            R.drawable.ic_viewfinder_record,
            R.drawable.ic_viewfinder_record_on,
            R.string.ipc_panel_button_record,
            IPC_FUNCTION_TYPE_RECORD
        )
        val speakView = FunctionMenuItem(
            R.drawable.ic_viewfinder_speak_off,
            R.drawable.ic_viewfinder_speak_on,
            R.string.ipc_panel_button_speak,
            IPC_FUNCTION_TYPE_SPEAK
        )
        val soundView = FunctionMenuItem(
            R.drawable.ic_viewfinder_sound_off,
            R.drawable.ic_viewfinder_sound_on,
            R.string.camera_settings_sound,
            IPC_FUNCTION_TYPE_SOUND
        )
        val alarmView = FunctionMenuItem(
            R.drawable.ic_viewfinder_alarm_off,
            -1,//横屏不展示
            R.string.ipc_panel_button_alarm,
            IPC_FUNCTION_TYPE_ALARM
        )
        val directionView = FunctionMenuItem(
            R.drawable.ic_viewfinder_preset_off,
            -1,//横屏不展示
            R.string.ipc_panel_button_direction,
            IPC_FUNCTION_TYPE_DIRECTION
        )

        /**
         * 打猎相机 新增支持图库，它的图库跟其他模块不一样
         */
        val galleryView = FunctionMenuItem(
            R.drawable.ic_viewfinder_photo_off,
            -1,//横屏不展示
            R.string.nooie_play_photo_title,
            IPC_FUNCTION_TYPE_PHOTO
        )

        val flashLightView = FunctionMenuItem(
            R.drawable.flash_light_switch_icon,
            -1,//横屏不展示
            R.string.cam_setting_flash_light,
            IPC_FUNCTION_TYPE_FLASH_LIGHT
        )


        functionMenuList.add(takePhotoView)

        if (mDeviceId != null) {
            if (checkIsSupportRecording(mDeviceId)) {
                YRLog.d("IsSupportRecording")
                functionMenuList.add(recordView)
            }
            if (checkIsSupportFlashLight(mDeviceId)) {
                YRLog.d("IsSupportFlashLight")
                functionMenuList.add(flashLightView)
            }
            //只有几部不支持讲话
//            val isHideTalkIcon = NooieDeviceHelper.mergeIpcType( mDeviceType ) === IpcType.MC120 || mDeviceType == IpcType.HC320
            if (checkIsSupportTalk(mDeviceId)) {
                YRLog.d("IsSupportTalk")
                functionMenuList.add(speakView)
            }
            if (checkIsSupportAudio(mDeviceId)){
                YRLog.d("IsSupportAudio")
                functionMenuList.add(soundView)
            }
            if (checkIsSupportAlarmState(mDeviceId) && checkIsSupportSound(mDeviceId)) {
                YRLog.d("IsSupportAlarmState")
                functionMenuList.add(alarmView)
            }
            if (IpcType.getIpcType(mModel) == IpcType.HC320) {
//                functionMenuList.add(galleryView)
            }
        }
        var isSupportV = checkIsSupportRotationDirectionUD(mDeviceId!!)
        var isSupportH = checkIsSupportRotationDirectionLR(mDeviceId!!)
        val openPresetPointIsShow =  mIsOwner && (isSupportV || isSupportH)
        if (openPresetPointIsShow){ //主人、摇头机才有
           functionMenuList.add(directionView)
        }

        block(functionMenuList)
    }

    fun checkUpgradeState(deviceId: String, block: (state: Int) -> Unit) {
        viewModelScope.launch {
            flow<YRApiResponse<DeviceUpgradeStatusResult>?> {
                var response = DeviceManagerRepository.getUpgradeStatus(deviceId)
                    .catch { e ->
                        YRLog.d { e }
                        emit(null)
                    }
                    .single()
                emit(response)
            }
                .flowOn(Dispatchers.IO)
                .collect {
                    YRLog.d { "->>  checkUpgradeState code ${it?.code} type ${it?.data?.type}" }
                    var responseSuccess = it?.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        var state = it?.data?.type ?: DeviceDefine.UPGRADE_TYPE_UNKNOWN
                        block(state)
                    }
                }
        }
    }


    /**
     * 拍照
     */
    fun takePhoto(player: NooieMediaPlayer?, mDeviceId :String?,positionView: ImageView, isLands :Boolean) {
        if ( player == null  || mDeviceId ==null){
            return
        }
        val file = FileUtil.getNooieSavedScreenShotPath(application, mDeviceId, CConstant.MEDIA_TYPE_JPEG, obtainUid())
        player?.snapShot(file)

    }


    /**
     * 已经授权后，录像
     */
    fun record(player: NooieMediaPlayer?, mDeviceId :String?,positionView: ImageView, mRecording:Boolean,isLands :Boolean) {
        if ( player == null  || mDeviceId ==null){
            return
        }
        if (mRecording) {
            positionView.setImageResource( if (isLands) R.drawable.ic_viewfinder_record_on else R.drawable.ic_viewfinder_record)
            player.stopRecord()
        } else {
            positionView.setImageResource(R.drawable.record_blue)
            player.startRecord( FileUtil.getNooieSavedRecordPath(
                    application,
                    mDeviceId,
                    CConstant.MEDIA_TYPE_MP4,
                    obtainUid()
                )
            )
        }

    }

    /**
     * 说话
     */
    fun talk(player: NooieMediaPlayer, mDeviceId :String?,positionView: ImageView,isLands :Boolean) {

        if (player.isTalking) {
//            positionView.setImageResource( if (isLands) R.drawable.ic_viewfinder_speak_on else R.drawable.ic_viewfinder_speak_off)
            player.stopTalk()
            if (player.isWaveout) {
                player.setWaveoutState(false)
                PlayFunctionHelper.changePhoneVolume(application, false)
            }
        } else {

            if (PlayFunctionHelper.getTalkGuide(mDeviceId)){
                Toast.makeText(application,R.string.device_guide_talk_des,Toast.LENGTH_SHORT).show() //某设备首次说话提示
                PlayFunctionHelper.saveTalkGuide(mDeviceId,false)
            }
//            positionView.setImageResource(R.drawable.talk__blue_land)
            player.startTalk(null)
            player.setWaveoutState(true) //打开声音图标
            PlayFunctionHelper.changePhoneVolume(application, true)
        }
    }

    /**
     * 声音控制
     */
    fun sound(player: NooieMediaPlayer, mDeviceId :String?,positionView: ImageView,isLands :Boolean) {
        if (player.isWaveout) {
            positionView.setImageResource(if (isLands) R.drawable.ic_viewfinder_mute_on else R.drawable.ic_viewfinder_mute_off)
            player.setWaveoutState(false)
            PlayFunctionHelper.changePhoneVolume(application, false)
        } else {
            positionView.setImageResource(if (isLands) R.drawable.ic_viewfinder_sound_on else R.drawable.ic_viewfinder_sound_off)
            player.setWaveoutState(true)
            PlayFunctionHelper.changePhoneVolume(application, true)
        }
        PlayFunctionHelper.saveAudioState(mDeviceId, player.isWaveout)
    }

    /**
     * 发起警报 ，没有横屏图标
     */
    fun alarm(mDeviceId: String, positionView: ImageView, isLands: Boolean) {
        if (positionView.tag != null && positionView.getTag() === ALARM_AUDIO_STATE_ON) {
            positionView.tag = ALARM_AUDIO_STATE_OFF
            positionView.setImageResource(R.drawable.ic_viewfinder_alarm_off)
            val  alarmStateInfo   = AlarmStateInfo().apply {
                this.open = 0
                this.type = 0
                this.dur = 0
                this.times = 0
            }
            setAlarmState(mDeviceId,alarmStateInfo)
        } else {
            positionView.tag = ALARM_AUDIO_STATE_ON
            positionView.setImageResource(R.drawable.alarm_blue_land)
            val  alarmStateInfo   = AlarmStateInfo().apply {
                this.open = 1
                this.type = 0
                this.dur = 60
                this.times = 1
            }
            setAlarmState(mDeviceId,alarmStateInfo)
        }
    }

    private fun setAlarmState(deviceId : String, alarmStateInfo :AlarmStateInfo) {
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(sendAlarmStateCmd(deviceId,alarmStateInfo, IPC_SCHEME_DP_AUTHORITY_W).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setAlarmState code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                YRLog.d { "-->> setAlarmState SUCCESS" }
            }
        }
    }
    /**
     * 打开图库
     */
    fun openPhoto(mDeviceId: String) {
        val param = Bundle()
        param.putString(INTENT_KEY_DEVICE_ID, mDeviceId)
        param.putFloat(INTENT_KEY_DATA_PARAM_1, CountryUtil.getCurrentTimeZone().toFloat())
//        PhotoMediaActivity.toPhotoMediaActivity(application, param)
    }

    /**
     * 打开闪光灯
     */
    fun flashLight(mDeviceId: String, positionView: ImageView, isLands: Boolean) {

        if (positionView.tag != null && IPC_SCHEME_FLASH_LIGHT_MODE_COLOR === positionView.tag) {
            positionView.tag = FLASH_LIGHT_STATE_OFF
            positionView.setImageResource(R.drawable.flash_light_switch_icon)
            setFlashLightMode(mDeviceId, false)
        } else {
            positionView.tag = IPC_SCHEME_FLASH_LIGHT_MODE_COLOR
            positionView.setImageResource(R.drawable.flash_light_switch_icon_blue_land)
            setFlashLightMode(mDeviceId, true)
        }

    }


    /**
     * 低功耗设备侦测警报设置
     */
    private fun setDetectionAlarm(deviceId: String, enable: Boolean) {
        viewModelScope.launch {
            var detectionParam = DetectionParam().apply {
                operationType = IPC_SCHEME_DETECTION_OPERATION_TYPE_ALARM
                info = DetectionInfo().apply {
                    detectionType = IPC_SCHEME_DETECTION_TYPE_PIR
                    alarmOn = enable
                }
            }
            var detectionInfoResult = JsonConvertUtil.convertData(
                sendDetectionInfoCmd(deviceId, detectionParam).single(),
                IpcSchemeResult::class.java
            )
            var detectionInfoCode = detectionInfoResult?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setFlashLightMode detectionInfoCode $detectionInfoCode" }
            if (detectionInfoCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {

            }
        }
    }


    /**
     * 设置闪光灯模式
     */
    fun setFlashLightMode(deviceId : String, isOpen :Boolean) {
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(setLight(deviceId,isOpen, IPC_SCHEME_DP_AUTHORITY_W).single(), IpcSchemeResult::class.java)
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel setAlarmState code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                YRLog.d { "-->> setAlarmState SUCCESS" }
            }
        }
    }
    /**
     * 获取侦测
     */
    fun loadDetectionPlanInfo(deviceId: String, block: (List<PresetPointConfigure>?) -> Unit) {

        viewModelScope.launch {
            DeviceManagerRepository.getDeviceUploadConfigure(deviceId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
                ?.let {
                    val list = it.PresetPointList as? List<PresetPointConfigure>
                    block(list)
                }
        }
    }

    fun  addPresetPoint(deviceId: String, position :Int,presetPointConfigure: PresetPointConfigure){
        viewModelScope.launch {
            var result: IpcSchemeResult? = JsonConvertUtil.convertData(
                sendPtzSavePos(deviceId, position).single(),
                IpcSchemeResult::class.java
            )
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadStorageInfo code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info: IpcSchemeResult? =  parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_INFO) {
                    object : TypeToken<Map<String, IpcSchemeResult>>() {}
                }

                YRLog.d { "-->> IpcSettingViewModel loadStorageInfo info ${info.toString()}" }
            }
        }
    }



     fun updateDeviceUploadConfigure(deviceId: String, directionList:List<PresetPointConfigure>,block: (state: Boolean) -> Unit){
         viewModelScope.launch {
             var uploadConfigure = DeviceManagerRepository.getDeviceUploadConfigure(deviceId).single() ?: DeviceUploadConfigure()
             uploadConfigure.PresetPointList = directionList
             val uploadSuccess = DeviceManagerRepository.updateDeviceUploadConfigure(deviceId, JsonConvertUtil.convertToJson(uploadConfigure).orEmpty()).single().code == HttpCode.SUCCESS_CODE
             block(uploadSuccess)
         }
     }

    private fun obtainUid() : String {
        return ""
    }

}