/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.model

import com.apemans.dmapi.schema.OperationSchema
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 11:29
 * 说明: 用户设备信息
 *
 * 备注:
 *
 ***********************************************************/
class UserDeviceModel : Serializable {
    /*所有者ID<Accout>*/
    var owner_id: String? = null

    /**
     * 家庭Id
     */
    var family_id: String? = null

    /**
     * 房间Home Id
     */
    var home_id: String? = null

    /**
     * 分组group Id
     */
    var group_id: String? = null

    /*设备*/
    var deviceModel: DeviceModel? = null

    /*定位信息*/
    var location: LocationModel? = null

    /*分享*/
    var shareSchema: ShareSchema? = null

    /*权限角色*/
    var operationSchema: OperationSchema? = null

    /*设备在线状态*/
    var isOnline = false

}