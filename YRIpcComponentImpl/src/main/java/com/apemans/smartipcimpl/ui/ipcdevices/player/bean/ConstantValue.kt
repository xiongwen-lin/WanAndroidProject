package com.apemans.smartipcimpl.ui.ipcdevices.player.bean

/**
 * @Author:dongbeihu
 * @Description:播放器 相关常量
 * @Date: 2021/12/2-15:17
 */

const val SUCCESS = "SUCCESS"
const val ERROR = "ERROR"

/**
 * 用户类型
 */
const val CONNECTION_MODE_NONE = 0

/**
 * 无线AP(路由器连接)
 */
const val CONNECTION_MODE_AP = 1

/**
 * 二维码连接
 */
const val CONNECTION_MODE_QC = 2

/**
 * 无线直连（类似手机wifi共享）
 */
const val CONNECTION_MODE_AP_DIRECT = 3

/**
 * 有线连接
 */
const val CONNECTION_MODE_LAN = 4

/**
 * 功能区，拍照、录像、说话等
 */
const val IPC_FUNCTION_TYPE_TAKE_PHOTO = 1
const val IPC_FUNCTION_TYPE_RECORD = 2
const val IPC_FUNCTION_TYPE_SPEAK = 3
const val IPC_FUNCTION_TYPE_SOUND = 4
const val IPC_FUNCTION_TYPE_ALARM = 5
const val IPC_FUNCTION_TYPE_DIRECTION = 6
const val IPC_FUNCTION_TYPE_PHOTO = 7
const val IPC_FUNCTION_TYPE_FLASH_LIGHT = 8
const val ITEM_COLOR_DEFAULT = -1

/**
 * 关键数据字段
 */


/**
 * 播放类型：直播、云回放、卡回放
 *
 */
const val NOOIE_PLAYBACK_TYPE_LIVE = 0
const val NOOIE_PLAYBACK_TYPE_SD = 1
const val NOOIE_PLAYBACK_TYPE_CLOUD = 2
const val NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL = 0
const val NOOIE_PLAYBACK_SOURCE_TYPE_DIRECT = 1


const val PLAY_DISPLAY_TYPE_NORMAL = 1
const val PLAY_DISPLAY_TYPE_DETAIL = 2


/**
 * 云回放参数
 */
const val CLOUD_PACK_PARAM_KEY_UUID = "uuid"
const val CLOUD_PACK_PARAM_KEY_MODEL = "model"
const val CLOUD_PACK_PARAM_KEY_ENTER_MARK = "enter_mark"
const val CLOUD_PACK_PARAM_KEY_ORIGIN = "origin"

/**
 * 云回放状态
 */
const val CLOUD_RECORD_REQUEST_NORMAL = 1
const val CLOUD_RECORD_REQUEST_MORE = 2


const val INTENT_NOLINE_DEVICE_LIST = "INTENT_NOLINE_DEVICE_LIST"

/**
 * 电源状态
 */
const val DEVICE_POWER_MODE_NORMAL = 1
const val DEVICE_POWER_MODE_LP_ACTIVE = 2
const val DEVICE_POWER_MODE_LP_SLEEP = 3


const val ROUTE_SOURCE_NORMAL = 0


/**
 * 手势动态控制相机
 */
const val GESTURE_MOVE_LEFT = 1
const val GESTURE_MOVE_TOP = 2
const val GESTURE_MOVE_RIGHT = 3
const val GESTURE_MOVE_BOTTOM = 4
const val GESTURE_TOUCH_DOWN = 5
const val GESTURE_TOUCH_UP = 6

const val AUDIO_STATE_ON = 1
const val AUDIO_STATE_OFF = 0

/**
 * 警报开关状态
 */
const val ALARM_AUDIO_STATE_ON = 1
const val ALARM_AUDIO_STATE_OFF = 0

/**
 * 闪光灯开关状态
 */
const val FLASH_LIGHT_STATE_ON = 1
const val FLASH_LIGHT_STATE_OFF = 0

/**
 * 低功耗休眠时间:秒
 */
const val LP_CAMERA_PLAY_LIMIT_TIME = 240

/**
 * 套餐下架了
 */
const val PACKAGE_INFO_STATUS_DELETE = 3
const val PACKAGE_INFO_STATUS_DEFAULT = 0
const val PACKAGE_INFO_STATUS_ON = 1
const val PACKAGE_INFO_STATUS_OFF = 2
