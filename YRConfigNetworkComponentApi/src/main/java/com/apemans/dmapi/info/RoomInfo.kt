/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/3 10:14
 * 说明: 房间基本信息
 *
 * 备注:
 *
 ***********************************************************/
class RoomInfo {
    /*所有者ID<Accout>*/
    var owner_id: String? = null

    /**
     * 房间id 如果不存在房间名，则房间id默认为0
     */
    var room_id: String? = null

    /**
     * 房间名称 如果不存在房间名，则默认为root
     */
    var room_name: String? = null

    /**
     * Room创建时间
     * Unix 单位
     */
    var create_time = 0L
}