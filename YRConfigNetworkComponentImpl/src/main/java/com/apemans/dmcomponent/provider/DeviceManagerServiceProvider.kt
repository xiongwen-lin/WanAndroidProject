/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.dmcomponent.provider

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.bluetooth.SmartBleManager
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.datastore.db.entity.BleDeviceEntity
import com.apemans.datastore.db.entity.DeviceEntity
import com.apemans.dmcomponent.db.DeviceManagerDbHelper
import com.apemans.dmapi.services.DeviceManagerService
import com.apemans.dmapi.services.path.PATH_DEVICE_MANAGER
import com.apemans.dmapi.webapi.*
import com.apemans.dmcomponent.webapi.DeviceApiHelper
import kotlinx.coroutines.flow.Flow

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/13 7:04 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

@Route(path = PATH_DEVICE_MANAGER)
class DeviceManagerServiceProvider : DeviceManagerService {
    override fun init(context: Context) {
        SmartBleManager.initCore(context)
            .configServiceUUID("0000181c-0000-1000-8000-00805f9b34fb")
            .configWriteUUID("0000181d-0000-1000-8000-00805f9b34fb")
            //.configReadUUID("00002222-0000-1000-8000-00805f9b34fb")
            .configNotifyUUID("0000181e-0000-1000-8000-00805f9b34fb")
            .configReceiveMessageCharacteristicUUID("0000181e-0000-1000-8000-00805f9b34fb")
            .configAutoSplitLongData(true)
    }

    override fun getDeviceBindStatus(): Flow<YRApiResponse<DeviceBindStatusResult>> {
        return DeviceApiHelper.deviceApi.getDeviceBindStatus()
    }

    override fun getRecentBindDevices(): Flow<YRApiResponse<MutableList<BindDevice>>> {
        return DeviceApiHelper.deviceApi.getRecentBindDevices()
    }

    override fun updateDeviceName(body: UpdateDeviceNameBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.updateDeviceName(body)
    }

    override fun getBindDevices(page: Int, perPage: Int): Flow<YRApiResponse<BindDeviceListResult>> {
        return DeviceApiHelper.deviceApi.getBindDevices(page, perPage)
    }

    override fun getDeviceInfo(deviceId: String): Flow<YRApiResponse<BindDevice>> {
        return DeviceApiHelper.deviceApi.getDeviceInfo(deviceId)
    }

    override fun getDeviceRelationList(deviceId: String, page: Int, perPage: Int): Flow<YRApiResponse<DeviceRelationListResult>> {
        return DeviceApiHelper.deviceApi.getDeviceRelationList(deviceId, page, perPage)
    }

    override fun deleteSharingDevice(id: Int): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.deleteSharingDevice(id)
    }

    override fun sendDeviceSharing(body: SharingDeviceBody): Flow<YRApiResponse<ShareDeviceResult>> {
        return DeviceApiHelper.deviceApi.sendDeviceSharing(body)
    }

    override fun feedbackDeviceSharing(body: SharingFeedbackBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.feedbackDeviceSharing(body)
    }

    override fun getDeviceUpgradeStatus(deviceId: String): Flow<YRApiResponse<DeviceUpgradeStatusResult>> {
        return DeviceApiHelper.deviceApi.getDeviceUpgradeStatus(deviceId)
    }

    override fun sortDevice(body: DeviceSortBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.sortDevice(body)
    }

    override fun bindDevice(body: DeviceBindingBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.bindDevice(body)
    }

    override fun updateDeviceConfigure(body: UpdateDeviceConfigureBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.updateDeviceConfigure(body)
    }

    override fun updateDeviceNotice(body: UpdateDeviceNoticeBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.updateDeviceNotice(body)
    }

    override fun getGatewayDeviceList(type: String): Flow<YRApiResponse<MutableList<GatewayDevice>>> {
        return DeviceApiHelper.deviceApi.getGatewayDeviceList(type)
    }

    override fun getSubDeviceList(deviceId: String): Flow<YRApiResponse<MutableList<BindDevice>>> {
        return DeviceApiHelper.deviceApi.getSubDeviceList(deviceId)
    }

    override suspend fun updateIpcDevice(param: Bundle) {
        DeviceManagerDbHelper.updateIpcDevice(param)
    }

    override suspend fun getAllIpcDevice(uid: String): MutableList<DeviceEntity> {
        return DeviceManagerDbHelper.getAllIpcDevice(uid)
    }

    override suspend fun getIpcDevice(uid: String, deviceId: String): DeviceEntity? {
        return DeviceManagerDbHelper.getIpcDevice(uid, deviceId)
    }

    override suspend fun deleteIpcDevice(uid: String, deviceId: String) {
        DeviceManagerDbHelper.deleteIpcDevice(uid, deviceId)
    }

    override suspend fun updateBleIpcDevice(param: Bundle) {
        DeviceManagerDbHelper.updateBleIpcDevice(param)
    }

    override suspend fun getAllBleIpcDevice(uid: String): MutableList<BleDeviceEntity> {
        return DeviceManagerDbHelper.getAllBleIpcDevice(uid)
    }

    override suspend fun getBleIpcDevice(uid: String, deviceId: String): BleDeviceEntity? {
        return DeviceManagerDbHelper.getBleIpcDevice(uid, deviceId)
    }

    override suspend fun deleteBleIpcDevice(uid: String, deviceId: String) {
        DeviceManagerDbHelper.deleteBleIpcDevice(uid, deviceId)
    }

    override fun deleteRemoteGatewayDevice(body: DeleteDeviceBindingBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.deleteGatewayDevice(body)
    }

    override fun deleteRemoteDevice(body: DeleteDeviceBindingBody): Flow<YRApiResponse<Any>> {
        return DeviceApiHelper.deviceApi.deleteDevice(body)
    }
}