/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.scheme

import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.cache.IIpcConfigureCache
import com.apemans.ipcchipproxy.scheme.cache.IIpcConnectionCache
import com.apemans.ipcselfdevelopscheme.cache.YRIpcConfigureCache
import com.apemans.ipcselfdevelopscheme.cache.YRIpcConnectionCache

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/11 4:28 下午
 * 说明:
 *
 * 备注: 自有Ipc方案管理类
 *
 ***********************************************************/
object IpcSelfDevelopSchemeManager : IIpcSchemeCore {

    /**
     * 自有Ipc方案初始化配置，根据不同协议传入对应参数，具体协议配置key说明如下
     * 自有P2P协议配置
     * IPC_SCHEME_KEY_P2P_URL：自有平台返回P2P链接地址
     * IPC_SCHEME_KEY_P2P_PORT：自有平台返回P2P链接端口
     * IPC_SCHEME_KEY_UID：自有平台返回账号uid
     */
    override fun init(param: Map<String, Any>, callback: IIpcSchemeResultCallback?) {
        var url : String? = param[IPC_SCHEME_KEY_P2P_URL] as? String
        var port = if (param[IPC_SCHEME_KEY_P2P_PORT] != null) param[IPC_SCHEME_KEY_P2P_PORT] as Int else 0
        var uid : String? = param[IPC_SCHEME_KEY_UID] as? String
        if (url.isNullOrEmpty() || uid.isNullOrEmpty()) {
            callback?.onError()
            return
        }
        YRIpcConnectionManager.initP2P(uid, url, port)
        callback?.onSuccess()
    }

    /**
     * 自有Ipc方案销毁配置
     */
    override fun destroy(param: Map<String, Any>?, callback: IIpcSchemeResultCallback?) {
        YRIpcConnectionManager.destroyP2P()
        callback?.onSuccess()
    }

    /**
     * 自有Ipc方案设备连接，根据不同协议传入对应参数，具体协议配置key说明如下
     * IPC_SCHEME_KEY_CONNECTION_PROTOCOL：连接设备所使用的协议类型，如P2P、热点TCP、热点P2P
     * IPC_SCHEME_KEY_DEVICE_ID：设备Id
     */
    override fun connect(param: Map<String, Any>, callback: IIpcSchemeResultCallback?) {
        var protocol = if (param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] != null) param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] as Int else 0
        when(protocol) {
            IPC_SCHEME_CONNECTION_PROTOCOL_P2P -> {
                var deviceId = param[IPC_SCHEME_KEY_DEVICE_ID] as? String
                if (deviceId.isNullOrEmpty()) {
                    callback?.onError()
                    return
                }
                YRIpcConnectionManager.connectP2P(deviceId)
                callback?.onSuccess()
            }
            IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP -> {
                var configureParam = param[IPC_SCHEME_KEY_CONNECTION_CONFIGURE] as? String
                var ssid = param[IPC_SCHEME_KEY_SSID] as? String
                if (configureParam.isNullOrEmpty() || ssid.isNullOrEmpty()) {
                    callback?.onError()
                    return
                }
                YRIpcConnectionManager.connectNetSpotTcp(configureParam, ssid, callback)
            }
            IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_P2P -> {
                var configureParam = param[IPC_SCHEME_KEY_CONNECTION_CONFIGURE] as? String
                var ssid = param[IPC_SCHEME_KEY_SSID] as? String
                var bleDeviceId = param[IPC_SCHEME_KEY_BLE_DEVICE_ID] as? String
                var model = param[IPC_SCHEME_KEY_MODEL] as? String
                if (configureParam.isNullOrEmpty() || ssid.isNullOrEmpty() || model.isNullOrEmpty()) {
                    callback?.onError()
                    return
                }
                YRIpcConnectionManager.connectNetSpotP2P(configureParam, ssid, bleDeviceId, model, callback)
            }
            else -> {
                callback?.onError()
            }
        }
    }

    /**
     * 自有Ipc方案设备断开连接，根据不同协议传入对应参数，具体协议配置key说明如下
     * IPC_SCHEME_KEY_CONNECTION_PROTOCOL：连接设备所使用的协议类型，如P2P、热点TCP、热点P2P
     * IPC_SCHEME_KEY_DEVICE_ID：设备Id
     */
    override fun disconnect(param: Map<String, Any>, callback: IIpcSchemeResultCallback?) {
        var protocol = if (param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] != null) param[IPC_SCHEME_KEY_CONNECTION_PROTOCOL] as Int else 0
        when(protocol) {
            IPC_SCHEME_CONNECTION_PROTOCOL_P2P -> {
                var deviceId = param[IPC_SCHEME_KEY_DEVICE_ID] as? String
                if (deviceId.isNullOrEmpty()) {
                    callback?.onError()
                    return
                }
                YRIpcConnectionManager.disconnectP2P(deviceId)
                callback?.onSuccess()
            }
            IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP, IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_P2P -> {
                var deviceId = param[IPC_SCHEME_KEY_DEVICE_ID] as? String
                if (deviceId.isNullOrEmpty()) {
                    callback?.onError()
                    return
                }
                YRIpcConnectionManager.disconnectNetSpot(deviceId)
                callback?.onSuccess()
            }
            else -> {
                callback?.onError()
            }
        }
    }

    /**
     * 自有Ipc方案命令发送，具体用法如下说明：
     * 根据定义好的功能点和命令所需参数，生成如下格式的json文本进行传送，并在回调接口接收命令执行结果
     * 发送命令json格式：
     * {"dpId" : {"deviceId" : "xxx", "data" : yyy}}
     * dpId:对应命令功能点，如"101"设置led指示灯开关
     * deviceId:设备id
     * data:命令所需参数，如true打开led指示灯
     * 示例如下：
     * {"101" : {"deviceId" : "123", "data" : true}}
     *
     * 命令执行结果数据模型IpcDPCmdResult
     * 命令执行结果格式：
     * {"deviceId" : "xxx", "data" : yyy}
     * deviceId:设备id
     * data:命令执行结果，内容为功能点和对应命令执行结果的键值对的json文本格式
     * {"dpId" : xxx} 如{"101":true} 设置led指示灯开
     * 示例如下：
     * {"deviceId":"55ffc4135dcc4fb1029a2c98e8096c22", "data":"{\"101\":true}"}
     */
    override fun sendCmd(cmd: String, callback: IIpcSchemeResultCallback?) {
        YRIpcCommandManager.sendCmd(cmd, callback)
    }

    /**
     * 获取自有Ipc配置缓存
     * 1、查询设备功能是否支持，通过功能点工具api接口IIpcSchemeDPTool来查询设备的功能是否支持
     */
    override fun configureCache(): IIpcConfigureCache {
        return YRIpcConfigureCache
    }

    /**
     * 获取Ipc链接缓存
     * 1、获取远程P2P链接缓存
     * 2、获取热点链接缓存
     */
    override fun connectionCache(): IIpcConnectionCache? {
        return YRIpcConnectionCache
    }
}