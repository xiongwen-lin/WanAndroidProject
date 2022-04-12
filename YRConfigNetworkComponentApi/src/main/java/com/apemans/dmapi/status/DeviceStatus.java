/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.status;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 09:47
 * 说明: 设备状态
 *
 * 备注:
 *
 ***********************************************************/
public @interface DeviceStatus {
    int offline = 0;
    int online = 1;
}
