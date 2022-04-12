package com.apemans.smartipcapi.middleservice

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.apemans.ipcchipproxy.define.IPC_SCHEME_DP_GET_AP_PAIR_INFO
import com.apemans.ipcchipproxy.define.IPC_SCHEME_NET_SPOT_PAIR_UNKNOWN
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.util.parseIpcDPCmdResult
import com.apemans.router.routerServices
import com.apemans.smartipcapi.bean.IpcSendCmdResult
import com.apemans.smartipcapi.services.IpcDeviceManagerService
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.base.middleservice.*
import com.google.gson.reflect.TypeToken

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 10:05 上午
 * 说明:
 *
 * 备注:
 *
 */
class YRCXIpcComponentKit : YRMiddleService() {

    private val ipcDeviceManagerService by routerServices<IpcDeviceManagerService>()

    override fun registerSelf(application: Application) {
        YRMiddleServiceManager.registerServiceClass(YR_IPC_COMPONENT_MODULE_DEVICE, YRCXIpcComponentKit::class.java.name, "Ipc服务组件");
    }

    override fun requestAsync(protocol: MutableMap<String, String>?, parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?) {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_START_NET_SPOT_TCP, protocol)) {
            var deviceSsid = YRIpcComponentKitUtil.parseParamForDeviceSsid(parameters)
            if (deviceSsid.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.startNetSpotTcp(deviceSsid) {
                listener?.onCall(okResponse(it))
            }
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_START_NET_SPOT_P2P, protocol)) {
            var deviceSsid = YRIpcComponentKitUtil.parseParamForDeviceSsid(parameters)
            var bleDeviceId = YRIpcComponentKitUtil.parseParamForBleDeviceId(parameters)
            var deviceModel = YRIpcComponentKitUtil.parseParamForDeviceModel(parameters)
            if (deviceSsid.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.startNetSpotP2p(deviceSsid, bleDeviceId, deviceModel) {
                listener?.onCall(okResponse(it))
            }
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_REMOVE_NET_SPOT, protocol)) {
            var deviceId = YRIpcComponentKitUtil.parseParamForDeviceId(parameters)
            if (deviceId.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.removeNetSpot(deviceId) {
                listener?.onCall(okResponse(it))
            }
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_SEND_START_NET_SPOT_PAIR_CMD, protocol)) {
            var configureJson = YRIpcComponentKitUtil.parseParamForExtra(parameters)
            if (configureJson.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.sendStartNetSpotPairCmd(configureJson, object : IIpcSchemeResultCallback {
                override fun onSuccess(code: Int, result: String?) {
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                    })))
                }

                override fun onError(code: Int, error: String?) {
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                    })))
                }
            })
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_SEND_GET_NET_SPOT_PAIR_STATE_CMD, protocol)) {
            ipcDeviceManagerService.sendGetNetSpotPairStateCmd(object : IIpcSchemeResultCallback {
                override fun onSuccess(code: Int, result: String?) {
                    var state = parseIpcDPCmdResult(result, IPC_SCHEME_DP_GET_AP_PAIR_INFO) {
                        object : TypeToken<Map<String, Int>>(){}
                    } ?: IPC_SCHEME_NET_SPOT_PAIR_UNKNOWN
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                        data = state
                    })))
                }

                override fun onError(code: Int, error: String?) {
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                    })))
                }
            })
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_SEND_HEART_BEAT_CMD, protocol)) {
            var deviceId = YRIpcComponentKitUtil.parseParamForDeviceId(parameters)
            if (deviceId.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.sendHeartBeatCmd(deviceId, object : IIpcSchemeResultCallback {
                override fun onSuccess(code: Int, result: String?) {
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                    })))
                }

                override fun onError(code: Int, error: String?) {
                    listener?.onCall(okResponse(JsonConvertUtil.convertToJson(IpcSendCmdResult<Int>().apply {
                        this.code = code
                    })))
                }
            })
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_UPDATE_NET_SPOT_CONFIGURE, protocol)) {
            var uid = YRIpcComponentKitUtil.parseParamForUid(parameters)
            if (uid.isNullOrEmpty()) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            ipcDeviceManagerService.updateNetSpotDeviceConfigure(uid) {
                listener?.onCall(okResponse(it))
            }
        } else {
            listener?.onCall(errorParametersResponse(""))
        }
    }

    override fun request(protocol: MutableMap<String, String>?, parameters: MutableMap<String, Any>?): YRMiddleServiceResponse<*>? {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_START_P2P_CONNECTION, protocol)) {
            var connectionInfoJson = YRIpcComponentKitUtil.parseParamForExtra(parameters)
            if (connectionInfoJson.isNullOrEmpty()) {
                return errorParametersResponse("")
            }
            var responseResult = ipcDeviceManagerService.startP2PConnection(connectionInfoJson)
            return okResponse(responseResult)
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_REMOVE_P2P_CONNECTION, protocol)) {
            var deviceId = YRIpcComponentKitUtil.parseParamForDeviceId(parameters)
            if (deviceId.isNullOrEmpty()) {
                return errorParametersResponse("")
            }
            var responseResult = ipcDeviceManagerService.removeP2PConnection(deviceId)
            return okResponse(true)
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_CHECK_P2P_CONNECTION_EXIST, protocol)) {
            var deviceId = YRIpcComponentKitUtil.parseParamForDeviceId(parameters)
            if (deviceId.isNullOrEmpty()) {
                return errorParametersResponse("")
            }
            var responseResult = ipcDeviceManagerService.checkP2PConnectionExist(deviceId)
            return okResponse(responseResult)
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_CHECK_IS_NET_SPOT_MODE, protocol)) {
            var responseResult = ipcDeviceManagerService.checkIsNetSpotMode()
            return okResponse(responseResult)
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_GET_NET_SPOT_DEVICE_INFO, protocol)) {
            var responseResult = ipcDeviceManagerService.getNetSpotDeviceInfo()
            return okResponse(JsonConvertUtil.convertToJson(responseResult))
        } else if(YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_CHECK_NET_SPOT_SSID_VALID, protocol)) {
            var deviceSsid = YRIpcComponentKitUtil.parseParamForDeviceSsid(parameters)
            if (deviceSsid.isNullOrEmpty()) {
                return errorParametersResponse("")
            }
            var responseResult = ipcDeviceManagerService.checkNetSpotSsidValid(deviceSsid)
            return okResponse(responseResult)
        } else {
            return errorNoFunctionResponse()
        }
    }

    override fun listening(protocol: MutableMap<String, String>?, lifeCycle: Any?, parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?) {
        if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_GET_ALL_IPC_DEVICE, protocol)) {
            val lifecycleOwner = lifeCycle as? LifecycleOwner
            var lfc = lifecycleOwner?.lifecycle
            if (lfc == null) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            var uid = YRIpcComponentKitUtil.parseParamForUid(parameters)
            ipcDeviceManagerService.queryIpcDevice(lfc, uid.orEmpty(), listener)
        } else if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_GET_ALL_IPC_DEVICE_AS_MAP, protocol)) {
            val lifecycleOwner = lifeCycle as? LifecycleOwner
            var lfc = lifecycleOwner?.lifecycle
            if (lfc == null) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            var uid = YRIpcComponentKitUtil.parseParamForUid(parameters)
            ipcDeviceManagerService.queryIpcDeviceListAsMap(lfc, uid.orEmpty(), listener)
        } else if (YRMiddleServiceUtil.checkServiceFunctionNameAssignable(YR_IPC_COMPONENT_FUNCTION_GET_IPC_STORAGE_INFO, protocol)) {
            val lifecycleOwner = lifeCycle as? LifecycleOwner
            var lfc = lifecycleOwner?.lifecycle
            if (lfc == null) {
                listener?.onCall(errorParametersResponse(""))
                return
            }
            var deviceId = YRIpcComponentKitUtil.parseParamForDeviceId(parameters)
            ipcDeviceManagerService.queryIpcDeviceStorageInfo(lfc, deviceId.orEmpty(), listener)
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