/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support.light;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 23:47
 * 说明: 照明支持分类
 *
 * 备注:
 *
 ***********************************************************/
public @interface LightSupportCategory {
    /*灯泡类型*/
    int bulb_type = 1;
    /*落地灯类型*/
    int floor_lamp_type = 2;
    /*灯带类型*/
    int strip_lights_type = 3;
}
