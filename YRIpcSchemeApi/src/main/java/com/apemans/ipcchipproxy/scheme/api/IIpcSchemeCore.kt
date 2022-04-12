/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.api

import com.apemans.ipcchipproxy.scheme.cache.IIpcConfigureCache
import com.apemans.ipcchipproxy.scheme.cache.IIpcConnectionCache

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/11 12:02 下午
 * 说明:
 *
 * 备注: Ipc方案api接口
 *
 ***********************************************************/
interface IIpcSchemeCore {

    /**
     * 初始化配置
     */
    fun init(param : Map<String, Any>, callback: IIpcSchemeResultCallback? = null)

    /**
     * 销毁配置
     */
    fun destroy(param : Map<String, Any>? = null, callback: IIpcSchemeResultCallback? = null)

    /**
     * 设备连接
     */
    fun connect(param : Map<String, Any>, callback: IIpcSchemeResultCallback? = null)

    /**
     * 断开设备连接
     */
    fun disconnect(param : Map<String, Any>, callback: IIpcSchemeResultCallback? = null)

    /**
     * 发送命令
     */
    fun sendCmd(cmd : String, callback: IIpcSchemeResultCallback? = null)

    /**
     * 获取Ipc配置缓存接口，详细请参考IIpcConfigureCache说明
     */
    fun configureCache() : IIpcConfigureCache?

    /**
     * 获取Ipc链接缓存，详细请参考IIpcConnectionCache说明
     */
    fun connectionCache() : IIpcConnectionCache?
}