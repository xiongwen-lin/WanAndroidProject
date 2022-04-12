/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.services

import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/11 3:54 下午
 * 说明:
 *
 * 备注: 自有Ipc方案开放api接口
 *
 ***********************************************************/
interface IpcSelfDevelopSchemeService : IProvider {

    /**
     * 创建自有Ipc方案，并返回统一方案api接口
     */
    fun createIpcSchemeCore() : IIpcSchemeCore?
}