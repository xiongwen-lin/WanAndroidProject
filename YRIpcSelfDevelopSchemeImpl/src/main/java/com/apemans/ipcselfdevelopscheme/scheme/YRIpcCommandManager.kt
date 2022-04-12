/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.scheme

import com.google.gson.reflect.TypeToken
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.cache.IIpcSchemeDPTool
import com.apemans.ipcselfdevelopscheme.bean.YRIpcConfigure
import com.apemans.ipcselfdevelopscheme.bean.YRIpcDPCmdData
import com.apemans.ipcselfdevelopscheme.cache.YRIpcConfigureCache
import com.apemans.ipcselfdevelopscheme.util.YRIpcDeviceUtil
import com.apemans.logger.YRLog

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/15 3:28 下午
 * 说明:
 *
 * 备注: 自有Ipc方案命令操作管理类
 *
 ***********************************************************/
object YRIpcCommandManager {

    /**
     * 发送功能点命令
     */
    fun sendCmd(cmd : String?, callback: IIpcSchemeResultCallback? = null) {
        YRLog.d { "-->> YRIpcCommandManager sendCmd $cmd" }
        var cmdMap : Map<String, Any>? = convertCmd(cmd)
        if (cmdMap.isNullOrEmpty()) {
            return
        }
        cmdMap.forEach {
            handleCmd(it.key, it.value, callback)
        }
    }

    /**
     * 根据不同功能点分发点用不同命令
     */
    private fun handleCmd(dpId : String?, data : Any?, callback: IIpcSchemeResultCallback? = null) {
        YRLog.d { "-->> YRIpcCommandManager handleCmd dpId $dpId data $data" }
        when(dpId) {
            IPC_SCHEME_DP_SWITCH_LED -> {
                dealCmd<Boolean>(dpId, data) {code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setLed(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getLed(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_DEVICE_SETTING -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getSetting(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_AUDIO_RECORD -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setAudioRecord(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getAudioRecord(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_ROTATE -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setRotate(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getRotate(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_LOOP_RECORD -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setLoopRecord(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getLoopRecord(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_SLEEP -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setSleep(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getSleep(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_DETECT_HUMAN -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setDetectHuman(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getDetectHuman(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_DETECT_CRY -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setDetectCry(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getDetectCry(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_WATER_MARK -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setWaterMark(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getWaterMark(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_ENERGY_SAVING_MODE -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setEnergySavingMode(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getEnergySavingMode(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_FLASH_LIGHT_STATE -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setFlashLightState(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getFlashLightState(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_IR -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setIR(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getIR(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_LIGHT_MODE -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setLightMode(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getLightMode(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_TIME_CONFIGURE -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.setTimeConfigure(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_STORAGE_INFO -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getStorageInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_FORMAT_STORAGE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.formatStorage(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_DATE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getStorageRecordVideoDate(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getStorageRecordVideoInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_DATE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getStorageRecordImageDate(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getStorageRecordImageInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_ALARM_STATE -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.setAlarmState(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_DETECTION_AREA_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setDetectionAreaInfo(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getDetectionAreaInfo(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_UPGRADE -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.upgrade(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_RESET_DEVICE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.resetDevice(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_REBOOT_DEVICE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.rebootDevice(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_SAVE_ROTATE_PRESET_POINT -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.saveRotatePresetPoint(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_TURN_ROTATE_PRESET_POINT -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.turnRotatePresetPoint(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_POWER_ROTATE_PRESET_POINT -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.setPowerRotatePresetPoint(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_MEDIA_MODE_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setMediaModeInfo(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getMediaModeInfo(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_NET_SPOT_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setNetSpotInfo(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getNetSpotInfo(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_DETECTION_INFO -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.setDetectionInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_MOTION_DETECTION_INFO -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getMotionDetectionInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_SOUND_DETECTION_INFO -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getSoundDetectionInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_PIR_DETECTION_INFO -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getPirDetectionInfo(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_MOTION_DETECTION_PLAN -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getMotionDetectionPlan(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_SOUND_DETECTION_PLAN -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getSoundDetectionPlan(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_PIR_DETECTION_PLAN -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getPirDetectionPlan(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_SOUND_STATE -> {
                dealCmd<Any>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.getSoundState(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_MOTION_TRACK -> {
                dealCmd<Boolean>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        if (YRIpcDeviceUtil.isWriteAuthority(cmdData?.authority)) {
                            YRIpcCommandApi.setMotionTrack(ipcConfigure, cmdData, callback)
                        } else {
                            YRIpcCommandApi.getMotionTrack(ipcConfigure, cmdData, callback)
                        }
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_UNBIND_DEVICE -> {
                dealCmd<String>(dpId, data) { code, ipcConfigure, cmdData ->
                    YRLog.d { "-->> YRIpcCommandManager handleCmd dpId ${cmdData?.deviceId} code $code cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
                    if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        YRIpcCommandApi.unbindDevice(ipcConfigure, cmdData, callback)
                    } else {
                        callback?.onError()
                    }
                }
            }
            IPC_SCHEME_DP_START_AP_PAIR -> {
                dealCmdSkipChecking<String>(dpId, data) { cmdData ->
                    YRIpcCommandApi.startAPPair(cmdData, callback)
                }
            }
            IPC_SCHEME_DP_GET_AP_PAIR_INFO -> {
                dealCmdSkipChecking<Any>(dpId, data) { cmdData ->
                    YRIpcCommandApi.getAPPairInfo(cmdData, callback)
                }
            }
            IPC_SCHEME_DP_SET_USER_INFO -> {
                dealCmdSkipChecking<String>(dpId, data) { cmdData ->
                    YRIpcCommandApi.setUserInfo(cmdData, callback)
                }
            }
            IPC_SCHEME_DP_SEND_HEART_BEAT -> {
                dealCmdSkipChecking<String>(dpId, data) { cmdData ->
                    YRIpcCommandApi.sendHeartBeat(cmdData, callback)
                }
            }
            else -> {
                callback?.onError()
            }
        }
    }

    /**
     * 解析命令参数并统一处理
     */
    private fun <T> dealCmd(dpId : String?, data : Any?, block : (Int, YRIpcConfigure?, data : YRIpcDPCmdData<T>?) -> Unit) {
        var dataJson = data as? String
        var cmdData = JsonConvertUtil.convertData(dataJson, object : TypeToken<YRIpcDPCmdData<T>>(){})
        cmdData?.dpId = dpId
        YRLog.d { "-->> YRIpcCommandManager dealCmd dpId $dpId cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
        var ipcConfigure = YRIpcConfigureCache.getIpcConfigure(cmdData?.deviceId.orEmpty())
        var dpTool : IIpcSchemeDPTool? = YRIpcConfigureCache.getDpTool(cmdData?.deviceId.orEmpty())
        if (ipcConfigure == null || dpTool == null || !dpTool?.isSupport(dpId)) {
            block.invoke(IPC_SCHEME_CMD_STATE_CODE_NOT_SUPPORT, null, null)
            return
        }
        block.invoke(IPC_SCHEME_CMD_STATE_CODE_SUCCESS, ipcConfigure, cmdData)
    }

    /**
     * 解析命令参数并统一处理
     */
    private fun <T> dealCmdSkipChecking(dpId : String?, data : Any?, block : (data : YRIpcDPCmdData<T>?) -> Unit) {
        var dataJson = data as? String
        var cmdData = JsonConvertUtil.convertData(dataJson, object : TypeToken<YRIpcDPCmdData<T>>(){})
        cmdData?.dpId = dpId
        YRLog.d { "-->> YRIpcCommandManager dealCmd dpId $dpId cmdData deviceId ${cmdData?.deviceId} data ${cmdData?.data}" }
        block.invoke(cmdData)
    }

    /**
     * 将命令json文本转为键值对
     */
    private fun convertCmd(cmd: String?) : Map<String, Any>? {
        if (cmd == null || cmd.isEmpty()) {
            return null
        }
        try {
            return JsonConvertUtil.convertData(cmd, object : TypeToken<Map<String, Any>>(){})
        } catch (e: Exception) {
        }
        return null
    }


}