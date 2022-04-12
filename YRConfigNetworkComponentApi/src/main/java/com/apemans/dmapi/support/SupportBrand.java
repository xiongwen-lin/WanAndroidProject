/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

import androidx.annotation.StringDef;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:23
 * 说明: 品牌支持定义，链接到对应的设备上
 *
 * 备注:
 * 品牌适配-->如支持Osaio品牌，Victure品牌，Apeman品牌，Teckin品牌
 *
 ***********************************************************/
@StringDef({
        SupportBrand.osaio_brand
})
public @interface SupportBrand {
    String osaio_brand = "Osaio";
}
