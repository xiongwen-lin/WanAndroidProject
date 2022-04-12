/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.api

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/18 12:00 下午
 * 说明:
 *
 * 备注: Ipc方案api回调接口
 *
 ***********************************************************/
interface IIpcSchemeResultCallback {

    fun onSuccess(code: Int = 0, result : String? = null)

    fun onError(code : Int = 0, error : String? = null)
}