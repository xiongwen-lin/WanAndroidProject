/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.bean

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 5:57 下午
 * 说明:
 *
 * 备注: Ipc配置基类
 *
 ***********************************************************/
open class BaseIpcConfigure {
    var deviceId : String? = ""
    var model : String? = ""
    var dps : Map<String, Any>? = null
}
