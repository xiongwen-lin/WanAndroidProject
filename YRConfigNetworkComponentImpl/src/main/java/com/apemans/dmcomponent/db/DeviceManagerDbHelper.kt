/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.dmcomponent.db

import android.os.Bundle
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.datastore.db.entity.BleDeviceEntity
import com.apemans.datastore.db.entity.DeviceEntity
import com.apemans.dmapi.info.IpcDeviceInfo
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/14 6:59 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DeviceManagerDbHelper {

    suspend fun updateIpcDevice(param : Bundle) {
        try {
            var uid : String = param.getString(KEY_UID).orEmpty()
            var deviceId : String = param.getString(KEY_DEVICE_ID).orEmpty()
            if (uid.isEmpty() || deviceId.isEmpty()) {
                return
            }
            var isDeviceExist = true
            var deviceEntity = SmartDatabase.getDatabase().deviceDao().getDevice(uid, deviceId) ?: DeviceEntity().apply {
                this.uid = uid
                this.deviceId = deviceId
                isDeviceExist = false
            }
            deviceEntity.apply {
                param.checkAndUpdate(KEY_DEVICE_MODEL) {
                    model = param.getStringOrEmpty(KEY_DEVICE_MODEL)
                }
                param.checkAndUpdate(KEY_DEVICE_NAME) {
                    name = param.getStringOrEmpty(KEY_DEVICE_NAME)
                }
                param.checkAndUpdate(KEY_DEVICE_MAIN_CATEGORY) {
                    mainCategory = param.getInt(KEY_DEVICE_MAIN_CATEGORY)
                }
                param.checkAndUpdate(KEY_DEVICE_SUB_CATEGORY) {
                    subCategory = param.getInt(KEY_DEVICE_SUB_CATEGORY)
                }
                param.checkAndUpdate(KEY_DEVICE_PLATFORM) {
                    platform = param.getInt(KEY_DEVICE_PLATFORM)
                }
                param.checkAndUpdate(KEY_DEVICE_ICON_URL) {
                    iconUrl = param.getStringOrEmpty(KEY_DEVICE_ICON_URL)
                }
                param.checkAndUpdate(KEY_DEVICE_BRAND) {
                    brand = param.getStringOrEmpty(KEY_DEVICE_BRAND)
                }
                param.checkAndUpdate(KEY_DEVICE_COMMUNICATION_TYPE) {
                    communicationType = param.getInt(KEY_DEVICE_COMMUNICATION_TYPE)
                }
                param.checkAndUpdate(KEY_DEVICE_BIND_TYPE) {
                    bindType = param.getInt(KEY_DEVICE_BIND_TYPE)
                }
                param.checkAndUpdate(KEY_DEVICE_SN) {
                    sn = param.getStringOrEmpty(KEY_DEVICE_SN)
                }
                param.checkAndUpdate(KEY_DEVICE_MAC) {
                    mac = param.getStringOrEmpty(KEY_DEVICE_MAC)
                }
                param.checkAndUpdate(KEY_DEVICE_VERSION) {
                    version = param.getStringOrEmpty(KEY_DEVICE_VERSION)
                }
                param.checkAndUpdate(KEY_DEVICE_LOCAL_IP) {
                    localIp = param.getLong(KEY_DEVICE_LOCAL_IP)
                }
                param.checkAndUpdate(KEY_DEVICE_WAN_IP) {
                    wanIp = param.getLong(KEY_DEVICE_WAN_IP)
                }
                param.checkAndUpdate(KEY_DEVICE_TIME) {
                    time = param.getLong(KEY_DEVICE_TIME)
                }
                param.checkAndUpdate(KEY_DEVICE_HB_SERVER) {
                    hbServer = param.getLong(KEY_DEVICE_HB_SERVER)
                }
                param.checkAndUpdate(KEY_DEVICE_HB_PORT) {
                    hbPort = param.getInt(KEY_DEVICE_HB_PORT)
                }
                param.checkAndUpdate(KEY_DEVICE_HB_DOMAIN) {
                    hbDomain = param.getStringOrEmpty(KEY_DEVICE_HB_DOMAIN)
                }
                param.checkAndUpdate(KEY_DEVICE_OPEN_STATUS) {
                    openStatus = param.getInt(KEY_DEVICE_OPEN_STATUS)
                }
                param.checkAndUpdate(KEY_DEVICE_ONLINE) {
                    online = param.getInt(KEY_DEVICE_ONLINE)
                }
                param.checkAndUpdate(KEY_DEVICE_ZONE) {
                    zone = param.getFloat(KEY_DEVICE_ZONE)
                }
                param.checkAndUpdate(KEY_DEVICE_IS_GOOGLE) {
                    googleState = param.getInt(KEY_DEVICE_IS_GOOGLE)
                }
                param.checkAndUpdate(KEY_DEVICE_IS_ALEXA) {
                    alexaState = param.getInt(KEY_DEVICE_IS_ALEXA)
                }
                param.checkAndUpdate(KEY_DEVICE_P_DEVICE_ID) {
                    pDeviceId = param.getStringOrEmpty(KEY_DEVICE_P_DEVICE_ID)
                }
                param.checkAndUpdate(KEY_DEVICE_WIFI_LEVEL) {
                    wifiLevel = param.getInt(KEY_DEVICE_WIFI_LEVEL)
                }
                param.checkAndUpdate(KEY_DEVICE_BATTERY_LEVEL) {
                    batteryLevel = param.getInt(KEY_DEVICE_BATTERY_LEVEL)
                }
                param.checkAndUpdate(KEY_DEVICE_SORT) {
                    sort = param.getInt(KEY_DEVICE_SORT)
                }
                param.checkAndUpdate(KEY_DEVICE_P_MODEL) {
                    pModel = param.getStringOrEmpty(KEY_DEVICE_P_MODEL)
                }
                param.checkAndUpdate(KEY_DEVICE_P_VERSION) {
                    pVersion = param.getStringOrEmpty(KEY_DEVICE_P_VERSION)
                }
                param.checkAndUpdate(KEY_DEVICE_MODEL_TYPE) {
                    modelType = param.getInt(KEY_DEVICE_MODEL_TYPE)
                }
                param.checkAndUpdate(KEY_DEVICE_SECRET) {
                    secret = param.getStringOrEmpty(KEY_DEVICE_SECRET)
                }
                param.checkAndUpdate(KEY_DEVICE_IS_THEFT) {
                    theftState = param.getInt(KEY_DEVICE_IS_THEFT)
                }
                param.checkAndUpdate(KEY_DEVICE_IS_NOTICE) {
                    noticeState = param.getInt(KEY_DEVICE_IS_NOTICE)
                }
                param.checkAndUpdate(KEY_DEVICE_APP_TIME_CONFIG) {
                    appTimingConfig = param.getStringOrEmpty(KEY_DEVICE_APP_TIME_CONFIG)
                }
                param.checkAndUpdate(KEY_DEVICE_MQTT_ONLINE) {
                    mqttOnline = param.getInt(KEY_DEVICE_MQTT_ONLINE)
                }
                param.checkAndUpdate(KEY_DEVICE_REGION) {
                    region = param.getStringOrEmpty(KEY_DEVICE_REGION)
                }
                param.checkAndUpdate(KEY_DEVICE_OWNER_ACCOUNT) {
                    region = param.getStringOrEmpty(KEY_DEVICE_OWNER_ACCOUNT)
                }
                param.checkAndUpdate(KEY_DEVICE_OWNER_NICKNAME) {
                    region = param.getStringOrEmpty(KEY_DEVICE_OWNER_NICKNAME)
                }
            }
            if (isDeviceExist) {
                SmartDatabase.getDatabase().deviceDao().updateDevice(deviceEntity)
            } else {
                SmartDatabase.getDatabase().deviceDao().insertDevice(deviceEntity)
            }
        } catch(e : Exception) {
        }
    }

    suspend fun getAllIpcDevice(uid : String) : MutableList<DeviceEntity>  {
        try {
            return SmartDatabase.getDatabase().deviceDao().getAllDevices(uid)
        } catch (e : Exception) {
        }
        return mutableListOf()
    }

    suspend fun getIpcDevice(uid : String, deviceId : String) : DeviceEntity?  {
        try {
            return SmartDatabase.getDatabase().deviceDao().getDevice(uid, deviceId)
        } catch (e : Exception) {
        }
        return null
    }

    suspend fun deleteIpcDevice(uid : String, deviceId : String)  {
        try {
            SmartDatabase.getDatabase().deviceDao().getDevice(uid, deviceId)?.let {
                SmartDatabase.getDatabase().deviceDao().deleteDevices(it)
            }
        } catch (e : Exception) {
        }
    }

    suspend fun updateIpcDevice(uid : String, device : IpcDeviceInfo) {
        val param = Bundle()
        param.apply {
            putString(KEY_UID, uid)
            putString(KEY_DEVICE_ID, device.device_id)
            putString(KEY_DEVICE_MODEL, device.model)
            putString(KEY_DEVICE_NAME, device.name)
            putInt(KEY_DEVICE_MAIN_CATEGORY, device.main_category)
            putInt(KEY_DEVICE_SUB_CATEGORY, device.sub_category)
            putInt(KEY_DEVICE_PLATFORM, device.platform)
            //putString(KEY_DEVICE_ICON_URL, device.icon_url)
            putString(KEY_DEVICE_BRAND, device.brand)
            putInt(KEY_DEVICE_COMMUNICATION_TYPE, device.communication_type)
            putInt(KEY_DEVICE_BIND_TYPE, device.bindType)
            putString(KEY_DEVICE_SN, device.sn)
            putString(KEY_DEVICE_MAC, device.mac)
            putString(KEY_DEVICE_VERSION, device.version)
            putLong(KEY_DEVICE_LOCAL_IP, device.localIp)
            putLong(KEY_DEVICE_WAN_IP, device.wanIp)
            putLong(KEY_DEVICE_TIME, device.time)
            putLong(KEY_DEVICE_HB_SERVER, device.hbServer)
            putInt(KEY_DEVICE_HB_PORT, device.hbPort)
            putString(KEY_DEVICE_HB_DOMAIN, device.hbDomain)
            putInt(KEY_DEVICE_OPEN_STATUS, device.openStatus)
            putInt(KEY_DEVICE_ONLINE, device.online)
            putFloat(KEY_DEVICE_ZONE, device.zone)
            putInt(KEY_DEVICE_IS_GOOGLE, device.isGoogle)
            putInt(KEY_DEVICE_IS_ALEXA, device.isAlexa)
            putString(KEY_DEVICE_P_DEVICE_ID, device.pDeviceId)
            putInt(KEY_DEVICE_WIFI_LEVEL, device.wifiLevel)
            putInt(KEY_DEVICE_BATTERY_LEVEL, device.batteryLevel)
            putInt(KEY_DEVICE_SORT, device.sort)
            putString(KEY_DEVICE_P_MODEL, device.pModel)
            putString(KEY_DEVICE_P_VERSION, device.pVersion)
            putInt(KEY_DEVICE_MODEL_TYPE, device.modelType)
            putString(KEY_DEVICE_SECRET, device.secret)
            putInt(KEY_DEVICE_IS_THEFT, device.isTheft)
            putInt(KEY_DEVICE_IS_NOTICE, device.isNotice)
            putString(KEY_DEVICE_APP_TIME_CONFIG, device.appTimingConfig)
            putInt(KEY_DEVICE_MQTT_ONLINE, device.mqttOnline)
            putString(KEY_DEVICE_REGION, device.region)
            putString(KEY_DEVICE_OWNER_NICKNAME, device.ownerNickname)
            putString(KEY_DEVICE_OWNER_ACCOUNT, device.ownerAccount)
        }
        updateIpcDevice(param)
    }

    suspend fun updateBleIpcDevice(uid: String, netSpotDeviceInfo: NetSpotDeviceInfo?) {
        if (uid.isNullOrEmpty() || netSpotDeviceInfo == null || netSpotDeviceInfo.deviceId.isNullOrEmpty()) {
            return
        }

        var param  = Bundle().apply {
            putString(KEY_UID, uid)
            putString(KEY_DEVICE_ID, netSpotDeviceInfo.deviceId.orEmpty())
            putString(KEY_DEVICE_MODEL, netSpotDeviceInfo.model.orEmpty())
            putString(KEY_DEVICE_SSID, netSpotDeviceInfo.ssid.orEmpty())
            putString(KEY_DEVICE_BLE_DEVICE_ID, netSpotDeviceInfo.bleDeviceId.orEmpty())
        }
        updateBleIpcDevice(param)
    }

    suspend fun updateBleIpcDevice(param : Bundle) {
        try {
            var uid : String = param.getString(KEY_UID).orEmpty()
            var deviceId : String = param.getString(KEY_DEVICE_ID).orEmpty()
            if (uid.isEmpty() || deviceId.isEmpty()) {
                return
            }
            var isDeviceExist = true
            var deviceEntity = SmartDatabase.getDatabase().deviceDao().getBleDevice(uid, deviceId) ?: BleDeviceEntity(0).apply {
                this.uid = uid
                this.deviceId = deviceId
                isDeviceExist = false
            }
            deviceEntity.apply {
                param.checkAndUpdate(KEY_DEVICE_MODEL) {
                    model = param.getStringOrEmpty(KEY_DEVICE_MODEL)
                }
                param.checkAndUpdate(KEY_DEVICE_NAME) {
                    name = param.getStringOrEmpty(KEY_DEVICE_NAME)
                }
                param.checkAndUpdate(KEY_DEVICE_MAIN_CATEGORY) {
                    mainCategory = param.getInt(KEY_DEVICE_MAIN_CATEGORY)
                }
                param.checkAndUpdate(KEY_DEVICE_SUB_CATEGORY) {
                    subCategory = param.getInt(KEY_DEVICE_SUB_CATEGORY)
                }
                param.checkAndUpdate(KEY_DEVICE_PLATFORM) {
                    platform = param.getInt(KEY_DEVICE_PLATFORM)
                }
                param.checkAndUpdate(KEY_DEVICE_ICON_URL) {
                    iconUrl = param.getStringOrEmpty(KEY_DEVICE_ICON_URL)
                }
                param.checkAndUpdate(KEY_DEVICE_BRAND) {
                    brand = param.getStringOrEmpty(KEY_DEVICE_BRAND)
                }
                param.checkAndUpdate(KEY_DEVICE_COMMUNICATION_TYPE) {
                    communicationType = param.getInt(KEY_DEVICE_COMMUNICATION_TYPE)
                }
                param.checkAndUpdate(KEY_DEVICE_BLE_DEVICE_ID) {
                    bleDeviceId = param.getStringOrEmpty(KEY_DEVICE_BLE_DEVICE_ID)
                }
                param.checkAndUpdate(KEY_DEVICE_SSID) {
                    ssid = param.getStringOrEmpty(KEY_DEVICE_SSID)
                }
            }
            if (isDeviceExist) {
                SmartDatabase.getDatabase().deviceDao().updateBleDevice(deviceEntity)
            } else {
                SmartDatabase.getDatabase().deviceDao().insertBleDevice(deviceEntity)
            }
        } catch(e : Exception) {
        }
    }

    suspend fun getAllBleIpcDevice(uid : String) : MutableList<BleDeviceEntity>  {
        try {
            return SmartDatabase.getDatabase().deviceDao().getAllBleDevices(uid)
        } catch (e : Exception) {
        }
        return mutableListOf()
    }

    suspend fun getBleIpcDevice(uid : String, deviceId : String) : BleDeviceEntity?  {
        try {
            return SmartDatabase.getDatabase().deviceDao().getBleDevice(uid, deviceId)
        } catch (e : Exception) {
        }
        return null
    }

    suspend fun deleteBleIpcDevice(uid : String, deviceId : String)  {
        try {
            SmartDatabase.getDatabase().deviceDao().getBleDevice(uid, deviceId)?.let {
                SmartDatabase.getDatabase().deviceDao().deleteBleDevices(it)
            }
        } catch (e : Exception) {
        }
    }

    fun Bundle.checkAndUpdate(key : String, block : () -> Unit) {
        if (key.isNotEmpty() && containsKey(key)) {
            block.invoke()
        }
    }

    fun Bundle.getStringOrEmpty(key : String): String {
        return getString(key).orEmpty()
    }
}