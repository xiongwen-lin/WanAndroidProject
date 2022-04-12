/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcchipproxy.define

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/12 7:16 下午
 * 说明:
 *
 * 备注: Ipc方案常量
 *
 ***********************************************************/

const val IPC_SCHEME_KEY_P2P_URL = "IPC_SCHEME_KEY_P2P_URL"
const val IPC_SCHEME_KEY_P2P_PORT = "IPC_SCHEME_KEY_P2P_PORT"

const val IPC_SCHEME_KEY_DEVICE_ID = "IPC_SCHEME_KEY_DEVICE_ID"
const val IPC_SCHEME_KEY_PARENT_DEVICE_ID = "IPC_SCHEME_KEY_PARENT_DEVICE_ID"
const val IPC_SCHEME_KEY_MODEL = "IPC_SCHEME_KEY_MODEL"
const val IPC_SCHEME_KEY_DPS = "IPC_SCHEME_KEY_DPS"

const val IPC_SCHEME_KEY_UID = "IPC_SCHEME_KEY_UID"
const val IPC_SCHEME_KEY_SERVER_DOMAIN = "IPC_SCHEME_KEY_SERVER_DOMAIN"
const val IPC_SCHEME_KEY_SERVER_PORT = "IPC_SCHEME_KEY_SERVER_PORT"
const val IPC_SCHEME_KEY_SECRET = "IPC_SCHEME_KEY_SECRET"
const val IPC_SCHEME_KEY_MODEL_TYPE = "IPC_SCHEME_KEY_MODEL_TYPE"
const val IPC_SCHEME_KEY_CONN_TYPE = "IPC_SCHEME_KEY_CONN_TYPE"
const val IPC_SCHEME_KEY_CMD_TYPE = "IPC_SCHEME_KEY_CMD_TYPE"
const val IPC_SCHEME_KEY_SSID = "IPC_SCHEME_KEY_SSID"
const val IPC_SCHEME_KEY_BLE_DEVICE_ID = "IPC_SCHEME_KEY_BLE_DEVICE_ID"

const val IPC_SCHEME_KEY_CONNECTION_PROTOCOL = "IPC_SCHEME_KEY_CONNECTION_PROTOCOL"
const val IPC_SCHEME_KEY_CONNECTION_CONFIGURE = "IPC_SCHEME_KEY_CONNECTION_CONFIGURE"

/**
 * IPC_SCHEME_DP_CODE：产品功能的简单编码，一般为整数型，例如 1、2、101。设备与云端的功能数据通过 DP ID 进行传输。
 * IPC_SCHEME_DP_NAME：产品功能的名称，可以用中文、英文、日语、法语等语言表示，仅用做平台展示。
 * IPC_SCHEME_DP_FLAG：功能的字符串表示，又称为产品 dpcode，用于有多语言能力的应用程序中，例如移动应用。支持字母、数字和下划线，以字母开头。
 * IPC_SCHEME_DP_DATA_TYPE：定义了产品功能的取值的数据类型
 * IPC_SCHEME_DP_VALUES：功能属性，进一步明确 数值型 产品功能的数值取值范围、数值间距和单位。
 * IPC_SCHEME_DP_AUTHORITY：定义了数据交互的方向。分为以下几种：可上报可下发(rw)、只下发(w)、只上报(r)
 * IPC_SCHEME_DP_DESC：备注
 * IPC_SCHEME_DP_STATE：状态，如设备led指示灯的开或关：1或0
 *{
   "code": "101",
   "name": "开关led指示灯",
   "flag": "switch_led",
   "type": "Boolean",
   "values": "{}",
   "authority": "rw",
   "desc": "开关led指示灯"
   "state" : 1
   }
 *
 */
const val IPC_SCHEME_DP_CODE = "code"
const val IPC_SCHEME_DP_NAME = "name"
const val IPC_SCHEME_DP_FLAG = "flag"
const val IPC_SCHEME_DP_DATA_TYPE = "type"
const val IPC_SCHEME_DP_VALUES = "values"
const val IPC_SCHEME_DP_AUTHORITY = "authority"
const val IPC_SCHEME_DP_DESC = "desc"
const val IPC_SCHEME_DP_STATE = "state"

/** Ipc P2P 远程连接协议类型 **/
const val IPC_SCHEME_CONNECTION_PROTOCOL_P2P = 1
/** Ipc TCP 热点连接协议类型 **/
const val IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_TCP = 2
/** Ipc P2P 热点连接协议类型 **/
const val IPC_SCHEME_CONNECTION_PROTOCOL_NET_SPOT_P2P = 3

/** Ipc功能授权，R表示获取，W表示设置 **/
const val IPC_SCHEME_DP_AUTHORITY_R = "R"
const val IPC_SCHEME_DP_AUTHORITY_W = "W"

/**
 * IPC_SCHEME_DP_SWITCH_LED 设备Led灯功能（设置和查询）
 * IPC_SCHEME_DP_DEVICE_SETTING 查询设备设置功能
 * IPC_SCHEME_DP_AUDIO_RECORD 设备声音录制功能（设置和查询）
 * IPC_SCHEME_DP_ROTATE 设备画面旋转功能（设置和查询）
 * IPC_SCHEME_DP_LOOP_RECORD 设备循坏录制功能（设置和查询）
 * IPC_SCHEME_DP_SLEEP 设备休眠功能（设置和查询）
 * IPC_SCHEME_DP_DETECT_HUMAN 设备人形侦测功能（设置和查询）
 * IPC_SCHEME_DP_DETECT_CRY 设备哭声侦测开关功能（设置和查询）
 * IPC_SCHEME_DP_WATER_MARK 设备水印功能（设置和查询）
 * IPC_SCHEME_DP_ENERGY_SAVING_MODE 设备节能模式功能（设置和查询）
 * IPC_SCHEME_DP_FLASH_LIGHT_STATE 设备白光灯功能（设置和查询）
 * IPC_SCHEME_DP_IR 设备红外灯功能（设置和查询）
 * IPC_SCHEME_DP_LIGHT_MODE 设备灯光模式功能（设置和查询）
 * IPC_SCHEME_DP_TIME_CONFIGURE 设备同步时间功能
 * IPC_SCHEME_DP_STORAGE_INFO 设备存储信息查询功能
 * IPC_SCHEME_DP_FORMAT_STORAGE 设备存储格式化
 * IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_DATE 设备录像日期查询功能
 * IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO 设备录像数据查询功能
 * IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_DATE 设备截图日期查询功能
 * IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_INFO 设备截图数据查询功能
 * IPC_SCHEME_DP_ALARM_STATE 设备警报功能设置
 * IPC_SCHEME_DP_DETECTION_AREA_INFO 设备侦测区域信息功能（设置和查询）
 * IPC_SCHEME_DP_UPGRADE 设备升级功能
 * IPC_SCHEME_DP_RESET_DEVICE 设备重置功能
 * IPC_SCHEME_DP_REBOOT_DEVICE 设备重启功能
 * IPC_SCHEME_DP_SAVE_ROTATE_PRESET_POINT 设备开机预设点添加功能
 * IPC_SCHEME_DP_TURN_ROTATE_PRESET_POINT 设备旋转开机预设点功能
 * IPC_SCHEME_DP_POWER_ROTATE_PRESET_POINT 设备开机预设点设置功能
 * IPC_SCHEME_DP_MEDIA_MODE_INFO 设备保存文件模式功能功能（设置和查询）
 * IPC_SCHEME_DP_NET_SPOT_INFO 设备热点信息功能（设置和查询）
 * IPC_SCHEME_DP_DETECTION_INFO 设备侦测信息查询功能
 * IPC_SCHEME_DP_MOTION_DETECTION_INFO 设备移动侦测信息设置功能
 * IPC_SCHEME_DP_SOUND_DETECTION_INFO 设备声音侦测查询功能
 * IPC_SCHEME_DP_PIR_DETECTION_INFO 设备人形侦测查询功能
 * IPC_SCHEME_DP_MOTION_DETECTION_PLAN 设备移动侦测计划设置功能
 * IPC_SCHEME_DP_SOUND_DETECTION_PLAN 设备声音侦测计划设置功能
 * IPC_SCHEME_DP_PIR_DETECTION_PLAN 设备人形侦测计划设置功能
 * IPC_SCHEME_DP_SOUND_STATE 设备声音输出查询功能
 * IPC_SCHEME_DP_MOTION_TRACK 设备移动追踪功能（设置和查询）
 * IPC_SCHEME_DP_UNBIND_DEVICE 设备解绑功能
 *
 * IPC_SCHEME_DP_START_AP_PAIR 设备ap热点配网功能
 * IPC_SCHEME_DP_GET_AP_PAIR_INFO 设备ap热点配网状态查询
 * IPC_SCHEME_DP_SET_USER_INFO 设备直连初始化设置功能
 * IPC_SCHEME_DP_SEND_HEART_BEAT 设备发送心跳功能
 * IPC_SCHEME_DP_SNAPSHOT 设备播放器截图功能
 * IPC_SCHEME_DP_RECORDING 设备播放器录像功能
 * IPC_SCHEME_DP_TALK 设备播放器麦克风功能
 * IPC_SCHEME_DP_AUDIO 设备播放器喇叭功能
 * IPC_SCHEME_DP_ROTATION_DIRECTION_LEFT_RIGHT 设备播放器左右旋转功能
 * IPC_SCHEME_DP_ROTATION_DIRECTION_UP_DOWN 设备播放器上下旋转功能
 */
const val IPC_SCHEME_DP_SWITCH_LED = "101"
const val IPC_SCHEME_DP_DEVICE_SETTING = "102"
const val IPC_SCHEME_DP_AUDIO_RECORD = "103"
const val IPC_SCHEME_DP_ROTATE = "104"
const val IPC_SCHEME_DP_LOOP_RECORD = "105"
const val IPC_SCHEME_DP_SLEEP = "106"
const val IPC_SCHEME_DP_DETECT_HUMAN = "107"
const val IPC_SCHEME_DP_DETECT_CRY = "108"
const val IPC_SCHEME_DP_WATER_MARK = "109"
const val IPC_SCHEME_DP_ENERGY_SAVING_MODE = "110"
const val IPC_SCHEME_DP_FLASH_LIGHT_STATE = "111"
const val IPC_SCHEME_DP_IR = "112"
const val IPC_SCHEME_DP_LIGHT_MODE = "113"
const val IPC_SCHEME_DP_TIME_CONFIGURE = "114"
const val IPC_SCHEME_DP_STORAGE_INFO = "115"
const val IPC_SCHEME_DP_FORMAT_STORAGE = "116"
const val IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_DATE = "117"
const val IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO = "118"
const val IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_DATE = "119"
const val IPC_SCHEME_DP_STORAGE_RECORD_IMAGE_INFO = "120"
const val IPC_SCHEME_DP_ALARM_STATE = "121"
const val IPC_SCHEME_DP_DETECTION_AREA_INFO = "122"
const val IPC_SCHEME_DP_UPGRADE = "123"
const val IPC_SCHEME_DP_RESET_DEVICE = "124"
const val IPC_SCHEME_DP_REBOOT_DEVICE = "125"
const val IPC_SCHEME_DP_SAVE_ROTATE_PRESET_POINT = "126"
const val IPC_SCHEME_DP_TURN_ROTATE_PRESET_POINT = "127"
const val IPC_SCHEME_DP_POWER_ROTATE_PRESET_POINT = "128"
const val IPC_SCHEME_DP_MEDIA_MODE_INFO = "129"
const val IPC_SCHEME_DP_NET_SPOT_INFO = "130"
const val IPC_SCHEME_DP_DETECTION_INFO = "131"
const val IPC_SCHEME_DP_MOTION_DETECTION_INFO = "132"
const val IPC_SCHEME_DP_SOUND_DETECTION_INFO = "133"
const val IPC_SCHEME_DP_PIR_DETECTION_INFO = "134"
const val IPC_SCHEME_DP_MOTION_DETECTION_PLAN = "135"
const val IPC_SCHEME_DP_SOUND_DETECTION_PLAN = "136"
const val IPC_SCHEME_DP_PIR_DETECTION_PLAN = "137"
const val IPC_SCHEME_DP_SOUND_STATE = "138"
const val IPC_SCHEME_DP_MOTION_TRACK = "139"
const val IPC_SCHEME_DP_UNBIND_DEVICE = "140"

const val IPC_SCHEME_DP_START_AP_PAIR = "1001"
const val IPC_SCHEME_DP_GET_AP_PAIR_INFO = "1002"
const val IPC_SCHEME_DP_SET_USER_INFO = "1003"
const val IPC_SCHEME_DP_SEND_HEART_BEAT = "1004"

const val IPC_SCHEME_DP_SNAPSHOT = "2001"
const val IPC_SCHEME_DP_RECORDING = "2002"
const val IPC_SCHEME_DP_TALK = "2003"
const val IPC_SCHEME_DP_AUDIO = "2004"
const val IPC_SCHEME_DP_ROTATION_DIRECTION_LEFT_RIGHT = "2005"
const val IPC_SCHEME_DP_ROTATION_DIRECTION_UP_DOWN = "2006"

/**
 * Ipc 命令请求结果
 * IPC_SCHEME_CMD_STATE_CODE_SUCCESS 命令请求成功
 * IPC_SCHEME_CMD_STATE_CODE_NOT_SUPPORT 命令不支持
 * IPC_SCHEME_CMD_STATE_CODE_ERROR 命令请求失败
 */
const val IPC_SCHEME_CMD_STATE_CODE_SUCCESS = 1
const val IPC_SCHEME_CMD_STATE_CODE_NOT_SUPPORT = 2
const val IPC_SCHEME_CMD_STATE_CODE_ERROR = 3

/**
 * Ipc侦测设置操作类型，每一种代表进行设置不同功能
 * IPC_SCHEME_DETECTION_OPERATION_TYPE_SWITCH 开关设置
 * IPC_SCHEME_DETECTION_OPERATION_TYPE_SENSITIVITY 灵敏度设置
 * IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN 侦测计划设置
 * IPC_SCHEME_DETECTION_OPERATION_TYPE_ALARM 低功耗设备警报设置
 * IPC_SCHEME_DETECTION_OPERATION_TYPE_LIGHT 低功耗设备闪光灯设置
 */
const val IPC_SCHEME_DETECTION_OPERATION_TYPE_SWITCH = 1
const val IPC_SCHEME_DETECTION_OPERATION_TYPE_SENSITIVITY = 2
const val IPC_SCHEME_DETECTION_OPERATION_TYPE_PLAN = 3
const val IPC_SCHEME_DETECTION_OPERATION_TYPE_ALARM = 4
const val IPC_SCHEME_DETECTION_OPERATION_TYPE_LIGHT = 5

/**
 * Ipc侦测设置侦测类型，每一种代表进行设置不同侦测功能
 * IPC_SCHEME_DETECTION_TYPE_MOTION 移动侦测功能
 * IPC_SCHEME_DETECTION_TYPE_SOUND 声音侦测功能
 * IPC_SCHEME_DETECTION_TYPE_PIR 人形（PIR）侦测功能
 */
const val IPC_SCHEME_DETECTION_TYPE_MOTION = 1
const val IPC_SCHEME_DETECTION_TYPE_SOUND = 2
const val IPC_SCHEME_DETECTION_TYPE_PIR = 3

/**
 * Ipc侦测灵敏度
 * IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE 移动侦测或声音侦测关闭
 * IPC_SCHEME_SENSITIVITY_LEVEL_LOW 侦测灵敏度低
 * IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE 侦测灵敏度中
 * IPC_SCHEME_SENSITIVITY_LEVEL_HIGH 侦测灵敏度高
 */
const val IPC_SCHEME_SENSITIVITY_LEVEL_CLOSE = 0
const val IPC_SCHEME_SENSITIVITY_LEVEL_LOW = 1
const val IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE = 2
const val IPC_SCHEME_SENSITIVITY_LEVEL_HIGH = 3

/**
 * Ipc 灯光模式
 * IPC_SCHEME_LIGHT_MODE_AUTO 自动模式
 * IPC_SCHEME_LIGHT_MODE_OFF 关闭
 * IPC_SCHEME_LIGHT_MODE_IR 红外模式
 * IPC_SCHEME_LIGHT_MODE_COLOR 全彩模式
 */
const val IPC_SCHEME_LIGHT_MODE_AUTO = 0
const val IPC_SCHEME_LIGHT_MODE_OFF = 1
const val IPC_SCHEME_LIGHT_MODE_IR = 2
const val IPC_SCHEME_LIGHT_MODE_COLOR = 3

/**
 * Ipc 白光灯模式
 * IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE 关闭
 * IPC_SCHEME_FLASH_LIGHT_MODE_COLOR 全彩模式
 * IPC_SCHEME_FLASH_LIGHT_MODE_ALARM 警报模式
 */
const val IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE = 0
const val IPC_SCHEME_FLASH_LIGHT_MODE_COLOR = 1
const val IPC_SCHEME_FLASH_LIGHT_MODE_ALARM = 2

/**
 * Ipc 储存卡状态
 * IPC_SCHEME_STORAGE_STATUS_NORMAL 存储卡正常
 * IPC_SCHEME_STORAGE_STATUS_FORMATTING 存储卡格式化中
 * IPC_SCHEME_STORAGE_STATUS_NO_SD 存储卡不存在
 * IPC_SCHEME_STORAGE_STATUS_DAMAGE 存储卡损坏
 */
const val IPC_SCHEME_STORAGE_STATUS_NORMAL = 0
const val IPC_SCHEME_STORAGE_STATUS_FORMATTING = 1
const val IPC_SCHEME_STORAGE_STATUS_NO_SD = 2
const val IPC_SCHEME_STORAGE_STATUS_DAMAGE= 3

/**
 * Ipc 拍照模式（如打猎相机直连的拍照模式）
 * IPC_SCHEME_SHOOTING_VIDEO 图片模式
 * IPC_SCHEME_SHOOTING_IMAGE 视频模式
 * IPC_SCHEME_SHOOTING_VIDEO_IMAGE 视频和图片模式
 */
const val IPC_SCHEME_SHOOTING_VIDEO = 0
const val IPC_SCHEME_SHOOTING_IMAGE = 1
const val IPC_SCHEME_SHOOTING_VIDEO_IMAGE = 2

/**
 * ap热点配网状态
 */
const val IPC_SCHEME_NET_SPOT_PAIR_UNKNOWN = 0
// 未收到配⽹网信息
const val IPC_SCHEME_NET_SPOT_PAIR_NO_RECV_WIFI = 1
// 收到配⽹网信息
const val IPC_SCHEME_NET_SPOT_PAIR_RECVED_WIFI = 2
// 正在连接wifi
const val IPC_SCHEME_NET_SPOT_PAIR_CONNECTING_WIFI = 3
// 连接wifi成功
const val IPC_SCHEME_NET_SPOT_PAIR_CONN_WIFI_SUCC = 4
// 连接wifi失败
const val IPC_SCHEME_NET_SPOT_PAIR_CONN_WIFI_FAILED = 5
// 正在上线
const val IPC_SCHEME_NET_SPOT_PAIR_START_ONLINE = 6
// 获取网络时间成功
const val IPC_SCHEME_NET_SPOT_PAIR_GOT_NET_TIME = 7
// p2p连接成功
const val IPC_SCHEME_NET_SPOT_PAIR_P2P_CONNECTED = 8
// 上线失败
const val IPC_SCHEME_NET_SPOT_PAIR_ONLINE_FAILED = 9
// 上线成功
const val IPC_SCHEME_NET_SPOT_PAIR_ONLINE_SUCCESS = 10

/**
 * Ipc ap热点配网类型
 * IPC_SCHEME_AP_CONN_TYPE_P2P P2P协议类型
 * IPC_SCHEME_AP_CONN_TYPE_TCP TCP协议类型
 * IPC_SCHEME_AP_CONN_TYPE_HTTP HTTP协议类型
 */
const val IPC_SCHEME_AP_CONN_TYPE_P2P = 1
const val IPC_SCHEME_AP_CONN_TYPE_TCP = 2
const val IPC_SCHEME_AP_CONN_TYPE_HTTP = 3

/**
 * Ipc 热点连接参数
 * IPC_SCHEME_NET_SPOT_AP_DEVICE_ID 热点统一设备Id
 * IPC_SCHEME_NET_SPOT_SERVER_DOMAIN 热点P2P连接ip
 * IPC_SCHEME_NET_SPOT_SERVER_PORT 热点P2P连接端口
 */
const val IPC_SCHEME_NET_SPOT_AP_DEVICE_ID = "victure_ap"
const val IPC_SCHEME_NET_SPOT_SERVER_DOMAIN = "192.168.43.1"
const val IPC_SCHEME_NET_SPOT_SERVER_PORT = 23000

/**
 * Ipc 设备兼容ssid前缀
 */
const val IPC_SCHEME_NET_SPOT_SSID_PREFIX_VICTURE = "victure_"
const val IPC_SCHEME_NET_SPOT_SSID_PREFIX_SECURITY_CAM = "securitycam_"
const val IPC_SCHEME_NET_SPOT_SSID_PREFIX_TECKIN = "teckin_"
const val IPC_SCHEME_NET_SPOT_SSID_PREFIX_OSAIO = "osaio_"