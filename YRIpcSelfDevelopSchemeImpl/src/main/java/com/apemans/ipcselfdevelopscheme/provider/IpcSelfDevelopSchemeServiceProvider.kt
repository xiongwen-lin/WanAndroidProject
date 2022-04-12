/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeCore
import com.apemans.ipcchipproxy.services.IpcSelfDevelopSchemeService
import com.apemans.ipcchipproxy.services.path.PATH_IPC_SELF_DEVELOP_SCHEME
import com.apemans.ipcselfdevelopscheme.scheme.IpcSelfDevelopSchemeManager

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/11 4:15 下午
 * 说明:
 *
 * 备注: 自有Ipc方案接口实现类
 *
 ***********************************************************/
@Route(path = PATH_IPC_SELF_DEVELOP_SCHEME)
class IpcSelfDevelopSchemeServiceProvider : IpcSelfDevelopSchemeService {

    override fun init(context: Context?) {
    }

    override fun createIpcSchemeCore() : IIpcSchemeCore? {
        return IpcSelfDevelopSchemeManager
    }
}