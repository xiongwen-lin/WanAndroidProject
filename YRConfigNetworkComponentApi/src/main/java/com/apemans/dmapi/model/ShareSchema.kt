/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 14:41
 * 说明: 分享属性
 *
 * 备注:
 *
 ***********************************************************/
class ShareSchema {
    /*是否是被分享*/
    var isShare = false

    /*分享时间*/
    var shared_time = 0L

    /*邀请人*/
    var invite_name = ""
}