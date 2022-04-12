package com.apemans.smartipcapi.services

import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.base.middleservice.YRMiddleServiceListener
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.NetSpotConfigure
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.smartipcapi.api.IIpcCmdHelper
import com.apemans.smartipcapi.api.IIpcDeviceHelper

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/24 3:16 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
interface IpcDeviceManagerService : IProvider {

    fun obtainIpcDeviceHelper() : IIpcDeviceHelper?

    fun obtainIpcCmdHelper() : IIpcCmdHelper?

    fun startP2PConnection(connectionInfo: String?) : Boolean

    fun removeP2PConnection(deviceId: String)

    fun checkP2PConnectionExist(deviceId: String?) : Boolean

    fun startNetSpotTcp(ssid : String?, callback : ((Boolean) -> Unit)? = null)

    fun startNetSpotP2p(ssid : String?, bleDeviceId : String?, model : String?, callback : ((Boolean) -> Unit)? = null)

    fun removeNetSpot(deviceId: String? = null, callback : ((Boolean) -> Unit)? = null)

    fun checkIsNetSpotMode() : Boolean

    fun getNetSpotDeviceInfo() : NetSpotDeviceInfo?

    fun sendStartNetSpotPairCmd(configureInfo: String?, callback: IIpcSchemeResultCallback?)

    fun sendGetNetSpotPairStateCmd(callback: IIpcSchemeResultCallback?)

    fun sendHeartBeatCmd(deviceId : String, callback: IIpcSchemeResultCallback?)

    fun updateNetSpotDeviceConfigure(uid : String, callback : ((Boolean) -> Unit)? = null)

    fun checkNetSpotSsidValid(ssid: String?) : Boolean

    fun queryIpcDevice(lifeCycle: Lifecycle, uid: String, listener: YRMiddleServiceListener?)

    fun queryIpcDeviceListAsMap(lifeCycle: Lifecycle, uid: String, listener: YRMiddleServiceListener?)

    fun queryIpcDeviceStorageInfo(lifeCycle: Lifecycle, deviceId: String, listener: YRMiddleServiceListener?)
}