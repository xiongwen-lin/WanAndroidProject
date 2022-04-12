package com.apemans.smartipcimpl.ui.ipcdevices.player


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.quickui.alerter.alertInfo
import com.apemans.dmapi.status.DeviceDefine

import com.apemans.logger.YRLog
import com.apemans.quickui.setOnClickListener
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.databinding.DeviceActivityTplPreviewBinding
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.FunctionAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.FunctionAdapterLands
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.NooiePlayerDevicesAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.model.IpcPlayLiveModel
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.delegate.PlayState
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.helper.DeviceHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.NooieDeviceHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayFunctionHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayerFileUtils
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayerGestureListenerView
import com.dylanc.longan.isLandscape
import com.nooie.common.utils.file.FileUtil
import com.nooie.common.utils.file.MediaStoreUtil
import com.nooie.common.utils.log.NooieLog
import com.nooie.common.utils.time.DateTimeUtil
import com.nooie.common.utils.tool.TaskUtil

import com.nooie.sdk.base.Constant

import com.nooie.sdk.listener.OnPlayerListener
import com.nooie.sdk.media.NooieMediaPlayer
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.util.*


/**
 * @Author:dongbeihu
 * @Description: 实现直播页面的功能：加载预览、播放
 * @Date: 2021/12/1-14:26
 */
abstract class IpcPlayerLiveActivity : IpcPlayerFunctionActivity() {

    private var mLpCameraTaskId: String? = null

    private lateinit var playLiveModel: IpcPlayLiveModel
    var apDeviceId : String? = ""

    fun initLiveDevice() {

        playLiveModel = registerViewModule(IpcPlayLiveModel::class.java)
        var netspotInfo = playLiveModel.getNetSpotDeviceInfo()
        apDeviceId = netspotInfo?.apDeviceId

        binding.tvLive.setOnClickListener {  //切换到直播
            restartLivePlayer()
        }

    }

    fun  restartLivePlayer(){
        mIsLive = true
        initPlayerGestureListenerView()
        resetDefault()
        resumeData(false,false,false)
    }



    override fun onStop() {
        super.onStop()
        resetDefault()
    }



    /**
     * 启动直播
     * @param isQueryUpdate        查询是否新设备
     * @param isStartLpDevice     是否直接打开低功耗设备
     * @param delayStartVideo    延迟启动直播
     */
    fun resumeData(isQueryUpdate: Boolean, isStartLpDevice: Boolean, delayStartVideo: Boolean) {
       YRLog.d("-->> IpcPlayerLiveActivity resumeData 1000")

        refreshPlayerControl()
        displayFpsAndBit(true)
        if (delayStartVideo) {
           YRLog.d("-->> debug IpcPlayerLiveActivity resumeData: 1001")
            TaskUtil.delayAction(400, TaskUtil.OnDelayTimeFinishListener {
                if (isDestroyed) {
                    return@OnDelayTimeFinishListener
                }
                startVideo(isStartLpDevice)
            })
        } else {
            startVideo(isStartLpDevice)
        }
        if (isQueryUpdate && mConnectionMode != CONNECTION_MODE_AP_DIRECT) {
//         TODO   mPlayerPresenter.queryNooieDeviceUpdateStatus(mDeviceId, mUserAccount)
        }
    }

    /**
     * 判断是启动低功耗还是正常模式的直播
     */
    fun startVideo(isStartLpDevice: Boolean) {
        modelType = DeviceHelper.convertNooieModel(mDeviceType, mModel)
        val streamType = Constant.VID_STREAM_MAIN
       YRLog.d("-->> IpcPlayerLiveActivity startVideo deviceId=" + mDeviceId.toString() + " isSubDevice=" + mIsSubDevice.toString() + " isLpDevice=" + mIsLpDevice.toString() + " modelType=" + modelType.toString() + " streamType=" + streamType)
        if (mIsLpDevice) {
            startLpLive( mDeviceId,  mModel,    mConnectionMode, modelType, streamType,mIsSubDevice, isStartLpDevice)
        } else {
            startLive(mDeviceId, mModel, mConnectionMode, modelType, streamType)
        }
    }

    /**
     * 开启普通模式直播
     */
    open fun startLive(
        deviceId: String?,
        model: String?,
        connectionMode: Int?,
        modelType: Int,
        steamType: Int
    ) {
        if (player == null) {
           YRLog.d("-->> IpcPlayerLiveActivity startVideo startLive deviceId=$deviceId")
            return
        }
        updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_START)
        if (connectionMode == CONNECTION_MODE_AP_DIRECT) {
            player!!.startAPLive(apDeviceId, 0, modelType) { code ->
               YRLog.d("-->> IpcPlayerLiveActivity startVideo Live APLive onResult deviceId=$deviceId code=$code")
                updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_FINISH)
                addDeviceConnectionMark(code, deviceId)
            }
        } else {
            if (IpcType.PC420F_TYPE.equals(model, ignoreCase = true)) {
                player!!.startMhLive(deviceId, modelType, steamType) { code ->
                   YRLog.d("-->> IpcPlayerLiveActivity startVideo Live NooieLive onResult deviceId=" + mDeviceId.toString() + " code=" + code)
                    updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_FINISH)
                    addDeviceConnectionMark(code, deviceId)
                }
                return
            }
            player!!.startNooieLive(deviceId, 0, modelType, steamType) { code ->
               YRLog.d("-->> IpcPlayerLiveActivity startVideo Live NooieLive onResult deviceId=" + mDeviceId.toString() + " code=" + code)
                updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_FINISH)
                addDeviceConnectionMark(code, deviceId)
            }
        }
    }

    /**
     * 开启低功耗直播
     */
    fun startLpLive( deviceId: String?, model: String?, connectionMode: Int?, modelType: Int, streamType: Int,
        isSubDevice: Boolean, isStartLpDevice: Boolean  ) {
        if (player == null || deviceId.isNullOrEmpty() || model.isNullOrEmpty()) {
            alertInfo("startLive param error")
            return
        }
        if (!isStartLpDevice) {
            mDevicePowerMode = DEVICE_POWER_MODE_LP_SLEEP
            displayLpDeviceRestartView(true)
            return
        }

        mDevicePowerMode = DEVICE_POWER_MODE_LP_ACTIVE
        val taskId = UUID.randomUUID().toString()
       YRLog.d("-->> IpcPlayerLiveActivity startVideo LpLive taskId=$taskId")
        displayLpDeviceRestartView(false)
        mLpCameraTaskId = taskId
        updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_START)
        if (connectionMode == CONNECTION_MODE_AP_DIRECT) {
            var netspotInfo = playLiveModel.getNetSpotDeviceInfo()
            var apDeviceId = netspotInfo?.apDeviceId
            if (NooieDeviceHelper.mergeIpcType(IpcType.getIpcType(model)) === IpcType.HC320) {
                player!!.startAPP2PLive(apDeviceId, 0, modelType) { code ->
                   YRLog.d("-->> IpcPlayerLiveActivity startVideo LpLive MHAPLive onResult deviceId=$deviceId code=$code taskId=$taskId")
                    onStartLpLive(code, taskId)
                }
            } else {
                player!!.startAPLive(apDeviceId, 0, modelType) { code ->
                   YRLog.d("-->> IpcPlayerLiveActivity startVideo LpLive MHAPLive onResult deviceId=$deviceId code=$code taskId=$taskId")
                    onStartLpLive(code, taskId)
                }
            }
        } else {
            if (isSubDevice) {
                player!!.startMhLive(deviceId, modelType, streamType) { code ->
                   YRLog.d("-->> IpcPlayerLiveActivity startVideo LpLive MhLive onResult deviceId=$deviceId code=$code taskId=$taskId")
                    onStartLpLive(code, taskId)
                    addDeviceConnectionMark(code, mPDeviceId)
                }
            } else {
                player!!.startMhLive(deviceId, modelType, streamType) { code ->
                   YRLog.d("-->> IpcPlayerLiveActivity startVideo LpLive MhLive onResult deviceId=$deviceId code=$code taskId=$taskId")
                    onStartLpLive(code, taskId)
                    addDeviceConnectionMark(code, deviceId)
                }
            }
        }
    }

    /**
     * 开启低功耗直播
     */
    private fun onStartLpLive(code: Int, taskId: String) {
       YRLog.d("-->> IpcPlayerLiveActivity onStartLpLive deviceId=" + mDeviceId.toString() + " code=" + code.toString() + " taskId=" + taskId)
        if (code == Constant.OK) {

            val isShowLiveTimeDialog = mIsLive && mDevicePowerMode == DEVICE_POWER_MODE_LP_ACTIVE
//            if (isOldEcCam()) {
//                stopLpVideo()
//            }
            if (isShowLiveTimeDialog) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(LP_CAMERA_PLAY_LIMIT_TIME.toLong() * 1000) // 播放一段时间后提醒，低功耗
                    showLiveLimitTimeDialog()
                }
            }
        } else {
            mDevicePowerMode = DEVICE_POWER_MODE_NORMAL
            displayLpDeviceRestartView(false)
        }
        updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_FINISH)
    }

    /**
     * 低电耗提示是否开启播放
     */
    private fun showLiveLimitTimeDialog() {
        SmartDialog.build(supportFragmentManager)
            .setTitle(resources.getString(R.string.nooie_play_limit_time_title))
            .setContentText(resources.getString(R.string.nooie_play_limit_time_content))
            .setPositiveTextName(resources.getString(R.string.confirm))
            .setNegativeTextName(resources.getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
            }.setOnNegative {
                stopLpVideo()
                it.dismiss()
            }.show()
    }

    private fun isOldEcCam(): Boolean {
        val deviceInfo = playLiveModel.loadDeviceInfo(mDeviceId!!) as? IpcDeviceInfo
        var isOld = false
        if (deviceInfo != null) {
            isOld = NooieDeviceHelper.compareVersion(deviceInfo.version, "1.0.61") <= 0
        }
        return mDeviceType === IpcType.EC810_CAM && isOld
    }

    private fun stopLpVideo() {
        mDevicePowerMode = DEVICE_POWER_MODE_LP_SLEEP
        displayLiveTag(false)
        resetDefault()
        displayLpDeviceRestartView(true)
    }

    private fun restartVideo(delayStartVideo: Boolean) {
        if (mIsLive && mIsLpDevice) {
            resumeData(false, true, delayStartVideo)
        }
    }

//----------------------------------辅助功能------------------start------------------------------

    private fun addDeviceConnectionMark(code: Int, deviceId: String?) {
        if (isDestroyed || TextUtils.isEmpty(deviceId)) {
            return
        }
//        if (code == Constant.OK) {
//            DeviceConnectionCache.getInstance().resetCacheDataMarkNum(deviceId)
//        } else {
//            DeviceConnectionCache.getInstance().addCacheDataMarkNum(deviceId)
//        }
    }

    /**
     * 低功耗，需要点击播放
     */
    private fun displayLpDeviceRestartView(show: Boolean) {
        if (show) {
            binding.containerLpDeviceController.setVisibility(if (mIsLpDevice && mDevicePowerMode == DEVICE_POWER_MODE_LP_SLEEP) View.VISIBLE else View.GONE)
            displayFpsAndBit(!(mIsLpDevice && mDevicePowerMode == DEVICE_POWER_MODE_LP_SLEEP))
            binding.btnLpDeviceRestart.setOnClickListener {
                binding.containerLpDeviceController.visibility = View.GONE
                restartLpVideo(mModel!!, mIsSubDevice, mConnectionMode)
            }
        } else {
            binding.containerLpDeviceController.visibility = View.GONE
            displayFpsAndBit(true)
        }
    }

     fun restartLpVideo(model: String, isSubDevice: Boolean, connectionMode: Int) {

//        if (NooieDeviceHelper.isSortLinkDevice(model, isSubDevice, connectionMode)) {
//            startConnectShortLinkDevice(object : ConnectResultListener {
//                override fun onConnectResult(result: Boolean, isNewConnect: Boolean) {
//                    if (result) {
//                        restartVideo(isNewConnect)
//                    }
//                }
//            })  TODO 短链接
//        } else {
//            restartVideo(false)
//        }
         restartVideo(false)
    }



    fun displayLiveTag(show: Boolean) {
        binding.tvLive.visibility = if (show) View.VISIBLE else View.GONE
    }
}