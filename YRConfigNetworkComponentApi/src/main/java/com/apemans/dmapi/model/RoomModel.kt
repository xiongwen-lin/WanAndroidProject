/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

import com.apemans.dmapi.info.FamilyInfo
import com.apemans.dmapi.info.RoomInfo
import com.apemans.dmapi.schema.OperationSchema
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 10:36
 * 说明:
 * 1：房间名称
 * 2：房间id
 * 3：房间创建时间
 *
 * 备注:
 ***********************************************************/
class RoomModel : Serializable {
    /*家庭基本信息*/
    var familyInfo: FamilyInfo? = null

    /*房间基本信息*/
    var roomInfo: RoomInfo? = null

    /*分享*/
    var shareSchema: ShareSchema? = null


    /*权限角色*/
    var operationSchema: OperationSchema? = null

    /**
     * 房间下分组列表
     */
    var groupList: MutableList<GroupModel> = mutableListOf()

    /*该房间下用户拥有的所有设备<包括自己的和他人分享给自己的>*/
    var userDeviceList: MutableList<UserDeviceModel> = mutableListOf()
}