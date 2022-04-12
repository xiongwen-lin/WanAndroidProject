/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:18
 * 说明: 设备支持品类
 *
 * 备注:
 *
 ***********************************************************/
@IntDef({DeviceSupportMainCategory.ipc,
        DeviceSupportMainCategory.EA,
        DeviceSupportMainCategory.light,
        DeviceSupportMainCategory.router
})
@Retention(RetentionPolicy.SOURCE)
public @interface DeviceSupportMainCategory {
    //安防监控IPC
    int ipc = 1;
    //电工
    int EA = 2;
    //照明
    int light = 3;
    //路由器
    int router = 4;
}
