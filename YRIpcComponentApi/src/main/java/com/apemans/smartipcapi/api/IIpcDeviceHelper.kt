package com.apemans.smartipcapi.api

import com.apemans.dmapi.model.DeviceModel
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.smartipcapi.bean.NetSpotDeviceConfigure

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/25 9:39 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
interface IIpcDeviceHelper {

    fun updateNetSpotDeviceConfigure(uid : String, callback : ((Boolean) -> Unit)? = null)

    fun updateNetSpotDeviceConfigure(netSpotDeviceInfo: NetSpotDeviceInfo?, ipcDevice: DeviceModel?)

    fun getNetSpotDeviceConfigure() : NetSpotDeviceConfigure?

    fun checkNetSpotDeviceConfigureValid() : Boolean

    fun checkNetSpotSsidValid(ssid: String?) : Boolean

    fun checkCloudValid(endTime: Long, deviceTimeZone: Float) : Boolean

    fun checkCloudSubscribe(status: Int) : Boolean

    fun convertCloudExpireTime(time: Long) : Long

    fun createEnterMark(uid: String?): String?

    fun createCloudPackUrl(webUrl: String?, deviceId: String?, model: String?, enterMark: String?, origin: String?): String?

    fun compareVersion(version1: String, version2: String): Int

    fun checkIsChildDevice(parentDeviceId: String?): Boolean

    fun checkIsOwnerDevice(bindType: Int) : Boolean

}