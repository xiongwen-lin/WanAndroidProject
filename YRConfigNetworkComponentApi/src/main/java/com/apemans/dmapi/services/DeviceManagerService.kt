/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.dmapi.services

import android.os.Bundle
import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.datastore.db.entity.BleDeviceEntity
import com.apemans.datastore.db.entity.DeviceEntity
import com.apemans.dmapi.webapi.*
import kotlinx.coroutines.flow.Flow

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/13 5:50 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
interface DeviceManagerService : IProvider {

    /** start 平台设备接口 */

    /**
     * 获取设备绑定状态
     */
    fun getDeviceBindStatus() : Flow<YRApiResponse<DeviceBindStatusResult>>

    /**
     * 获取最近绑定的设备
     */
    fun getRecentBindDevices() : Flow<YRApiResponse<MutableList<BindDevice>>>

    /**
     * 更新设备名称
     */
    fun updateDeviceName(body: UpdateDeviceNameBody) : Flow<YRApiResponse<Any>>

    /**
     * 获取当前绑定的ipc设备
     */
    fun getBindDevices(page : Int, perPage : Int) : Flow<YRApiResponse<BindDeviceListResult>>

    /**
     * 获取ipc设备信息
     */
    fun getDeviceInfo(deviceId : String) : Flow<YRApiResponse<BindDevice>>

    /**
     * 获取设备的绑定关系
     */
    fun getDeviceRelationList(deviceId : String, page : Int,  per_page : Int) : Flow<YRApiResponse<DeviceRelationListResult>>

    /**
     * 删除分享设备
     */
    fun deleteSharingDevice(id : Int) : Flow<YRApiResponse<Any>>

    /**
     * 发送设备分享给其他用户
     */
    fun sendDeviceSharing(body : SharingDeviceBody) : Flow<YRApiResponse<ShareDeviceResult>>

    /**
     * 处理设备分享邀请
     */
    fun feedbackDeviceSharing(body : SharingFeedbackBody) : Flow<YRApiResponse<Any>>

    /**
     * 获取设备升级信息
     */
    fun getDeviceUpgradeStatus(deviceId : String) : Flow<YRApiResponse<DeviceUpgradeStatusResult>>

    /**
     * 设备排序
     */
    fun sortDevice(body : DeviceSortBody) : Flow<YRApiResponse<Any>>

    /**
     * 手机扫码绑定设备
     */
    fun bindDevice(body : DeviceBindingBody) : Flow<YRApiResponse<Any>>

    /**
     * 更新设备服务器配置信息
     */
    fun updateDeviceConfigure(body : UpdateDeviceConfigureBody) : Flow<YRApiResponse<Any>>

    /**
     * 更新设备通知开关
     */
    fun updateDeviceNotice(body : UpdateDeviceNoticeBody) : Flow<YRApiResponse<Any>>

    /**
     * 获取用户绑定网关设备
     */
    fun getGatewayDeviceList(type : String) : Flow<YRApiResponse<MutableList<GatewayDevice>>>

    /**
     * 获取网关设备的子设备
     */
    fun getSubDeviceList(deviceId : String) : Flow<YRApiResponse<MutableList<BindDevice>>>

    /** end 平台设备接口 */

    /** start 本地数据库设备管理接口 */

    /**
     * 添加或更新IPC设备信息到本地数据库，相关参数键值参考com.smart.dmapi.datastore.db.DeviceManagerDbConstant中的键值常量
     * var param = Bundle()
     * param.putString(KEY_UID, "123")
     * param.putString(KEY_DEVICE_ID, "123)
     */
    suspend fun updateIpcDevice(param : Bundle)

    /**
     * 从本地数据中获取用户的全部设备信息
     */
    suspend fun getAllIpcDevice(uid : String) : MutableList<DeviceEntity>

    /**
     * 从本地数据中获取用户的单台设备信息
     */
    suspend fun getIpcDevice(uid : String, deviceId : String) : DeviceEntity?

    /**
     * 从本地数据中删除用户的设备
     */
    suspend fun deleteIpcDevice(uid : String, deviceId : String)

    /**
     * 添加或更新蓝牙IPC设备信息到本地数据库，相关参数键值参考com.smart.dmapi.datastore.db.DeviceManagerDbConstant中的键值常量
     * var param = Bundle()
     * param.putString(KEY_UID, "123")
     * param.putString(KEY_DEVICE_ID, "123)
     */
    suspend fun updateBleIpcDevice(param : Bundle)

    /**
     * 从本地数据中获取用户的全部蓝牙IPC设备信息
     */
    suspend fun getAllBleIpcDevice(uid : String) : MutableList<BleDeviceEntity>

    /**
     * 从本地数据中获取用户的单台蓝牙IPC设备信息
     */
    suspend fun getBleIpcDevice(uid : String, deviceId : String) : BleDeviceEntity?

    /**
     * 从本地数据中删除用户的蓝牙IPC设备
     */
    suspend fun deleteBleIpcDevice(uid : String, deviceId : String)

    /**
     * 从服务器删除网关设备
     */
    fun deleteRemoteGatewayDevice(body: DeleteDeviceBindingBody): Flow<YRApiResponse<Any>>

    /**
     * 从服务器删除设备
     */
    fun deleteRemoteDevice(body: DeleteDeviceBindingBody): Flow<YRApiResponse<Any>>

    /** end 本地数据库设备管理接口 */
}