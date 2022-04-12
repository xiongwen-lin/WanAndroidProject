/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.info

import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 09:54
 * 说明: 电工设备类信息
 *
 * 备注:
 *
 ***********************************************************/
open class EADeviceInfo(
    model: String,
    name: String,
    category: Int,
    sub_category: Int,
    device_id: String,
    platform: Int,
    icon_url: Any,
    brand: String,
    communication_type:Int
) : BaseDeviceInfo(model, name, category, sub_category,device_id, platform, icon_url, brand,communication_type),
    Serializable {

}