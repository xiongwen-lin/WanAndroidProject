/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

import androidx.annotation.IntDef;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 00:14
 * 说明: 支持平台
 *
 * 备注:
 *
 ***********************************************************/
@IntDef({
        SupportPlatform.tuyaProtocolPlatform,
        SupportPlatform.selfDevelopProtocolPlatform
})
public @interface SupportPlatform {
    /*涂鸦协议平台*/
    int tuyaProtocolPlatform = 1;
    /*自研协议平台*/
    int selfDevelopProtocolPlatform = 2;
}
