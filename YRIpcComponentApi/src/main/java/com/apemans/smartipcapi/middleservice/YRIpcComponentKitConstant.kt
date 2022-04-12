package com.apemans.smartipcapi.middleservice

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

/** Ipc模块的模块名称 */
const val YR_IPC_COMPONENT_MODULE_DEVICE = "yripccomponentdevice"

/** Ipc模块方法名称-进行p2p连接 */
const val YR_IPC_COMPONENT_FUNCTION_START_P2P_CONNECTION = "startp2pconnection"
/** Ipc模块方法名称-移除p2p连接 */
const val YR_IPC_COMPONENT_FUNCTION_REMOVE_P2P_CONNECTION = "removep2pconnection"
/** Ipc模块方法名称-判断p2p连接是否存在 */
const val YR_IPC_COMPONENT_FUNCTION_CHECK_P2P_CONNECTION_EXIST = "checkp2pconnectionexist"
/** Ipc模块方法名称-进行tcp协议直连 */
const val YR_IPC_COMPONENT_FUNCTION_START_NET_SPOT_TCP = "startnetspottcp"
/** Ipc模块方法名称-进行p2p协议直连 */
const val YR_IPC_COMPONENT_FUNCTION_START_NET_SPOT_P2P = "startnetspotp2p"
/** Ipc模块方法名称-断开直连 */
const val YR_IPC_COMPONENT_FUNCTION_REMOVE_NET_SPOT = "removenetspot"
/** Ipc模块方法名称-判断直连是否存在 */
const val YR_IPC_COMPONENT_FUNCTION_CHECK_IS_NET_SPOT_MODE = "checkisnetspotmode"
/** Ipc模块方法名称-获取当前直连信息 */
const val YR_IPC_COMPONENT_FUNCTION_GET_NET_SPOT_DEVICE_INFO = "getnetspotdeviceinfo"
/** Ipc模块方法名称-发送ap热点配网命令 */
const val YR_IPC_COMPONENT_FUNCTION_SEND_START_NET_SPOT_PAIR_CMD = "sendstartnetspotpaircmd"
/** Ipc模块方法名称-发送获取ap热点配网状态命令 */
const val YR_IPC_COMPONENT_FUNCTION_SEND_GET_NET_SPOT_PAIR_STATE_CMD = "sendgetnetspotpairstatecmd"
/** Ipc模块方法名称-发送心跳命令 */
const val YR_IPC_COMPONENT_FUNCTION_SEND_HEART_BEAT_CMD = "sendheartbeatcmd"
/** Ipc模块方法名称-更新直连配置信息 */
const val YR_IPC_COMPONENT_FUNCTION_UPDATE_NET_SPOT_CONFIGURE = "updatenetspotdeviceconfigure"
/** Ipc模块方法名称-判断设备ssid是否合法 */
const val YR_IPC_COMPONENT_FUNCTION_CHECK_NET_SPOT_SSID_VALID = "checknetspotssidvalid"

/** Ipc模块方法名称-获取全部Ipc设备信息，返回字典格式，该接口返回详细信息，包括云信息，过滤本地无效缓存等 */
const val YR_IPC_COMPONENT_FUNCTION_GET_ALL_IPC_DEVICE = "queryIpcDevice"
/** Ipc模块方法名称-依次获取本地和远程全部Ipc设备信息，返回字典格式，该接口快速动态获取设备信息 */
const val YR_IPC_COMPONENT_FUNCTION_GET_ALL_IPC_DEVICE_AS_MAP = "queryIpcDeviceAsMap"
/** Ipc模块方法名称-获取全部Ipc设备存储信息 */
const val YR_IPC_COMPONENT_FUNCTION_GET_IPC_STORAGE_INFO = "queryIpcStorageInfo"