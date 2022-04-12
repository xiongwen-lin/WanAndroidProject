/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipccontrolcomponent.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.ipccontrolapi.define.IPC_SCHEME_TYPE_SELF_DEVELOP
import com.apemans.ipccontrolapi.services.IpcControlManagerService
import com.apemans.ipccontrolapi.services.path.PATH_IPC_CONTROL_MANAGER
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore
import com.apemans.ipcchipproxy.services.IpcSelfDevelopSchemeService
import com.apemans.router.routerServices

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 11:03 上午
 * 说明:
 *
 * 备注: Ipc控制模块开放api接口实现
 *
 ***********************************************************/
@Route(path = PATH_IPC_CONTROL_MANAGER)
class IpcControlManagerServiceProvider : IpcControlManagerService {

    private val ipcSelfDevelopSchemeService : IpcSelfDevelopSchemeService by routerServices()

    override fun init(context: Context?) {
    }

    /**
     * 根据方案类型，创建Ipc控制方案并返回统一Api接口
     * 支持方案类型如下：
     * IPC_SCHEME_TYPE_SELF_DEVELOP：自有Ipc方案
     */
    override fun createIpcSchemeCore(type: Int): IIpcSchemeCore? {
        return when(type) {
            IPC_SCHEME_TYPE_SELF_DEVELOP -> ipcSelfDevelopSchemeService.createIpcSchemeCore()
            else -> null
        }
    }
}