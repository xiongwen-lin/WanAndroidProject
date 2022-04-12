package com.apemans.tdprintercomponentimpl.utils

import com.apemans.tdprintercomponentimpl.bean.BasePlatformDevice
import com.apemans.tdprintercomponentimpl.constant.YR_PLATFORM_CATEGORY_OF_TD_PRINTER
import com.apemans.tdprintercomponentimpl.constant.YR_PLATFORM_KEY_EXTRA_CATEGORY
import com.apemans.tdprintercomponentimpl.constant.YR_PLATFORM_KEY_EXTRA_PLATFORM
import com.apemans.tdprintercomponentimpl.constant.YR_PLATFORM_OF_YRCX
import com.apemans.tdprintercomponentimpl.webapi.BindDevice

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/14 10:02 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRTDPrinterHelper {

    fun convertPlatformDevice(device: BindDevice): BasePlatformDevice {

        return BasePlatformDevice().apply {
            uuid = device.uuid.orEmpty()
            name = device.name.orEmpty()
            online = device.online == 1
            version = device.version.orEmpty()
            iconUrl = ""
            productId = ""
            platform = YR_PLATFORM_OF_YRCX
            category = YR_PLATFORM_CATEGORY_OF_TD_PRINTER
            extra = mapOf()
        }

    }
}