/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.bean

import com.nooie.sdk.device.bean.ConnType
import com.apemans.ipcchipproxy.scheme.bean.BaseIpcConfigure
import com.apemans.ipcselfdevelopscheme.define.IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 6:00 下午
 * 说明:
 *
 * 备注: 自有Ipc配置类
 *
 ***********************************************************/
class YRIpcConfigure : BaseIpcConfigure() {
    var uid : String? = ""
    var serverDomain : String? = ""
    var serverPort : Int = 0
    var secret : String? = ""
    var modelType : Int = 0
    var connType : ConnType? = null
    var cmdType : Int = IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN
    var parentDeviceId: String? = ""
}
