package com.apemans.datamanager

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.apemans.base.middleservice.*
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.datamanager.repository.YRPlatformRepository
import kotlinx.coroutines.launch

class YRSmartHomeKit : YRMiddleService() {

//    private val serviceList = mapOf(
//        "yrtuya" to listOf("querydevice"),
//        "yrplatformcomponent" to listOf("querydevice", "querynetspotdeviceinfo"),
//    )

    private val serviceList = listOf("yrtuya", "yrplatform")
    private val yrCategoryList = listOf("yripccomponentdevice")

    override fun registerSelf(application: Application) {
        YRMiddleServiceManager.registerServiceClass("yrsmarthomekitservice", YRSmartHomeKit::class.java.name, "数据中心组件")
    }

    override fun requestAsync(protocol: MutableMap<String, String>, parameters: MutableMap<String, Any>, listener: YRMiddleServiceListener) {
        when (protocol.functionName) {
            "querynetspotdeviceinfo" -> {
                serviceList.forEach {
                    if (it.contains("querynetspotdeviceinfo")) {
                        YRMiddleServiceManager.requestAsync("yrcx://$it/querynetspotdeviceinfo", parameters, listener)
                    }
                }
            }
            else -> listener.onCall(errorNoFunctionResponse())
        }
    }

    override fun request(protocol: MutableMap<String, String>, parameters: MutableMap<String, Any>): YRMiddleServiceResponse<*> {
        return errorNoFunctionResponse()
    }

    override fun listening(
        protocol: MutableMap<String, String>,
        lifecycleOwner: Any,
        parameters: MutableMap<String, Any>,
        listener: YRMiddleServiceListener
    ) {
        if (lifecycleOwner !is LifecycleOwner) {
            listener.onCall(errorParametersResponse(""))
            return
        }
        when (protocol.functionName) {
            "querydevice" -> {
                serviceList.forEach {
                    if (it == "yrplatform") {
                        lifecycleOwner.lifecycle.coroutineScope.launch {
                            val response = YRPlatformRepository.queryDevice()
                            val responseSuccess = response?.code == HttpCode.SUCCESS_CODE
                            val list = response?.data?.data

//                            yrCategoryList.forEach {
                            YRMiddleServiceManager.requestAsync(
                                "yrcx://yripccomponentdevice/transformipcdevice", mapOf(
                                    "uid" to uid,
                                    "account" to "",
                                    "extra" to JsonConvertUtil.convertToJson(mapOf("responseSuccess" to responseSuccess, "result" to list))!!
                                ), listener
                            )
//                            }
                        }
                    } else {
                        YRMiddleServiceManager.listening("yrcx://$it/querydevice", lifecycleOwner, parameters, listener)
                    }
                }
            }
            else -> {
                listener.onCall(errorNoFunctionResponse())
            }
        }
    }

    private fun MutableMap<String, Any>.getString(key: String) = (get(key) as? String).orEmpty()

    private val MutableMap<String, String>.functionName
        get() = get(YRMiddleServiceFunctionName).orEmpty().lowercase()

    private val uid: String
        get() = ""
}