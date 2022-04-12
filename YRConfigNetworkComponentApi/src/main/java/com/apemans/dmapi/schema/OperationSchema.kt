/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.schema

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 15:20
 * 说明: 权限操作/角色描述Schema
 *
 * 备注:
 *
 ***********************************************************/
class OperationSchema {
    /*用户角色：默认admin*/
    var role = -1

    /*权限等级-涉及是否是被分享，操作权限*/
    var perm_level = -1
}