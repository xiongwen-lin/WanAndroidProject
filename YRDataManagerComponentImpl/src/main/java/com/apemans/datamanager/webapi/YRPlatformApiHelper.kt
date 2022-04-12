/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.datamanager.webapi

import com.apemans.business.apisdk.ApiManager

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/13 7:09 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRPlatformApiHelper {

    var platformApi = ApiManager.getService(IYRPlatformApi::class.java)
}