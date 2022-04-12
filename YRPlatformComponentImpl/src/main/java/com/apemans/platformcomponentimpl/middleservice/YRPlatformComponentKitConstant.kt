package com.apemans.platformcomponentimpl.middleservice

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 10:22 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

const val YR_MIDDLE_SERVICE_PARAM_EXTRA = "extra"

const val YR_MIDDLE_SERVICE_PARAM_DEVICE_ID = "deviceId"
const val YR_MIDDLE_SERVICE_PARAM_PARENT_DEVICE_ID = "parentDeviceId"
const val YR_MIDDLE_SERVICE_PARAM_DEVICE_MODEL = "deviceModel"
const val YR_MIDDLE_SERVICE_PARAM_DEVICE_SSID = "deviceSsid"
const val YR_MIDDLE_SERVICE_PARAM_BLE_DEVICE_ID = "bleDeviceId"
const val YR_MIDDLE_SERVICE_PARAM_UID = "uid"
const val YR_MIDDLE_SERVICE_PARAM_ACCOUNT = "account"
const val YR_MIDDLE_SERVICE_PARAM_CATEGORY = "category"

/** 平台模块的模块名称 */
const val YR_PLATFORM_COMPONENT_MODULE = "yrplatformcomponent"

/** 平台模块方法名称-获取全部设备信息，返回字典格式，该接口返回详细信息，包括云信息，过滤本地无效缓存等 */
const val YR_PLATFORM_COMPONENT_FUNCTION_TEST= "test"
const val YR_PLATFORM_COMPONENT_FUNCTION_QUERY_DEVICE = "querydevice"

/** 平台模块方法名称-获取当前直连信息 */
const val YR_PLATFORM_COMPONENT_FUNCTION_QUERY_NET_SPOT_DEVICE_INFO = "querynetspotdeviceinfo"