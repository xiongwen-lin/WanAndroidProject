/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

import com.apemans.dmapi.info.FamilyInfo
import com.apemans.dmapi.schema.OperationSchema
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 10:31
 * 说明: 家庭数据模型
 * 1：家庭名称
 * 2：家庭id
 * 3：家庭创建时间
 * 3：家庭下房间列表
 *
 * 备注:
 *
 ***********************************************************/
class FamilyModel : Serializable {

    /*家庭基本信息*/
    var familyInfo: FamilyInfo? = null

    /*定位信息*/
    var location: LocationModel? = null

    /*分享*/
    var shareSchema: ShareSchema? = null

    /*权限角色*/
    var operationSchema: OperationSchema? = null

    /*该家庭下所有房间列表*/
    var roomList: MutableList<RoomModel> = mutableListOf()

    /*该家庭下所有分组列表*/
    var groupList: MutableList<GroupModel> = mutableListOf()

    /*该家庭下用户拥有的所有设备<包括自己的和他人分享给自己的>*/
    var userDeviceList: MutableList<UserDeviceModel> = mutableListOf()

}