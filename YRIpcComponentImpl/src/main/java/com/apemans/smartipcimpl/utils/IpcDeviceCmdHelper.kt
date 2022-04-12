package com.apemans.smartipcimpl.utils

import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.ConnectionConfigure
import com.apemans.ipcchipproxy.scheme.bean.NetSpotConfigure
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.ipcchipproxy.scheme.cache.IIpcConnectionCache
import com.apemans.ipccontrolapi.define.IPC_SCHEME_TYPE_SELF_DEVELOP
import com.apemans.ipccontrolapi.services.IpcControlManagerService
import com.apemans.logger.YRLog
import com.apemans.router.routerServices
import com.apemans.smartipcapi.api.IIpcCmdHelper
import com.apemans.smartipcapi.bean.IpcP2PConnectionInfo
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.base.utils.JsonConvertUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/26 9:52 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object IpcDeviceCmdHelper : IIpcCmdHelper {

    private val ipcControlService by routerServices<IpcControlManagerService>()

    override fun sendStartNetSpotPairCmd(configure: NetSpotConfigure, callback: IIpcSchemeResultCallback?) {
        var netSpotPairConfigure = JsonConvertUtil.convertToJson(configure)
        ipcSchemeCore()?.sendCmd(
            DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_START_AP_PAIR, "", IPC_SCHEME_DP_AUTHORITY_R, netSpotPairConfigure), callback)
    }

    override fun sendGetNetSpotPairStateCmd(callback: IIpcSchemeResultCallback?) {
        ipcSchemeCore()?.sendCmd(
            DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_GET_AP_PAIR_INFO, "", IPC_SCHEME_DP_AUTHORITY_R, null), callback)
    }

    override fun sendHeartBeatCmd(deviceId : String, callback: IIpcSchemeResultCallback?) {
        ipcSchemeCore()?.sendCmd(
            DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_SEND_HEART_BEAT, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), callback)
    }

    override fun sendStorageInfoCmd(deviceId : String, callback: IIpcSchemeResultCallback?) {
        ipcSchemeCore()?.sendCmd(
            DeviceControlHelper.createDpCmd(IPC_SCHEME_DP_STORAGE_INFO, deviceId, IPC_SCHEME_DP_AUTHORITY_R, null), callback)
    }

    fun startP2PConnection(deviceInfo: IpcDeviceInfo?) {
        deviceInfo?.let {
            ipcSchemeCore()
                ?.configureCache()
                ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            var connectionDeviceId: String? = if (!DeviceControlHelper.checkIsChildDevice(it.pDeviceId)) it.device_id else it.pDeviceId
            val connectable = it.online == DeviceDefine.ONLINE && !checkP2PConnectionExist(connectionDeviceId)
            if (connectable) {
                var param = mutableMapOf<String, Any>()
                param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_P2P
                param[IPC_SCHEME_KEY_DEVICE_ID] = connectionDeviceId.orEmpty()
                ipcSchemeCore()?.connect(param)
            }
        }
    }

    fun startP2PConnection(deviceInfo: IpcP2PConnectionInfo?) {
        deviceInfo?.let {
            ipcSchemeCore()
                ?.configureCache()
                ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            var connectionDeviceId: String? = if (!DeviceControlHelper.checkIsChildDevice(it.parentDeviceId)) it.deviceId else it.parentDeviceId
            val connectable = it.online && !checkP2PConnectionExist(connectionDeviceId)
            if (connectable) {
                var param = mutableMapOf<String, Any>()
                param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_P2P
                param[IPC_SCHEME_KEY_DEVICE_ID] = connectionDeviceId.orEmpty()
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
                param[IPC_SCHEME_KEY_DEVICE_ID] = connectionDeviceId.orEmpty()
                ipcSchemeCore()?.connect(param)
            }
        }
    }

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

    fun checkP2PConnectionExist(deviceId: String?) : Boolean {
        return getConnectionCache()?.checkConnectionExist(deviceId) ?: false
    }

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

    fun removeNetSpot(deviceId: String? = null, callback : ((Boolean) -> Unit)? = null) {
        var param = mutableMapOf<String, Any>()
        param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] = IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP
        param[IPC_SCHEME_KEY_DEVICE_ID] = IPC_SCHEME_NET_SPOT_AP_DEVICE_ID
        ipcSchemeCore()?.disconnect(param, object : IIpcSchemeResultCallback {

            override fun onSuccess(code: Int, result: String?) {
                dealOnNetSpotConnected(true, callback)
            }

            override fun onError(code: Int, error: String?) {
                dealOnNetSpotConnected(false, callback)
            }
        })
    }

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
                callback?.invoke(true)
            }

            override fun onError(code: Int, error: String?) {
                callback?.invoke(false)
            }

        })
    }

    fun checkIsNetSpotMode() : Boolean {
        return ipcSchemeCore()?.connectionCache()?.checkNetSpotConnectionExist() ?: false
    }

    fun getNetSpotDeviceInfo() : NetSpotDeviceInfo? {
        return ipcSchemeCore()?.connectionCache()?.getNetSpotDeviceInfo()
    }

    suspend fun updateBleIpcDeviceInDbOnNetSpotConnected(isSuccess : Boolean) {
        if (!isSuccess) {
            return
        }
        var netSpotConnectionIsExist = checkIsNetSpotMode()
        var netSpotDeviceInfo = getNetSpotDeviceInfo()
        if (!netSpotConnectionIsExist || netSpotDeviceInfo == null) {
            return
        }
        DeviceManagerRepository.updateBleIpcDevice(obtainUid(), netSpotDeviceInfo).single()
    }

    private fun ipcSchemeCore() : IIpcSchemeCore? {
        return ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)
    }

    private fun getConnectionCache() : IIpcConnectionCache? {
        return ipcSchemeCore()?.connectionCache()
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
        GlobalScope.launch {
            DeviceManagerRepository.updateBleIpcDevice(obtainUid(), netSpotDeviceInfo)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                    callback?.invoke(false)
                }
                .collect {
                    callback?.invoke(true)
                }
        }
    }

    private fun obtainUid() : String {
        return ""
    }
}