/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.scheme.cache

import com.apemans.ipcchipproxy.scheme.bean.DataPoint

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/15 2:11 下午
 * 说明:
 *
 * 备注: 功能点工具api接口实现类
 *
 ***********************************************************/
class IpcSchemeDPTool : IIpcSchemeDPTool {

    var dpList : MutableList<DataPoint>? = null

    override fun initDps(dps: List<DataPoint>?): IIpcSchemeDPTool {
        addDps(dps)
        return this
    }

    override fun addDps(dps: List<DataPoint>?) {
        if (dpList == null) {
            dpList = mutableListOf()
        }
        dpList?.clear()
        if (!dps.isNullOrEmpty()) {
            dpList?.addAll(dps)
        }
    }

    override fun getDp(dpId: String?): DataPoint? {
        if (dpId == null || dpId.isEmpty()) {
            return null
        }
        return dpList?.find {
            it.code == dpId
        }
    }

    override fun isSupport(dpId: String?): Boolean {
        return getDp(dpId) != null
    }

}