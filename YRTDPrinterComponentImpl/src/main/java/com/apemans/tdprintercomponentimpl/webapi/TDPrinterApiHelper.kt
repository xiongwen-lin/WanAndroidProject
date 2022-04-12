package com.apemans.tdprintercomponentimpl.webapi

import com.apemans.business.apisdk.ApiManager


/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/10 6:44 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object TDPrinterApiHelper {

    val tdPrinterApi = ApiManager.getService(ITDPrinterApi::class.java)

}