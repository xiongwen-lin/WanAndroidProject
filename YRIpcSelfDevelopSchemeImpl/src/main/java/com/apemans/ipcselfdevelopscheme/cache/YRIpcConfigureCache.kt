/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.cache

import com.nooie.sdk.device.bean.ConnType
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.cache.BaseIpcConfigureCache
import com.apemans.ipcselfdevelopscheme.bean.YRIpcConfigure
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN
import com.apemans.ipcselfdevelopscheme.util.YRIpcDeviceUtil

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 7:10 下午
 * 说明:
 *
 * 备注: 自有Ipc配置缓存类
 *
 ***********************************************************/
object YRIpcConfigureCache : BaseIpcConfigureCache() {

    override fun updateDeviceConfigure(data: Map<String, Any>) {
        if (!data.containsKey(IPC_SCHEME_KEY_DEVICE_ID) || data[IPC_SCHEME_KEY_DEVICE_ID] == null || (data[IPC_SCHEME_KEY_DEVICE_ID] as String).isEmpty()) {
            return
        }
        var key = data[IPC_SCHEME_KEY_DEVICE_ID] as String
        var configure : MutableMap<String, Any> = getDeviceConfigure(key).orEmpty().toMutableMap()
        configure[IPC_SCHEME_KEY_DEVICE_ID] = key
        if (data.containsKey(IPC_SCHEME_KEY_MODEL) && data[IPC_SCHEME_KEY_MODEL] != null) {
            configure[IPC_SCHEME_KEY_MODEL] = data[IPC_SCHEME_KEY_MODEL] as String
        }

        if (data.containsKey(IPC_SCHEME_KEY_DPS) && data[IPC_SCHEME_KEY_DPS] != null) {
            configure[IPC_SCHEME_KEY_DPS] = data[IPC_SCHEME_KEY_DPS] as String
        }

        if (data.containsKey(IPC_SCHEME_KEY_UID) && data[IPC_SCHEME_KEY_UID] != null) {
            configure[IPC_SCHEME_KEY_UID] = data[IPC_SCHEME_KEY_UID] as String
        }

        if (data.containsKey(IPC_SCHEME_KEY_SERVER_DOMAIN) && data[IPC_SCHEME_KEY_SERVER_DOMAIN] != null) {
            configure[IPC_SCHEME_KEY_SERVER_DOMAIN] = data[IPC_SCHEME_KEY_SERVER_DOMAIN] as String
        }

        if (data.containsKey(IPC_SCHEME_KEY_SERVER_PORT) && data[IPC_SCHEME_KEY_SERVER_PORT] != null) {
            configure[IPC_SCHEME_KEY_SERVER_PORT] = data[IPC_SCHEME_KEY_SERVER_PORT] as Int
        }

        if (data.containsKey(IPC_SCHEME_KEY_SECRET) && data[IPC_SCHEME_KEY_SECRET] != null) {
            configure[IPC_SCHEME_KEY_SECRET] = data[IPC_SCHEME_KEY_SECRET] as String
        }
        if (data.containsKey(IPC_SCHEME_KEY_PARENT_DEVICE_ID)) {
            configure[IPC_SCHEME_KEY_PARENT_DEVICE_ID] = data[IPC_SCHEME_KEY_PARENT_DEVICE_ID] as? String ?: ""
        }
        configure[IPC_SCHEME_KEY_MODEL_TYPE] = YRIpcDeviceUtil.convertModelType(data[IPC_SCHEME_KEY_MODEL_TYPE] as? Int)
        configure[IPC_SCHEME_KEY_CONN_TYPE] = YRIpcDeviceUtil.convertConnType(data[IPC_SCHEME_KEY_CONN_TYPE] as? Int)
        configure[IPC_SCHEME_KEY_CMD_TYPE] = YRIpcDeviceUtil.convertCmdType(data[IPC_SCHEME_KEY_CMD_TYPE] as? Int)
        addDeviceConfigure(key, configure)
    }

    /**
     * 返回自有Ipc配置
     */
    fun getIpcConfigure(deviceId : String) : YRIpcConfigure? {
        return getDeviceConfigure(deviceId)?.let { configure ->
            YRIpcConfigure().apply {
                this.deviceId = configure[IPC_SCHEME_KEY_DEVICE_ID] as? String
                model = configure[IPC_SCHEME_KEY_MODEL] as? String
                dps = configure[IPC_SCHEME_KEY_DPS] as? Map<String, Any>
                uid = configure[IPC_SCHEME_KEY_UID] as? String
                serverDomain = configure[IPC_SCHEME_KEY_SERVER_DOMAIN] as? String
                serverPort = if (configure[IPC_SCHEME_KEY_SERVER_PORT] != null) configure[IPC_SCHEME_KEY_SERVER_PORT] as Int else 0
                secret = configure[IPC_SCHEME_KEY_SECRET] as? String
                modelType = if (configure[IPC_SCHEME_KEY_MODEL_TYPE] != null) configure[IPC_SCHEME_KEY_MODEL_TYPE] as Int else 0
                connType = configure[IPC_SCHEME_KEY_CONN_TYPE] as? ConnType
                cmdType = if (configure[IPC_SCHEME_KEY_CMD_TYPE] != null) configure[IPC_SCHEME_KEY_CMD_TYPE] as Int else IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN
                parentDeviceId = configure[IPC_SCHEME_KEY_PARENT_DEVICE_ID] as? String
            }
        }
    }
}