package com.apemans.platformcomponentimpl.middleservice

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.base.middleservice.*
import com.apemans.platformcomponentimpl.repository.YRPlatformRepository
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 10:05 上午
 * 说明:
 *
 * 备注:
 *
 */
class YRCXPlatformComponentKit : YRMiddleService() {

    override fun registerSelf(application: Application) {
        YRMiddleServiceManager.registerServiceClass(YR_PLATFORM_COMPONENT_MODULE, YRCXPlatformComponentKit::class.java.name, "平台服务组件");
    }

    override fun requestAsync(protocol: MutableMap<String, String>?, parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?) {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_PLATFORM_COMPONENT_FUNCTION_TEST, protocol)) {
            var deviceSsid = YRPlatformComponentKitUtil.parseParamForDeviceSsid(parameters)
            if (deviceSsid.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            listener?.onCall(okResponse(""))
        } else if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_PLATFORM_COMPONENT_FUNCTION_QUERY_NET_SPOT_DEVICE_INFO, protocol)) {
            listener?.onCall(okResponse(JsonConvertUtil.convertToJson(YRPlatformRepository.queryNetSpotDeviceInfo())))
        } else {
            listener?.onCall(errorParametersResponse(""))
        }
    }

    override fun request(protocol: MutableMap<String, String>?, parameters: MutableMap<String, Any>?): YRMiddleServiceResponse<*>? {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_PLATFORM_COMPONENT_FUNCTION_TEST, protocol)) {
            var connectionInfoJson = YRPlatformComponentKitUtil.parseParamForExtra(parameters)
            if (connectionInfoJson.isNullOrEmpty()) {
                return errorParametersResponse("")
            }
            var responseResult = null
            return okResponse(responseResult)
        } else {
            return errorNoFunctionResponse()
        }
    }

    override fun listening(protocol: MutableMap<String, String>?, lifeCycle: Any?, parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?) {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_PLATFORM_COMPONENT_FUNCTION_QUERY_DEVICE, protocol)) {
            val lifecycleOwner = lifeCycle as? LifecycleOwner
            var lfc = lifecycleOwner?.lifecycle
            if (lfc == null) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            var uid = YRPlatformComponentKitUtil.parseParamForUid(parameters).orEmpty()
            var category = YRPlatformComponentKitUtil.parseParamForCategory(parameters).orEmpty()

            lfc.coroutineScope.launch {
                YRPlatformRepository.queryDevice(uid, category)
                    .flowOn(Dispatchers.IO)
                    .catch {
                        emit(listOf())
                    }
                    .collect {
                        listener?.onCall(okResponse(JsonConvertUtil.convertToJson(it)))
                    }
            }
        } else {
            val lifecycleOwner = lifeCycle as? LifecycleOwner
            var lfc = lifecycleOwner?.lifecycle
            if (lfc == null) {
                listener?.onCall(errorNoFunctionResponse())
                return
            }
        }
    }

}