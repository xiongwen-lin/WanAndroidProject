/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.customserviceapi.webapi

/**
 * @Auther:
 * @datetime: 2021/11/1
 * @desc:
 */
/**
 * 请求消息Body类
 */
data class RequestMessageBody(
    var rows: Int,
    var time: Int,
    var type: Int
)