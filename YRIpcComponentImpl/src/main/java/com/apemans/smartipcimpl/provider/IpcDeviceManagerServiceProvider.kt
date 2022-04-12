package com.apemans.smartipcimpl.provider

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.base.middleservice.YRMiddleConst
import com.apemans.base.middleservice.YRMiddleServiceListener
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.middleservice.YRMiddleServiceResponse
import com.apemans.ipcchipproxy.define.IPC_SCHEME_CMD_STATE_CODE_ERROR
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.NetSpotConfigure
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.smartipcapi.api.IIpcCmdHelper
import com.apemans.smartipcapi.api.IIpcDeviceHelper
import com.apemans.smartipcapi.bean.IpcP2PConnectionInfo
import com.apemans.smartipcapi.services.IpcDeviceManagerService
import com.apemans.smartipcapi.services.path.PATH_SMART_IPC_DEVICE_MANAGER
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.utils.IpcDeviceCmdHelper
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.ipcchipproxy.define.IPC_SCHEME_CMD_STATE_CODE_SUCCESS
import com.apemans.ipcchipproxy.define.IPC_SCHEME_DP_STORAGE_INFO
import com.apemans.ipcchipproxy.scheme.bean.IpcSchemeResult
import com.apemans.ipcchipproxy.scheme.bean.StorageInfo
import com.apemans.ipcchipproxy.scheme.util.parseIpcDPCmdResult
import com.apemans.ipcchipproxy.scheme.util.subscribeCallbackFlow
import com.apemans.logger.YRLog
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/24 3:20 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
@Route(path = PATH_SMART_IPC_DEVICE_MANAGER)
class IpcDeviceManagerServiceProvider : IpcDeviceManagerService {

    override fun init(context: Context?) {
    }

    override fun obtainIpcDeviceHelper() : IIpcDeviceHelper? {
        return DeviceControlHelper
    }

    override fun obtainIpcCmdHelper(): IIpcCmdHelper? {
        return IpcDeviceCmdHelper
    }

    override fun startP2PConnection(connectionInfo: String?) : Boolean {
        val ipcDeviceInfo = JsonConvertUtil.convertData(connectionInfo, IpcP2PConnectionInfo::class.java) ?: return false
        IpcDeviceCmdHelper.startP2PConnection(ipcDeviceInfo)
        return true
    }

    override fun removeP2PConnection(deviceId: String) {
        IpcDeviceCmdHelper.removeP2PConnection(deviceId)
    }

    override fun checkP2PConnectionExist(deviceId: String?): Boolean {
        return IpcDeviceCmdHelper.checkP2PConnectionExist(deviceId)
    }

    override fun startNetSpotTcp(ssid: String?, callback: ((Boolean) -> Unit)?) {
        IpcDeviceCmdHelper.startNetSpotTcp(ssid, callback)
    }

    override fun startNetSpotP2p(ssid: String?, bleDeviceId: String?, model: String?, callback: ((Boolean) -> Unit)?) {
        IpcDeviceCmdHelper.startNetSpotP2p(ssid, bleDeviceId, model, callback)
    }

    override fun removeNetSpot(deviceId: String?, callback: ((Boolean) -> Unit)?) {
        IpcDeviceCmdHelper.removeNetSpot(deviceId, callback)
    }

    override fun checkIsNetSpotMode(): Boolean {
        return IpcDeviceCmdHelper.checkIsNetSpotMode()
    }

    override fun getNetSpotDeviceInfo(): NetSpotDeviceInfo? {
        return IpcDeviceCmdHelper.getNetSpotDeviceInfo()
    }

    override fun sendStartNetSpotPairCmd(configureInfo: String?, callback: IIpcSchemeResultCallback?) {
        var configure = JsonConvertUtil.convertData(configureInfo, NetSpotConfigure::class.java)
        if (configure == null) {
            callback?.onError(IPC_SCHEME_CMD_STATE_CODE_ERROR)
            return
        }
        IpcDeviceCmdHelper.sendStartNetSpotPairCmd(configure, callback)
    }

    override fun sendGetNetSpotPairStateCmd(callback: IIpcSchemeResultCallback?) {
        IpcDeviceCmdHelper.sendGetNetSpotPairStateCmd(callback)
    }

    override fun sendHeartBeatCmd(deviceId: String, callback: IIpcSchemeResultCallback?) {
        IpcDeviceCmdHelper.sendHeartBeatCmd(deviceId, callback)
    }

    override fun updateNetSpotDeviceConfigure(uid: String, callback: ((Boolean) -> Unit)?) {
        DeviceControlHelper.updateNetSpotDeviceConfigure(uid, callback)
    }

    override fun checkNetSpotSsidValid(ssid: String?): Boolean {
        return DeviceControlHelper.checkNetSpotSsidValid(ssid)
    }

    override fun queryIpcDevice(lifeCycle: Lifecycle, uid: String, listener: YRMiddleServiceListener?) {
        lifeCycle.coroutineScope.launch {

            DeviceManagerRepository.queryIpcDeviceListFromLocal(uid)
                .flowOn(Dispatchers.IO)
                .catch {
                    YRLog.d { it }
                    queryIpcDeviceFromRemote(uid, listOf(), listener)
                }
                .collect { localIpcDeviceList ->
                    localIpcDeviceList.filter {
                        it.deviceType == IpcDevice.DEVICE_TYPE_IPC
                    }
                    localIpcDeviceList.filter {
                        it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
                    }
                    listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", JsonConvertUtil.convertToJson(localIpcDeviceList)))
                    queryIpcDeviceFromRemote(uid, localIpcDeviceList, listener)
                }

        }

    }

    override fun queryIpcDeviceListAsMap(lifeCycle: Lifecycle, uid: String, listener: YRMiddleServiceListener?) {
        lifeCycle.coroutineScope.launch {

            DeviceManagerRepository.queryLocalIpcDeviceListAsMapFlow(uid)
                .flowOn(Dispatchers.IO)
                .catch {
                    YRLog.d { it }
                    queryIpcDeviceListAsMapFromRemote(listener)
                }
                .collect { ipcDeviceMaps ->
                    listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", JsonConvertUtil.convertToJson(ipcDeviceMaps)))
                    queryIpcDeviceListAsMapFromRemote(listener)
                }

        }

    }

    override fun queryIpcDeviceStorageInfo(lifeCycle: Lifecycle, deviceId: String, listener: YRMiddleServiceListener?) {
        lifeCycle.coroutineScope.launch {

            var cmdResult = subscribeCallbackFlow {
                IpcDeviceCmdHelper.sendStorageInfoCmd(deviceId, it)
            }
                .single()

            var result: IpcSchemeResult? = JsonConvertUtil.convertData(cmdResult, IpcSchemeResult::class.java)
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcDeviceManagerServiceProvider queryIpcDeviceStorageInfo code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info : StorageInfo? = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_INFO) {
                    object : TypeToken<Map<String, StorageInfo>>(){}
                }
                YRLog.d { "-->> IpcDeviceManagerServiceProvider queryIpcDeviceStorageInfo info ${info.toString()}" }
                listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", JsonConvertUtil.convertToJson(info)))
            } else {
                listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", ""))
            }

        }
    }

    private suspend fun queryIpcDeviceFromRemote(uid: String, localIpcDeviceList: List<IpcDevice>, listener: YRMiddleServiceListener?) {
        DeviceManagerRepository.queryIpcDeviceListFromRemote(uid, localIpcDeviceList)
            .flowOn(Dispatchers.IO)
            .catch {
                YRLog.d { it }
            }
            .collect { remoteIpcDeviceList ->
                remoteIpcDeviceList?.filter {
                    it.deviceType == IpcDevice.DEVICE_TYPE_IPC || it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
                }.forEach {
                    (it?.device?.deviceInfo as IpcDeviceInfo)?.run {
                        IpcDeviceCmdHelper.startP2PConnection(this)
                    }
                }
                remoteIpcDeviceList.filter {
                    it.deviceType == IpcDevice.DEVICE_TYPE_IPC
                }
                remoteIpcDeviceList.filter {
                    it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
                }
                listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", JsonConvertUtil.convertToJson(remoteIpcDeviceList)))
            }
    }

    private suspend fun queryIpcDeviceListAsMapFromRemote(listener: YRMiddleServiceListener?) {
        DeviceManagerRepository.queryRemoteIpcDeviceListAsMapFlow(1, 100)
            .flowOn(Dispatchers.IO)
            .catch {
                YRLog.d { it }
            }
            .collect { ipcDeviceMaps ->
                listener?.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, "成功✅✅✅", JsonConvertUtil.convertToJson(ipcDeviceMaps)))
            }
    }

}