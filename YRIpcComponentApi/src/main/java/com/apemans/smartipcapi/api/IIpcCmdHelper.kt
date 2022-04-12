package com.apemans.smartipcapi.api

import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.NetSpotConfigure

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/26 9:49 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
interface IIpcCmdHelper {

    /**
     * 发送Ap配网命令
     */
    fun sendStartNetSpotPairCmd(configure: NetSpotConfigure, callback: IIpcSchemeResultCallback? = null)

    /**
     *
     */
    fun sendGetNetSpotPairStateCmd(callback: IIpcSchemeResultCallback? = null)

    fun sendHeartBeatCmd(deviceId : String, callback: IIpcSchemeResultCallback? = null)

    fun sendStorageInfoCmd(deviceId : String, callback: IIpcSchemeResultCallback? = null)

}