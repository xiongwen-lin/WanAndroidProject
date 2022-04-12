/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.bean

import com.apemans.ipcchipproxy.scheme.bean.IpcDPCmdData

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/15 5:45 下午
 * 说明:
 *
 * 备注: 自有方案Ipc功能点命令数据模型
 *
 ***********************************************************/
class YRIpcDPCmdData<T> : IpcDPCmdData<T>() {

    var dpId : String? = null

}