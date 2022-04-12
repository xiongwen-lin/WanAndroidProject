/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apemans.ipcchipproxy.define.IPC_SCHEME_KEY_DPS
import com.apemans.ipcchipproxy.scheme.bean.DataPoint

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 6:53 下午
 * 说明:
 *
 * 备注: Ipc配置缓存基类
 *
 ***********************************************************/
abstract class BaseIpcConfigureCache : IIpcConfigureCache {

    private var configureCache : MutableMap<String, Map<String, Any>> = mutableMapOf()

    override fun addDeviceConfigure(key: String, data: Map<String, Any>) {
        if (configureCache == null) {
            configureCache = mutableMapOf()
        }
        if (key.isNotEmpty()) {
            configureCache[key] = data
        }
    }

    override fun getDeviceConfigure(deviceId: String): Map<String, Any>? {
        return if (isExist(deviceId)) configureCache[deviceId] else null
    }

    override fun removeDeviceConfigures(deviceIds: List<String>) {
        deviceIds.map {
            if (isExist(it)) {
                configureCache.remove(it)
            }
        }
    }

    override fun removeDeviceConfigure(deviceId: String) {
        if (isExist(deviceId)) {
            configureCache.remove(deviceId)
        }
    }

    override fun clearDeviceConfigure() {
        configureCache.clear()
    }

    override fun isExist(deviceId: String): Boolean {
        return !deviceId.isNullOrEmpty() && configureCache.containsKey(deviceId)
    }

    override fun getDpTool(deviceId: String): IIpcSchemeDPTool? {
        if (!isExist(deviceId)) {
            return null
        }
        return getDeviceConfigure(deviceId)?.let {
            var dps : String? = it[IPC_SCHEME_KEY_DPS] as? String
            return IpcSchemeDPTool().initDps(convertDataPoint(dps))
        }
    }

    /**
     * 将功能点由json文本转化为DataPoint的列表
     */
    private fun convertDataPoint(dps : String?) : List<DataPoint>? {
        if (dps == null || dps.isEmpty()) {
            return null
        }
        try {
            return Gson().fromJson<List<DataPoint>>(dps, object : TypeToken<List<DataPoint>>(){}.type)
        } catch (e : Exception) {
        }
        return null
    }
}