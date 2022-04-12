/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support.ea;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 23:37
 * 说明: EA电工类设备支持分类
 *
 * 备注:
 *
 ***********************************************************/
public @interface EASupportCategory {
    /*室内插座类型*/
    int indoor_plug_type = 1;
    /*室外插座类型*/
    int outdoor_plug_type = 2;
    /*通用开关类型*/
    int switch_common_type = 3;
    /*调光开关类型*/
    int switch_dimming_type = 4;
}
