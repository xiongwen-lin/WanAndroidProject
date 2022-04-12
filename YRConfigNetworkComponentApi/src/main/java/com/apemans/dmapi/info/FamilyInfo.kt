/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/3 10:08
 * 说明: 家庭基本信息
 *
 * 备注:
 *
 ***********************************************************/
class FamilyInfo {
    /*所有者ID<Accout>*/
    var owner_id: String? = null

    /**
     * 房间id
     */
    var family_id: String? = null

    /**
     * 房间名称
     */
    var family_name: String? = null

    /**
     * 家庭创建时间
     * Unix 单位
     */
    var create_time = 0L
}