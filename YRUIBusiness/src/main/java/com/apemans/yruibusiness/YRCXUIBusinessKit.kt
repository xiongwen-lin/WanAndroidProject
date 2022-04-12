package com.apemans.yruibusiness

import android.app.Application
import android.content.Intent
import com.apemans.base.middleservice.*
import com.dylanc.longan.activityList
import com.dylanc.longan.finishAllActivities
import com.dylanc.longan.topActivity

/**
 * UI 服务组件
 */
class YRCXUIBusinessKit : YRMiddleService() {
    override fun registerSelf(application: Application?) {
        YRMiddleServiceManager.registerServiceClass("yruibusiness", YRCXUIBusinessKit::class.java.name, "UI服务组件")
    }

    override fun requestAsync(
        protocol: MutableMap<String, String>?,
        parameters: MutableMap<String, Any>?,
        listener: YRMiddleServiceListener?
    ) {
        listener?.onCall(dealMiddleEvent(protocol, parameters))
    }

    override fun request(
        protocol: MutableMap<String, String>?,
        parameters: MutableMap<String, Any>?
    ): YRMiddleServiceResponse<*> {
        return dealMiddleEvent(protocol, parameters)
    }

    override fun listening(
        protocol: MutableMap<String, String>?,
        lifeCycle: Any?,
        parameters: MutableMap<String, Any>?,
        listener: YRMiddleServiceListener?
    ) {
    }

    private fun dealMiddleEvent(protocol: MutableMap<String, String>?,
                                parameters: MutableMap<String, Any>?) : YRMiddleServiceResponse<*> {
        if ("pop" == protocol?.get(YRMiddleServiceFunctionName)) {
            topActivity.finish()
            val hashMap = HashMap<String, Any>()
            hashMap["result"] = true
            return okResponse(hashMap)
        } else if ("rootview" == protocol?.get(YRMiddleServiceFunctionName)) {
            val intent = Intent(topActivity, activityList.first()::class.java)
            topActivity.startActivity(intent)
            finishAllActivities()
            val hashMap = HashMap<String, Any>()
            hashMap["result"] = true
            return okResponse(hashMap)
        } else {
            return errorNoFunctionResponse()
        }
    }
}