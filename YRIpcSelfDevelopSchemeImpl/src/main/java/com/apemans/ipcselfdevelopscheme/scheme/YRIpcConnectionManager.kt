/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.scheme

import com.dylanc.longan.application
import com.nooie.sdk.base.Constant
import com.nooie.sdk.device.DeviceCmdService
import com.nooie.sdk.device.bean.APNetCfg
import com.nooie.sdk.device.bean.DeviceConnInfo
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.ipcchipproxy.define.IPC_SCHEME_CMD_STATE_CODE_ERROR
import com.apemans.ipcchipproxy.define.IPC_SCHEME_CMD_STATE_CODE_SUCCESS
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.*
import com.apemans.ipcchipproxy.scheme.util.retrySendCmd
import com.apemans.ipcchipproxy.scheme.util.runCallbackStream
import com.apemans.ipcselfdevelopscheme.cache.YRIpcConfigureCache
import com.apemans.ipcselfdevelopscheme.cache.YRIpcConnectionCache
import com.apemans.ipcselfdevelopscheme.util.YRIpcDeviceUtil
import com.apemans.logger.YRLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/13 2:59 下午
 * 说明:
 *
 * 备注: 自有Ipc方案链接管理类
 *
 ***********************************************************/
object YRIpcConnectionManager {

    private var keepNetSpotIsAlive = false
    private var keepNetSpotAliveTask : Job? = null
    var netSpotDeviceInfo : NetSpotDeviceInfo? = null

    /**
     *  自有Ipc方案P2P链接配置初始化
     */
    fun initP2P(uid : String, url : String, port : Int) {
        DeviceCmdService.getInstance(application).initConn(uid, url, port)
    }

    /**
     *  自有Ipc方案链接配置销毁
     */
    fun destroyP2P() {
        DeviceCmdService.getInstance(application).destroyAllConn {  }
    }

    /**
     *  自有Ipc方案设备进行P2P链接
     */
    fun connectP2P(deviceId : String) {
        YRIpcConfigureCache.getIpcConfigure(deviceId)?.let {
            if (it.deviceId.isNullOrEmpty()) {
                return
            }
            val connInfo = DeviceConnInfo().apply {
                this.uuid = if (!YRIpcDeviceUtil.checkIsChildDevice(it.parentDeviceId)) it.deviceId else it.parentDeviceId
                userName = it.uid
                hbServer = it.serverDomain
                hbPort = it.serverPort
                secret = it.secret
                modeType = it.modelType
                connType = it.connType
            }
            DeviceCmdService.getInstance(application).connecNooieDevice(mutableListOf(connInfo))
            YRIpcConnectionCache.addConnection(YRIpcDeviceUtil.convertConnectionConfigure(connInfo))
        }
    }

    /**
     *  自有Ipc方案设备断开P2P链接
     */
    fun disconnectP2P(deviceId : String) {
        YRIpcConfigureCache.getIpcConfigure(deviceId)?.let {
            if (it.deviceId.isNullOrEmpty()) {
                return
            }
            var disconnectDeviceId = if (YRIpcDeviceUtil.checkIsChildDevice(it.parentDeviceId)) {
                it.parentDeviceId
            } else {
                it.deviceId
            }
            DeviceCmdService.getInstance(application).removeConnDevice(disconnectDeviceId)
            YRIpcConnectionCache.removeConnection(disconnectDeviceId)
        }
        YRIpcConfigureCache.removeDeviceConfigure(deviceId)
    }

    fun connectNetSpotTcp(param : String, ssid: String, callback: IIpcSchemeResultCallback?) {
        var deviceConnInfo = YRIpcDeviceUtil.convertDeviceConnInfo(JsonConvertUtil.convertData(param, ConnectionConfigure::class.java))
        if (deviceConnInfo == null) {
            callback?.onError()
            return
        }
        var result = DeviceCmdService.getInstance(application).apNewConn(listOf(deviceConnInfo))
        keepNetSpotAlive(deviceConnInfo.uuid)
        checkNetSpotTcp(deviceConnInfo.uuid, ssid) { code ->
            if (code == Constant.OK) {
                callback?.onSuccess()
            } else {
                callback?.onError()
            }
        }
    }

    fun disconnectNetSpot(deviceId : String) {
        var result = DeviceCmdService.getInstance(application).apRemoveConn(deviceId)
        cancelKeepNetSpotAlive()
        YRIpcConfigureCache.removeDeviceConfigure(deviceId)
        netSpotDeviceInfo = null
    }

    fun connectNetSpotP2P(param : String, ssid: String, bleDeviceId: String?, model: String?, callback: IIpcSchemeResultCallback?) {
        var deviceConnInfo = YRIpcDeviceUtil.convertDeviceConnInfo(JsonConvertUtil.convertData(param, ConnectionConfigure::class.java))
        if (deviceConnInfo == null) {
            callback?.onError()
            return
        }
        var result = DeviceCmdService.getInstance(application).apNewConn(listOf(deviceConnInfo))
        checkNetSpotP2P(deviceConnInfo.uuid, ssid, bleDeviceId, model) { code ->
            if (code == Constant.OK) {
                callback?.onSuccess()
            } else {
                callback?.onError()
            }
        }
    }

    fun disconnectNetSpotP2P(deviceId : String) {
        var result = DeviceCmdService.getInstance(application).apRemoveConn(deviceId)
        YRIpcConfigureCache.removeDeviceConfigure(deviceId)
        netSpotDeviceInfo = null
    }

    fun checkNetSpotConnectionExist() : Boolean {
        return netSpotDeviceInfo?.let {
            !it.apDeviceId.isNullOrEmpty() && !it.deviceId.isNullOrEmpty() && !it.model.isNullOrEmpty()
        } ?: false
    }

    private fun checkNetSpotTcp(deviceId: String, ssid: String, block: ((Int) -> Unit)? = null) {
        var apNetCfg = APNetCfg().apply {
            region = "us"
            zone = "1"
            encrypt = "OPEN"
        }

        CoroutineScope(Dispatchers.Main).launch {
            retrySendCmd(2, 1000) {
                var callbackResultJson = runCallbackStream {
                    DeviceCmdService.getInstance(application).setUserInfo(deviceId, apNetCfg) { code ->
                        if (code == Constant.OK) {
                            DeviceCmdService.getInstance(application).setUTCTimeStamp(deviceId, System.currentTimeMillis() / 1000L) { codeOfSetTime ->
                                DeviceCmdService.getInstance(application).getDevInfo(deviceId) { code, info ->
                                    if (code == Constant.OK) {
                                        var resultJson = JsonConvertUtil.convertToJson(
                                            DeviceSettingInfo().apply {
                                                this.deviceId = info.uuid
                                                model = info.model
                                            }
                                        )
                                        it.onSuccess(IPC_SCHEME_CMD_STATE_CODE_SUCCESS, resultJson)
                                    } else {
                                        it.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                                    }
                                }
                            }
                        } else {
                            it.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        }
                    }
                }
                var callbackResult = JsonConvertUtil.convertData(callbackResultJson, IpcSchemeResult::class.java)
                var sendCmdIsSuccess = callbackResult?.let { it.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS } ?: false
                IpcSchemeRetryCmdResult<IpcSchemeResult>().apply {
                    isSuccess = sendCmdIsSuccess
                    data = callbackResult
                }
            }
                .catch { e ->
                    YRLog.d { "-->> getNetSpotDeviceSetting e $e"}
                }
                .collect {
                    YRLog.d { "-->> getNetSpotDeviceSetting isSuccess ${it.isSuccess} code ${it.data?.code} result ${it.data?.result}" }
                    JsonConvertUtil.convertData(it.data?.result, DeviceSettingInfo::class.java)?.let {
                        saveNetSpotTcpInfo(deviceId, it.deviceId, it.model, ssid)
                    }
                    if (it.data?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        block?.invoke(Constant.OK)
                    } else {
                        block?.invoke(Constant.ERROR)
                    }
                }
        }
    }

    private fun checkNetSpotP2P(deviceId: String, ssid: String, bleDeviceId: String? = "", model: String? = "", block: ((Int) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Main).launch {
            retrySendCmd(2, 1000) {
                var callbackResultJson = runCallbackStream {
                    DeviceCmdService.getInstance(application).camGetInfo(deviceId) { code, info ->
                        if (code == Constant.OK) {
                            var resultJson = JsonConvertUtil.convertToJson(
                                DeviceSettingInfo().apply {
                                    this.deviceId = info.uuid
                                }
                            )
                            it.onSuccess(IPC_SCHEME_CMD_STATE_CODE_SUCCESS, resultJson)
                        } else {
                            it.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
                        }
                    }
                }
                var callbackResult = JsonConvertUtil.convertData(callbackResultJson, IpcSchemeResult::class.java)
                var sendCmdIsSuccess = callbackResult?.let { it.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS } ?: false
                IpcSchemeRetryCmdResult<IpcSchemeResult>().apply {
                    isSuccess = sendCmdIsSuccess
                    data = callbackResult
                }
            }
                .catch { e ->
                    YRLog.d { "-->> getNetSpotDeviceSetting e $e"}
                }
                .collect {
                    YRLog.d { "-->> getNetSpotDeviceSetting isSuccess ${it.isSuccess} code ${it.data?.code} result ${it.data?.result}" }
                    JsonConvertUtil.convertData(it.data?.result, DeviceSettingInfo::class.java)?.let {
                        var modelTmp = model
                        if (model.isNullOrEmpty()) {
                            modelTmp = "HC320"
                        }
                        saveNetSpotTcpInfo(deviceId, it.deviceId, modelTmp, ssid, bleDeviceId)
                    }
                    if (it.data?.code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                        block?.invoke(Constant.OK)
                    } else {
                        block?.invoke(Constant.ERROR)
                    }
                }
        }
    }

    private fun cancelKeepNetSpotAlive() {
        var isCancel = keepNetSpotAliveTask?.isActive ?: false
        if (isCancel) {
            keepNetSpotAliveTask?.cancel()
            keepNetSpotAliveTask = null
        }
        keepNetSpotIsAlive = false
    }

    private fun saveNetSpotTcpInfo(apDeviceId: String?, deviceId: String?, model: String?, ssid: String? = null, bleDeviceId: String? = null) {
        if (apDeviceId.isNullOrEmpty() || deviceId.isNullOrEmpty() || model.isNullOrEmpty()) {
            return
        }
        netSpotDeviceInfo = NetSpotDeviceInfo().apply {
            this.deviceId = deviceId
            this.model = model
            this.apDeviceId = apDeviceId
            this.ssid = ssid
            this.bleDeviceId = bleDeviceId
        }
    }

    private fun getNetSpotDeviceSetting(deviceId: String, ssid: String, block: ((Int) -> Unit)? = null) {

    }

    @DelicateCoroutinesApi
    private fun keepNetSpotAlive(deviceId : String) {
        keepNetSpotIsAlive = true
        GlobalScope.launch {
            keepNetSpotAliveTask = launch {
                flow<Int> {
                    emit(0)
                }
                    .retryWhen { cause, attempt ->
                        delay(5 * 1000)
                        keepNetSpotIsAlive
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        YRLog.d { "$e" }
                    }
                    .collect {
                        DeviceCmdService.getInstance(application).heartBeat(deviceId) { code ->
                        }
                    }
            }
        }
    }
}