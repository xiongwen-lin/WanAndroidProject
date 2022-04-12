/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.cache

import com.apemans.ipcchipproxy.scheme.bean.DataPoint

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/15 2:16 下午
 * 说明:
 *
 * 备注: 功能点工具api接口
 *
 ***********************************************************/
interface IIpcSchemeDPTool {

    /**
     * 初始化功能点
     */
    fun initDps(dps : List<DataPoint>?) : IIpcSchemeDPTool

    /**
     * 添加功能点到列表
     */
    fun addDps(dps : List<DataPoint>?)

    /**
     * 根据功能点id获取功能点模型
     */
    fun getDp(dpId : String?) : DataPoint?

    /**
     * 根据功能点id判断是否支持该功能
     */
    fun isSupport(dpId : String?) : Boolean
}