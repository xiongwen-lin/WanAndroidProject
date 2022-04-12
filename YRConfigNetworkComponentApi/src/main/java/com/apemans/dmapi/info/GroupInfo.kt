/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/3 10:06
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class GroupInfo {
    /*所有者ID<Accout>*/
    var owner_id: String? = null

    /**
     * 群组id
     */
    var group_id: String? = null

    /**
     * 群组名称
     */
    var group_name: String? = null

    /**
     * 群组创建时间
     * Unix 单位
     */
    var create_time = 0L
}