/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipccontrolapi.services

import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 10:47 上午
 * 说明:
 *
 * 备注: Ipc控制模块开放api接口
 *
 ***********************************************************/
interface IpcControlManagerService : IProvider {

    /**
     * 根据方案类型，创建Ipc控制方案并返回统一Api接口
     */
    fun createIpcSchemeCore(type : Int) : IIpcSchemeCore?

}