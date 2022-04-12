package com.apemans.platformcomponentimpl.middleservice

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 11:41 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRPlatformComponentKitUtil {

    fun parseParamByKey(params: Map<String, Any>?, key: String?) : String? {
        if (params == null || key.isNullOrEmpty()) {
            return ""
        }
        return if (params[key] is String) params[key] as? String else ""
    }

    fun parseParamForExtra(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_EXTRA)
    }

    fun parseParamForDeviceId(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_DEVICE_ID)
    }

    fun parseParamForParentDeviceId(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_PARENT_DEVICE_ID)
    }

    fun parseParamForDeviceModel(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_DEVICE_MODEL)
    }

    fun parseParamForDeviceSsid(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_DEVICE_SSID)
    }

    fun parseParamForBleDeviceId(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_BLE_DEVICE_ID)
    }

    fun parseParamForUid(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_UID)
    }

    fun parseParamForAccount(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_ACCOUNT)
    }

    fun parseParamForCategory(params: Map<String, Any>?) : String? {
        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_CATEGORY)
    }

//    fun parseParamFor(params: Map<String, Any>?) : String? {
//        return parseParamByKey(params, YR_MIDDLE_SERVICE_PARAM_)
//    }
}