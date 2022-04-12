/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.bean

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/15 5:45 下午
 * 说明:
 *
 * 备注: Ipc方案功能点命令返回结果
 *
 ***********************************************************/
class IpcDPCmdResult<T> {

    var deviceId : String? = null
    var data : T? = null

}