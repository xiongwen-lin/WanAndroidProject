/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.custom.webapi

import com.apemans.business.apisdk.ApiManager

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/1
 * @desc:
 */
object CustomApiHelper {
    var customApi = ApiManager.getService(ICustomApi::class.java)
}