/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

import com.apemans.dmapi.info.FamilyInfo
import com.apemans.dmapi.info.GroupInfo
import com.apemans.dmapi.info.RoomInfo
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 10:31
 * 说明: 群组数据模型
 * 1：群组名称
 * 2：群组id
 * 3：群组创建时间
 *
 * 备注:
 *
 ***********************************************************/
class GroupModel : Serializable {
    /*Group信息*/
    var groupInfo:GroupInfo?=null

    /*房间基本信息*/
    var roomInfo: RoomInfo? = null

    /*家庭基本信息*/
    var familyInfo: FamilyInfo? = null

    /*该分组下用户拥有的所有设备<包括自己的和他人分享给自己的>*/
    var userDeviceList: MutableList<UserDeviceModel> = mutableListOf()
}