package com.apemans.base

import android.app.Application
import com.apemans.base.middleservice.*
import com.apemans.base.utils.YRSystemUtils

/**
 * 基础服务组件
 */
class YRCXBaseKit : YRMiddleService() {

    override fun registerSelf(application : Application) {
        YRMiddleServiceManager.registerServiceClass("yrbase", YRCXBaseKit::class.java.name, "基础服务组件")
    }

    override fun requestAsync(protocol: MutableMap<String, String>?,
                              parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?) {
        listener?.onCall(dealMiddleEvent(protocol, parameters))
    }

    override fun request(protocol: MutableMap<String, String>?,
                         parameters: MutableMap<String, Any>?): YRMiddleServiceResponse<*> {
        return dealMiddleEvent(protocol, parameters)
    }

    override fun listening(protocol: MutableMap<String, String>?,
                           lifeCycle: Any?, parameters: MutableMap<String, Any>?,
                           listener: YRMiddleServiceListener?) {
    }

    private fun dealMiddleEvent(protocol: MutableMap<String, String>?,
                                parameters: MutableMap<String, Any>?) : YRMiddleServiceResponse<*> {
        if ("systemwifi" == protocol?.get(YRMiddleServiceFunctionName)) {
            val hashMap = HashMap<String, Any>()
            YRSystemUtils.gotoSystemWifiView()
            hashMap["result"] = true
            return okResponse(hashMap)
        } else if ("routerip" == protocol?.get(YRMiddleServiceFunctionName)) {
            val hashMap = HashMap<String, Any>()
            hashMap["result"] = true
            hashMap["routerip"] = YRSystemUtils.getSystemNetIp()
            return okResponse(hashMap)
        } else {
            return errorNoFunctionResponse()
        }
    }
}