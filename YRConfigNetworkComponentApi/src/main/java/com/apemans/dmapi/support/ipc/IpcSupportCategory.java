/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support.ipc;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:23
 * 说明: IPC类设备支持分类
 *
 * 备注:
 *
 ***********************************************************/
@Retention(AnnotationRetention.SOURCE)
public @interface IpcSupportCategory {
    /*Indoor camera 室内卡片摄像机类型*/
    int indoor_card_camera_type = 1;

    /*indoor camera pan 室内摇头摄像机类型*/
    int indoor_pan_camera_type = 2;

    /*indoor camera MINI 室内mini摄像机类型*/
    int indoor_mini_camera_type = 3;

    /*camera_battery 电池摄像机类型*/
    int camera_battery_type = 4;

    /*outdoor camera 户外摄像机类型*/
    int outdoor_camera_type = 5;

    /*outdoor camera 户外摄像机类型*/
    int ipc_hub_type = 5;
}
