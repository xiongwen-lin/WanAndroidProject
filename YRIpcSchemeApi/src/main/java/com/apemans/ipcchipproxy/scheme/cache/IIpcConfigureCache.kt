/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.cache

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 6:51 下午
 * 说明:
 *
 * 备注: Ipc配置缓存接口
 *
 ***********************************************************/
interface IIpcConfigureCache {

    /**
     * 添加配置
     */
    fun addDeviceConfigure(key : String, data : Map<String, Any>)

    /**
     * 更新配置
     */
    fun updateDeviceConfigure(data : Map<String, Any>)

    /**
     * 获取配置
     */
    fun getDeviceConfigure(deviceId : String) : Map<String, Any>?

    /**
     * 移除配置
     */
    fun removeDeviceConfigures(deviceIds : List<String>)

    /**
     * 移除配置
     */
    fun removeDeviceConfigure(deviceId : String)

    /**
     * 清除全部配置
     */
    fun clearDeviceConfigure()

    /**
     * 根据设备id判断配置是否已存在
     */
    fun isExist(deviceId : String) : Boolean

    /**
     * 根据设备id返回功能点工具api接口, 详细请参考IIpcSchemeDPTool说明
     */
    fun getDpTool(deviceId : String) : IIpcSchemeDPTool?
}