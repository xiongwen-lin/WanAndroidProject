package com.apemans.ipcselfdevelopscheme.cache

import com.apemans.ipcchipproxy.scheme.bean.ConnectionConfigure
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.ipcchipproxy.scheme.cache.IIpcConnectionCache
import com.apemans.ipcselfdevelopscheme.scheme.YRIpcConnectionManager

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/3 11:40 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRIpcConnectionCache : IIpcConnectionCache {

    private var connectionCache : MutableMap<String, ConnectionConfigure> = mutableMapOf()

    override fun getNetSpotDeviceInfo(): NetSpotDeviceInfo? {
        return YRIpcConnectionManager.netSpotDeviceInfo
    }

    override fun checkNetSpotConnectionExist(): Boolean {
        return YRIpcConnectionManager.checkNetSpotConnectionExist()
    }

    override fun checkConnectionExist(deviceId: String?) : Boolean {
        return deviceId?.let { connectionCache.containsKey(it) } ?: false
    }

    fun addConnection(connectionConfigure: ConnectionConfigure?) {
        connectionConfigure?.uuid?.let { connectionCache.put(it, connectionConfigure) }
    }

    fun getConnection(deviceId : String?) : ConnectionConfigure? {
        return deviceId?.let { connectionCache[it] }
    }

    fun removeConnection(deviceId: String?) {
        if (!checkConnectionExist(deviceId)) {
            return
        }
        deviceId?.let { connectionCache.remove(deviceId) }
    }

    fun removeAllConnection() {
        connectionCache.clear()
    }
}