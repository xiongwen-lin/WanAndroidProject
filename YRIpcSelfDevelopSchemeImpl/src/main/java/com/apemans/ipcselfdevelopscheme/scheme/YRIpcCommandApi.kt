/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.scheme

import com.dylanc.longan.application
import com.nooie.sdk.base.Constant
import com.nooie.sdk.device.DeviceCmdService
import com.nooie.sdk.device.bean.*
import com.nooie.sdk.device.bean.hub.PirState
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcselfdevelopscheme.bean.YRIpcConfigure
import com.apemans.ipcselfdevelopscheme.bean.YRIpcDPCmdData
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM_HUB
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_HUB
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_NORMAL
import com.apemans.ipcselfdevelopscheme.util.YRIpcDeviceUtil
import com.apemans.logger.YRLog

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/16 3:32 下午
 * 说明:
 *
 * 备注: 自有Ipc方案命令接口类
 * 每个命令接口必须包含三个参数
 * YRIpcConfigure：设备配置
 * YRIpcDPCmdData：命令参数
 * IIpcSchemeResultCallback：命令回调接口
 *
 ***********************************************************/
object YRIpcCommandApi {

    /**
     * 设置led指示灯开关
     */
    fun setLed(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                var on = cmdData.data ?: false
                DeviceCmdService.getInstance(application).setLED(cmdData.deviceId, on) { code ->
                    dealCmdAction(code, on, cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockSetLed : (YRIpcConfigure?, YRIpcDPCmdData<Boolean>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var on = data.data ?: false
                DeviceCmdService.getInstance(application).hubSetLed(data.deviceId, on) { code ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setLED(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockSetLed, blockLpCamHub = blockSetLed) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getLed(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).getLED(cmdData.deviceId) { code, on ->
                    dealCmdAction(code, on, cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockGetLed : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).hubGetLed(data.deviceId) { code, on ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getLED(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockGetLed, blockLpCamHub = blockGetLed) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getSetting(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetSetting : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetInfo(data.deviceId) { code, setting ->
                    dealCmdAction(code, YRIpcDeviceUtil.convertDeviceSettingInfo(data.deviceId, setting), data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getCamAllSettingsV2(data.deviceId) { code, setting ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDeviceSettingInfo(data.deviceId, setting), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetSetting,
            blockLpHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).hubGetInfo(data.deviceId) { code, setting ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDeviceSettingInfo(data.deviceId, setting), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCamHub = blockGetSetting) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setAudioRecord(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setRecordWithAudio(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getAudioRecord(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getRecordWithAudio(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setRotate(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                var on = cmdData.data ?: false
                DeviceCmdService.getInstance(application).setRotateImg(cmdData.deviceId, on) { code ->
                    dealCmdAction(code, on, cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockSetRotate : (YRIpcConfigure?, YRIpcDPCmdData<Boolean>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var on = data.data ?: false
                DeviceCmdService.getInstance(application).camSetVidRotate(data.deviceId, on) { code ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setRotateImg(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockSetRotate, blockLpCamHub = blockSetRotate) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getRotate(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetRotate : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetVidRotate(data.deviceId) { code, on ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getRotateImg(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetRotate, blockLpCamHub = blockGetRotate) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setLoopRecord(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockSetLoopRecord : (YRIpcConfigure?, YRIpcDPCmdData<Boolean>?) -> Unit =  { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var on = data.data ?: false
                DeviceCmdService.getInstance(application).hubSetLoopRec(data.deviceId, on) { code ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setLoopRecord(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockSetLoopRecord, blockLpCamHub = blockSetLoopRecord) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getLoopRecord(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetLoopRecord : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).hubGetLoopRec(data.deviceId) { code, on ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getLoopRecord(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockGetLoopRecord, blockLpCamHub = blockGetLoopRecord) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setSleep(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setSleep(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getSleep(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getSleep(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setDetectHuman(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        var blockSetDetectHuman : (YRIpcConfigure?, YRIpcDPCmdData<Boolean>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var on = data.data ?: false
                DeviceCmdService.getInstance(application).camSetAIMode(data.deviceId, on) { code ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = blockSetDetectHuman, blockLpCamHub = blockSetDetectHuman) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getDetectHuman(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        var blockGetDetectHuman : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetAIMode(data.deviceId) { code, on ->
                    dealCmdAction(code, on, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = blockGetDetectHuman, blockLpCamHub = blockGetDetectHuman) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setDetectCry(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setDetectCry(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getDetectCry(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getDetectCry(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setWaterMark(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setWaterMark(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getWaterMark(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getWaterMark(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setEnergySavingMode(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setInfraredSavePower(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getEnergySavingMode(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getInfraredSavePower(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setFlashLightState(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setLight(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getFlashLightState(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getLight(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setMotionTrack(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<Boolean>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var on = data.data ?: false
                    DeviceCmdService.getInstance(application).setMotionTrack(data.deviceId, on) { code ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getMotionTrack(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getMotionTrack(data.deviceId) { code, on ->
                        dealCmdAction(code, on, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setIR(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockSetIR : (YRIpcConfigure?, YRIpcDPCmdData<String>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var info = JsonConvertUtil.convertData(data.data, LightInfo::class.java) ?: LightInfo().apply {
                    mode = IPC_SCHEME_LIGHT_MODE_OFF
                }
                var mode = YRIpcDeviceUtil.convertIRMode(info.mode)
                DeviceCmdService.getInstance(application).camSetIR(data.deviceId, mode) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var info = JsonConvertUtil.convertData(data.data, LightInfo::class.java) ?: LightInfo().apply {
                        mode = IPC_SCHEME_LIGHT_MODE_OFF
                    }
                    var mode = YRIpcDeviceUtil.convertIcrMode(info.mode)
                    DeviceCmdService.getInstance(application).setIcr(data.deviceId, mode) { code ->
                        dealCmdAction(code, "on", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockSetIR, blockLpCamHub = blockSetIR) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getIR(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetIR : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetIR(data.deviceId) { code, irMode ->
                    dealCmdAction(code, YRIpcDeviceUtil.convertLightInfo(irMode), data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getIcr(data.deviceId) { code, irMode ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertLightInfo(irMode), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetIR, blockLpCamHub = blockGetIR) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setLightMode(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var info = JsonConvertUtil.convertData(data.data, LightInfo::class.java)
                    var mode = info?.mode ?: IPC_SCHEME_LIGHT_MODE_OFF
                    DeviceCmdService.getInstance(application).setIcrV2(data.deviceId, mode) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getLightMode(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getIcrV2(data.deviceId) { code, mode ->
                        var info = LightInfo().apply {
                            this.mode = mode
                        }
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setTimeConfigure(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockSetTimeConfigure : (YRIpcConfigure?, YRIpcDPCmdData<String>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var configure = JsonConvertUtil.convertData(data.data, TimeConfigure::class.java)
                var mode = configure?.mode ?: 0
                var timeZone = configure?.timeZone ?: 0f
                var timeOffset = configure?.timeOffset ?: 0
                DeviceCmdService.getInstance(application).setTime(data.deviceId, mode, timeZone, timeOffset) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var configure = JsonConvertUtil.convertData(data.data, TimeConfigure::class.java)
                    var mode = configure?.mode ?: 0
                    var timeZone = configure?.timeZone ?: 0f
                    var timeOffset = configure?.timeOffset ?: 0
                    DeviceCmdService.getInstance(application).setTime(data.deviceId, mode, timeZone, timeOffset) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockSetTimeConfigure, blockLpCamHub = blockSetTimeConfigure) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getStorageInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).getFormatInfo(cmdData.deviceId) { code, info ->
                    dealCmdAction(code, YRIpcDeviceUtil.convertStorageInfo(info), cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockGetStorageInfo : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).hubSDcardStatus(data.deviceId) { code, info ->
                    dealCmdAction(code, YRIpcDeviceUtil.convertStorageInfo(info), data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getFormatInfo(data.deviceId) { code, info ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertStorageInfo(info), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetStorageInfo,blockLpHub = blockGetStorageInfo, blockLpCamHub = blockGetStorageInfo) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun formatStorage(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).formatSdCard(cmdData.deviceId) { code ->
                    dealCmdAction(code, "", cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockFormatStorage : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).hubSDcardFmt(data.deviceId) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).formatSdCard(data.deviceId) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockFormatStorage, blockLpCamHub = blockFormatStorage) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getStorageRecordVideoDate(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetStorageRecordVideoDate : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetRecDates(data.deviceId) { code, dateList, today ->
                    val info = StorageRecordDateInfo().apply {
                        this.today = today
                        recordDataList = YRIpcDeviceUtil.convertRecordDataList(dateList?.asList())
                    }
                    dealCmdAction(code, info, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getSDcardRecDay(data.deviceId) { code, dateList, today ->
                        val info = StorageRecordDateInfo().apply {
                            this.today = today
                            recordDataList = YRIpcDeviceUtil.convertRecordDataList(dateList?.asList())
                        }
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetStorageRecordVideoDate, blockLpCamHub = blockGetStorageRecordVideoDate) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getStorageRecordVideoInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockGetStorageRecordVideoInfo : (YRIpcConfigure?, YRIpcDPCmdData<String>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var queryParam = JsonConvertUtil.convertData(data.data, StorageRecordQueryParam::class.java)
                var startTime = queryParam?.startTime ?: 0
                DeviceCmdService.getInstance(application).camGetRecList(data.deviceId, startTime.toInt()) { code, items ->
                    val info = YRIpcDeviceUtil.convertRecordVideoInfo(items?.asList())
                    dealCmdAction(code, info, data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var queryParam = JsonConvertUtil.convertData(data.data, StorageRecordQueryParam::class.java)
                    var startTime = queryParam?.startTime ?: 0
                    DeviceCmdService.getInstance(application).getSDcardRecordList(data.deviceId, startTime) { code, items ->
                        val info = YRIpcDeviceUtil.convertRecordVideoInfo(items?.asList())
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockGetStorageRecordVideoInfo, blockLpCamHub = blockGetStorageRecordVideoInfo) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getStorageRecordImageDate(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getImgsDates(data.deviceId) { code, dateList, today ->
                        val info = StorageRecordDateInfo().apply {
                            this.today = today
                            recordDataList = YRIpcDeviceUtil.convertRecordDataList(dateList?.asList())
                        }
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getStorageRecordImageInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var queryParam = JsonConvertUtil.convertData(data.data, StorageRecordQueryParam::class.java)
                    var startTime = queryParam?.startTime ?: 0
                    DeviceCmdService.getInstance(application).getImgLists(data.deviceId, startTime.toInt(), 0, 1) { code, items ->
                        val info = YRIpcDeviceUtil.convertRecordImageInfo(items?.asList())
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getSoundState(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getSpeakerInfo(data.deviceId) { code, info ->
                        var state : SoundStateInfo? = info?.let {
                            SoundStateInfo().apply {
                                alarmOn = it.alarmSoundOpen
                                talkOn = it.talkSoundOpen
                            }
                        }
                        dealCmdAction(code, state, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setAlarmState(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var alarmRequest  = JsonConvertUtil.convertData(data.data, AlarmStateInfo::class.java)?.let {
                        AlarmSoundRequest().apply {
                            open = it.open
                            type = it.type
                            dur = it.dur
                            times = it.times
                        }
                    } ?: AlarmSoundRequest()
                    DeviceCmdService.getInstance(application).setAlarmSound(data.deviceId, alarmRequest) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setDetectionAreaInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var areaInfo  = YRIpcDeviceUtil.convertMtAreaInfo(JsonConvertUtil.convertData(data.data, DetectionAreaInfo::class.java))
                    if (areaInfo == null) {
                        callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                    } else {
                        DeviceCmdService.getInstance(application).setMTAreaInfo(data.deviceId, areaInfo) { code ->
                            dealCmdAction(code, "", data, callback) {
                                it
                            }
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getDetectionAreaInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getMTAreaInfo(data.deviceId) { code, info ->
                        var state : DetectionAreaInfo? = YRIpcDeviceUtil.convertDetectionAreaInfo(info)
                        dealCmdAction(code, state, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun upgrade(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        var blockUpgrade : (YRIpcConfigure?, YRIpcDPCmdData<String>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var param = JsonConvertUtil.convertData(data.data, UpgradeParam::class.java)
                var model = param?.model.orEmpty()
                var version = param?.version.orEmpty()
                var pkt = param?.pkt.orEmpty()
                var md5 = param?.md5.orEmpty()
                DeviceCmdService.getInstance(application).camUpgrade(data.deviceId, model, version, pkt, md5) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var param = JsonConvertUtil.convertData(data.data, UpgradeParam::class.java)
                    var model = param?.model.orEmpty()
                    var version = param?.version.orEmpty()
                    var pkt = param?.pkt.orEmpty()
                    DeviceCmdService.getInstance(application).upgrade(data.deviceId, model, version, pkt) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = blockUpgrade,
            blockLpHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var param = JsonConvertUtil.convertData(data.data, UpgradeParam::class.java)
                    var model = param?.model.orEmpty()
                    var version = param?.version.orEmpty()
                    var pkt = param?.pkt.orEmpty()
                    var md5 = param?.md5.orEmpty()
                    DeviceCmdService.getInstance(application).hubUpgrade(data.deviceId, model, version, pkt, md5) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCamHub = blockUpgrade) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun resetDevice(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        var blockResetDevice : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).hubReset(data.deviceId) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).factoryReset(data.deviceId) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockResetDevice, blockLpCamHub = blockResetDevice) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun rebootDevice(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        var blockRebootDevice : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camReboot(data.deviceId) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = blockRebootDevice,
            blockLpHub =  { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).hubReboot(data.deviceId) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCamHub = blockRebootDevice) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun saveRotatePresetPoint(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var point  = JsonConvertUtil.convertData(data.data, RotatePresetPoint::class.java)
                    when(point?.pointIndex) {
                        1 -> {
                            DeviceCmdService.getInstance(application).ptzSavePos1(data.deviceId) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                        2 -> {
                            DeviceCmdService.getInstance(application).ptzSavePos2(data.deviceId) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                        3 -> {
                            DeviceCmdService.getInstance(application).ptzSavePos3(data.deviceId) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                        else -> {
                            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun turnRotatePresetPoint(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var point  = JsonConvertUtil.convertData(data.data, RotatePresetPoint::class.java)
                    when(point?.pointIndex) {
                        1 -> {
                            DeviceCmdService.getInstance(application).ptzTurnPos1(data.deviceId) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                        2 -> {
                            DeviceCmdService.getInstance(application).ptzTurnPos2(data.deviceId) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                        3 -> {
                            DeviceCmdService.getInstance(application).ptzTurnPos3(data.deviceId) { code ->
                            dealCmdAction(code, "", data, callback) {
                                it
                            }
                        }}
                        else -> {
                            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setPowerRotatePresetPoint(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var point  = JsonConvertUtil.convertData(data.data, RotatePresetPoint::class.java) ?: RotatePresetPoint().apply {
                        pointIndex = 1
                    }
                    DeviceCmdService.getInstance(application).ptzSetPowerOnPos(data.deviceId, point.pointIndex) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setMediaModeInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var info  = YRIpcDeviceUtil.convertMediaMode(JsonConvertUtil.convertData(data.data, MediaModeInfo::class.java))
                    if (info == null) {
                        callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                    } else {
                        DeviceCmdService.getInstance(application).setMediaMode(data.deviceId, info) { code ->
                            dealCmdAction(code, "", data, callback) {
                                it
                            }
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getMediaModeInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getMediaMode(data.deviceId) { code, info ->
                        var info = YRIpcDeviceUtil.convertMediaModeInfo(info)
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setNetSpotInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var info  = YRIpcDeviceUtil.convertNetSpot(JsonConvertUtil.convertData(data.data, NetSpotInfo::class.java))
                    if (info == null) {
                        callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                    } else {
                        DeviceCmdService.getInstance(application).setHotspot(data.deviceId, info) { code ->
                            dealCmdAction(code, "", data, callback) {
                                it
                            }
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getNetSpotInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getHotspot(data.deviceId) { code, info ->
                        var info = YRIpcDeviceUtil.convertNetSpotInfo(info)
                        dealCmdAction(code, info, data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun setDetectionInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            if (detectionParam?.info == null
                || (detectionParam.operationType == IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN && detectionParam.info?.planInfo == null)
                || (detectionParam.operationType == IPC_SCHEME_DETECTION_OPERATION_TYPE_LIGHT && detectionParam.info?.lightInfo == null)) {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                return
            }
            when(detectionParam.operationType) {
                IPC_SCHEME_DETECTION_OPERATION_TYPE_SWITCH -> { setDetectionSwitch(ipcConfigure, cmdData, callback) }
                IPC_SCHEME_DETECTION_OPERATION_TYPE_SENSITIVITY -> { setDetectionSensitivity(ipcConfigure, cmdData, callback) }
                IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN -> { setDetectionPlan(ipcConfigure, cmdData, callback) }
                IPC_SCHEME_DETECTION_OPERATION_TYPE_ALARM -> { setDetectionAlarm(ipcConfigure, cmdData, callback) }
                IPC_SCHEME_DETECTION_OPERATION_TYPE_LIGHT -> { setDetectionLight(ipcConfigure, cmdData, callback) }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getMotionDetectionInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getMotionDetectLevel(data.deviceId) { code, state ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionInfo(state), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getSoundDetectionInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getSoundDetectLevel(data.deviceId) { code, state ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionInfo(state), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getPirDetectionInfo(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).camGetPIRMode(data.deviceId) { code, pirState ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionInfo(pirState), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getPirV2(data.deviceId) { code, pirState ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionInfo(pirState), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getMotionDetectionPlan(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getMotionAlertPlanV2(data.deviceId) { code, planList ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionPlanInfo(planList), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getSoundDetectionPlan(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getSoundAlertPlanV2(data.deviceId) { code, planList ->
                        dealCmdAction(code, YRIpcDeviceUtil.convertDetectionPlanInfo(planList), data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun getPirDetectionPlan(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        var blockGetPirDetectionPlan : (YRIpcConfigure?, YRIpcDPCmdData<out Any>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                DeviceCmdService.getInstance(application).camGetPIRPlan(data.deviceId) { code, planList ->
                    dealCmdAction(code, YRIpcDeviceUtil.convertDetectionPlanInfo(planList), data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = blockGetPirDetectionPlan, blockLpCamHub = blockGetPirDetectionPlan) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun unbindDevice(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
            if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
                var param = JsonConvertUtil.convertData(cmdData.data, UnbindParam::class.java)
                var uid = param?.uid.orEmpty()
                DeviceCmdService.getInstance(application).unbindDevice(cmdData.deviceId, uid) { code ->
                    dealCmdAction(code, "", cmdData, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
            return
        }
        var blockUnbindDevice : (YRIpcConfigure?, YRIpcDPCmdData<String>?) -> Unit = { configure, data ->
            if (data != null && !data.deviceId.isNullOrEmpty()) {
                var param = JsonConvertUtil.convertData(data.data, UnbindParam::class.java)
                var uid = param?.uid.orEmpty()
                var parentId = param?.parentId.orEmpty()
                DeviceCmdService.getInstance(application).hubUnbind(data.deviceId, uid) { code ->
                    dealCmdAction(code, "", data, callback) {
                        it
                    }
                }
            } else {
                callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockNormal = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var param = JsonConvertUtil.convertData(data.data, UnbindParam::class.java)
                    var uid = param?.uid.orEmpty()
                    var parentId = param?.parentId.orEmpty()
                    DeviceCmdService.getInstance(application).unbindDevice(data.deviceId, uid) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCam = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    var param = JsonConvertUtil.convertData(data.data, UnbindParam::class.java)
                    var uid = param?.uid.orEmpty()
                    var parentId = param?.parentId.orEmpty()
                    DeviceCmdService.getInstance(application).camUnbind(data.deviceId, uid) { code ->
                        dealCmdAction(code, "", data, callback) {
                            it
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpHub = blockUnbindDevice,
            blockLpCamHub = blockUnbindDevice) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    fun startAPPair(cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        var connectConfigure = YRIpcDeviceUtil.convertApNetCfg(JsonConvertUtil.convertData(cmdData?.data, NetSpotConfigure::class.java))
        if (connectConfigure == null) {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            return
        }
        DeviceCmdService.getInstance(application).startAPPair(connectConfigure) { code ->
            dealCmdAction(code, "", cmdData, callback) {
                it
            }
        }
    }

    fun getAPPairInfo(cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        DeviceCmdService.getInstance(application).getAPPairStatus() { code, status, uuid ->
            dealCmdAction(code, YRIpcDeviceUtil.convertNetSportState((status?.intValue ?: 0)), cmdData, callback) {
                it
            }
        }
    }

    fun setUserInfo(cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        var connectConfigure = YRIpcDeviceUtil.convertApNetCfg(JsonConvertUtil.convertData(cmdData?.data, NetSpotConfigure::class.java))
        if (cmdData?.deviceId.isNullOrEmpty() || connectConfigure == null) {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            return
        }
        DeviceCmdService.getInstance(application).setUserInfo(cmdData?.deviceId, connectConfigure) { code ->
            dealCmdAction(code, "", cmdData, callback) {
                it
            }
        }
    }

    fun sendHeartBeat(cmdData: YRIpcDPCmdData<out Any>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            DeviceCmdService.getInstance(application).heartBeat(cmdData?.deviceId) { code ->
                dealCmdAction(code, "", cmdData, callback) {
                    it
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    /**
     * 根据设备配置命令类型执行对应回调
     */
    private fun <T> runByCmdType(ipcConfigure : YRIpcConfigure?, data : YRIpcDPCmdData<T>?,
                         blockNormal : ((YRIpcConfigure?, YRIpcDPCmdData<T>?) -> Unit)? = null,
                         blockLpCam : ((YRIpcConfigure?, YRIpcDPCmdData<T>?) -> Unit)? = null,
                         blockLpHub: ((YRIpcConfigure?, YRIpcDPCmdData<T>?) -> Unit)? = null,
                         blockLpCamHub : ((YRIpcConfigure?, YRIpcDPCmdData<T>?) -> Unit)? = null,
                         blockUnSupport : ((YRIpcConfigure?, YRIpcDPCmdData<T>?) -> Unit)? = null) {
        when(ipcConfigure?.cmdType) {
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_NORMAL -> { blockNormal?.invoke(ipcConfigure, data) }
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM -> {  blockLpCam?.invoke(ipcConfigure, data) }
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_HUB -> {  blockLpHub?.invoke(ipcConfigure, data) }
            IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM_HUB -> {  blockLpCamHub?.invoke(ipcConfigure, data) }
            else -> { blockUnSupport?.invoke(ipcConfigure, data)}
        }
    }

    /**
     * 统一处理命令返回结果
     * T：命令参数类型；V：命令返回结果类型
     * code：自有Ipc方案命令调用状态码
     * actionResult：自有Ipc方案命令返回结果
     * data：命令参数
     * callback：命令回调接口
     * block：转化命令返回结果
     */
    private fun <T, V> dealCmdAction(code : Int, actionResult : V?, data: YRIpcDPCmdData<T>?, callback: IIpcSchemeResultCallback? = null, block : ((V?) -> Any?)? = null) {
        YRLog.d { "-->> dealCmdAction deviceId ${data?.deviceId} dpId ${data?.dpId} code $code actionResult $actionResult" }
        if (code == Constant.OK) {
            var result = IpcDPCmdResult<Any>()
            data?.let {
                result.deviceId = it.deviceId
                if (!it.dpId.isNullOrEmpty()) {
                    var dpAndData : MutableMap<String, Any> = mutableMapOf()
                    dpAndData[it.dpId!!] = block?.invoke(actionResult) ?: Any()
                    result.data = JsonConvertUtil.convertToJson(dpAndData)
                }
            }
            callback?.onSuccess(IPC_SCHEME_CMD_STATE_CODE_SUCCESS, JsonConvertUtil.convertToJson(result))
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setDetectionSwitch(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            when(detectionParam?.info?.detectionType) {
                IPC_SCHEME_DETECTION_TYPE_MOTION -> {
                    var level = if (detectionParam.info?.enable == true) MotionDetectLevel.MOTION_DETECT_HIGH else MotionDetectLevel.MOTION_DETECT_CLOSE
                    DeviceCmdService.getInstance(application).setMotionDetectLevel(cmdData.deviceId, level) { code ->
                        dealCmdAction(code, "", cmdData, callback) {
                            it
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_SOUND -> {
                    var level = if (detectionParam.info?.enable == true) SoundDetectLevel.SOUND_DETECT_HIGH else SoundDetectLevel.SOUND_DETECT_CLOSE
                    DeviceCmdService.getInstance(application).setSoundDetectLevel(cmdData.deviceId, level) { code ->
                        dealCmdAction(code, "", cmdData, callback) {
                            it
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_PIR -> {
                    setPir(ipcConfigure, cmdData, callback) { pirState ->
                        if (pirState != null && pirState is PirState) {
                            (pirState as? PirState)?.apply {
                                enable = detectionParam.info?.enable == true
                            }
                        } else if (pirState != null && pirState is PirStateV2) {
                            (pirState as? PirStateV2)?.apply {
                                enable = detectionParam.info?.enable == true
                            }
                        } else {
                            null
                        }
                    }
                }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setDetectionSensitivity(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            when(detectionParam?.info?.detectionType) {
                IPC_SCHEME_DETECTION_TYPE_MOTION -> {
                    DeviceCmdService.getInstance(application).setMotionDetectLevel(cmdData.deviceId, YRIpcDeviceUtil.convertMotionDetectLevel(detectionParam.info?.level ?: IPC_SCHEME_SENSITIVITY_LEVEL_HIGH)) { code ->
                        dealCmdAction(code, "", cmdData, callback) {
                            it
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_SOUND -> {
                    DeviceCmdService.getInstance(application).setSoundDetectLevel(cmdData.deviceId, YRIpcDeviceUtil.convertSoundDetectLevel(detectionParam.info?.level ?: IPC_SCHEME_SENSITIVITY_LEVEL_HIGH)) { code ->
                        dealCmdAction(code, "", cmdData, callback) {
                            it
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_PIR -> {
                    setPir(ipcConfigure, cmdData, callback) { pirState ->
                        if (pirState != null && pirState is PirState) {
                            (pirState as? PirState)?.apply {
                                sensitivityLevel = YRIpcDeviceUtil.convertSensitivityLevel(detectionParam.info?.level ?: IPC_SCHEME_SENSITIVITY_LEVEL_HIGH)
                            }
                        } else if (pirState != null && pirState is PirStateV2) {
                            (pirState as? PirStateV2)?.apply {
                                sensitivityLevel = YRIpcDeviceUtil.convertSensitivityLevel(detectionParam.info?.level ?: IPC_SCHEME_SENSITIVITY_LEVEL_HIGH)
                            }
                        } else {
                            null
                        }
                    }
                }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setDetectionPlan(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            when(detectionParam?.info?.detectionType) {
                IPC_SCHEME_DETECTION_TYPE_MOTION -> {
                    DeviceCmdService.getInstance(application).getMotionAlertPlanV2(cmdData.deviceId) { code, planList ->
                        if (code == Constant.OK) {
                            DeviceCmdService.getInstance(application).setMotionAlertPlanV2(cmdData.deviceId, YRIpcDeviceUtil.convertDetectionPlan(detectionParam?.info?.planInfo, planList)) { code ->
                                dealCmdAction(code, "", cmdData, callback) {
                                    it
                                }
                            }
                        } else {
                            dealCmdAction(code, "", cmdData, callback) {
                                it
                            }
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_SOUND -> {
                    DeviceCmdService.getInstance(application).getSoundAlertPlanV2(cmdData.deviceId) { code, planList ->
                        if (code == Constant.OK) {
                            DeviceCmdService.getInstance(application).setSoundAlertPlanV2(cmdData.deviceId, YRIpcDeviceUtil.convertDetectionPlan(detectionParam?.info?.planInfo, planList)) { code ->
                                dealCmdAction(code, "", cmdData, callback) {
                                    it
                                }
                            }
                        } else {
                            dealCmdAction(code, "", cmdData, callback) {
                                it
                            }
                        }
                    }
                }
                IPC_SCHEME_DETECTION_TYPE_PIR -> {
                    DeviceCmdService.getInstance(application).camSetPIRPlan(cmdData.deviceId, YRIpcDeviceUtil.convertPirPlan(detectionParam?.info?.planInfo)) { code ->
                        dealCmdAction(code, "", cmdData, callback) {
                            it
                        }
                    }
                }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setDetectionAlarm(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            when(detectionParam?.info?.detectionType) {
                IPC_SCHEME_DETECTION_TYPE_PIR -> {
                    setPir(ipcConfigure, cmdData, callback) { pirState ->
                        if (pirState != null && pirState is PirState) {
                            (pirState as? PirState)?.apply {
                                siren = detectionParam.info?.alarmOn == true
                            }
                        } else if (pirState != null && pirState is PirStateV2) {
                            (pirState as? PirStateV2)?.apply {
                                siren = detectionParam.info?.alarmOn == true
                            }
                        } else {
                            null
                        }
                    }
                }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setDetectionLight(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null) {
        if (cmdData != null && !cmdData.deviceId.isNullOrEmpty()) {
            var detectionParam = JsonConvertUtil.convertData(cmdData.data, DetectionParam::class.java);
            when(detectionParam?.info?.detectionType) {
                IPC_SCHEME_DETECTION_TYPE_PIR -> {
                    setPir(ipcConfigure, cmdData, callback) { pirState ->
                        if (pirState != null && pirState is PirStateV2) {
                            (pirState as? PirStateV2)?.apply {
                                lightMode = detectionParam.info?.lightInfo?.mode ?: IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
                            }
                        } else {
                            null
                        }
                    }
                }
                else -> {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }
        } else {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun setPir(ipcConfigure: YRIpcConfigure?, cmdData: YRIpcDPCmdData<String>?, callback: IIpcSchemeResultCallback? = null, blockTransform : (Any?) -> Any?) {
        if (YRIpcConnectionManager.checkNetSpotConnectionExist()) {
            cmdData?.apply {
                deviceId = getNetSpotDeviceId()
            }
        }
        runByCmdType(ipcConfigure, cmdData,
            blockLpCam = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).camGetPIRMode(data.deviceId) { code, pirState ->
                        var state = if (code == Constant.OK) blockTransform.invoke(pirState) as? PirState else null
                        if (state == null) {
                            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        } else {
                            DeviceCmdService.getInstance(application).camSetPIRMode(data.deviceId, state) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            },
            blockLpCamHub = { configure, data ->
                if (data != null && !data.deviceId.isNullOrEmpty()) {
                    DeviceCmdService.getInstance(application).getPirV2(data.deviceId) { code, pirState ->
                        var state = if (code == Constant.OK) blockTransform.invoke(pirState) as? PirStateV2 else null
                        if (state == null) {
                            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        } else {
                            DeviceCmdService.getInstance(application).setPirV2(data.deviceId, state) { code ->
                                dealCmdAction(code, "", data, callback) {
                                    it
                                }
                            }
                        }
                    }
                } else {
                    callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                }
            }) { configure, data ->
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
        }
    }

    private fun getNetSpotDeviceId() : String {
        return IPC_SCHEME_NET_SPOT_AP_DEVICE_ID
    }
}