package com.apemans.ipcchipproxy.scheme.cache

import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/3 11:36 上午
 * 说明:
 *
 * 备注: Ipc链接缓存接口
 *
 ***********************************************************/
interface IIpcConnectionCache {

    /**
     * 获取当前直连设备信息
     * 返回当前直连中的设备信息，若返回空表示无设备直连中
     */
    fun getNetSpotDeviceInfo() : NetSpotDeviceInfo?

    /**
     * 判断当前是否有设备直连中
     */
    fun checkNetSpotConnectionExist() : Boolean

    /**
     * 根据设备id判断是否已完成远程P2P连接
     */
    fun checkConnectionExist(deviceId: String?) : Boolean
}