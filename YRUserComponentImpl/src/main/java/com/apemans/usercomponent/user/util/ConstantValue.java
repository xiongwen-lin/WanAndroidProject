package com.apemans.usercomponent.user.util;

import android.Manifest;

/**
 * Created by victor on 2018/6/26
 * Email is victor.qiao.0604@gmail.com
 */
public class ConstantValue {
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_ERROR = 0;

    //app account brand
    public static final String APP_TAG_PREFIX = "Gncc";
    public static final String APP_ACCOUNT_BRAND_CURRENT = "osaio";
    public static final String APP_ACCOUNT_BRAND_VICTURE = "victure";
    public static final String APP_ACCOUNT_BRAND_TECKIN = "teckin";
    public static final String APP_NAME_OF_BRAND_VICTURE = "Victure Home";
    public static final String APP_NAME_OF_BRAND_TECKIN = "Teckin Home";

    //Configure
    public static final String UMENG_APP_KEY = "60e9043772748106e47cb25e";
    public static final String UMENG_APP_SECRET = "673b6c00b4e90cbc0f9658b7694abc73";

    // Url
    public static final String URL_USER_MANUAL = "https://www.nooie.com/app-user-manual-%s";
    public static final String URL_CONDITION_OF_USE = "https://www.govicture.com/privacy-policy-%s";
    public static final String URL_PRIVACY_NOTICE = "https://www.govicture.com/blank-1";
    public static final String URL_FAQ = "https://www.nooie.com/app-faq-%s";
    public static final String URL_USER_MANUAL_VICTURE = "http://www.nooie.com/app-user-manual-720";
    public static final String AMAZON_VICTURE_IPC = "https://www.amazon.com/-/zh/dp/B07MG46PTD/ref=sr_1_2?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99&dchild=1&keywords=victure+baby+monitor&qid=1585105258&sr=8-2";
    public static final String AMAZON_VICTURE_GATEWAY = "https://www.amazon.com/dp/B085TLC7CS?ref=myi_title_dp";
    public static final String URL_PRIVACY_POLICY = "https://www.gncchome.com/privacy-policy-%s";
    public static final String URL_TERMS = "https://www.gncchome.com/terms-%s";
    public static final String URL_PRIVACY_POLICY_TEMPLATE = "https://www.gncchome.com/privacy-policy-%s-%s";

    // email
    public static final String EMAIL_TO = "noo@apemans.com";
    //public static final String EMAIL_TO = "qiaofei@apemans.com";
    public static final String EMAIL_HOST = "smtp.sina.com";
    public static final String EMAIL_ACCOUNT = "apemans@sina.com";
    public static final String EMAIL_PASSWORD = "apemans";
    public static final String EMAIL_PORT = "465";
    public static final String EMAIL_SENDER_NAME = "android";
    public static final String EMAIL_FEEDBACK = "feedback";

    // Broadcast
    public static final String BROADCAST_KEY_REMOVE_CAMERA = "BROADCAST.KEY.REMOVE.CAMERA";
    public static final String BROADCAST_KEY_UPDATE_CAMERA = "BROADCAST.KEY.UPDATE.CAMERA";
    public static final String BROADCAST_KEY_RECEIVE_PUSH = "BROADCAST_KEY_RECEIVE_PUSH";
    public static final String BROADCAST_KEY_RECEIVE_JG_PUSH = "BROADCAST_KEY_RECEIVE_JG_PUSH";
    public static final String BROADCAST_KEY_RECEIVE_FCM_PUSH = "BROADCAST_KEY_RECEIVE_FCM_PUSH";
    public static final String BROADCAST_KEY_RECEIVE_SHARE_AGREE = "BROADCAST_KEY_RECEIVE_SHARE_AGREE";
    public static final String BROADCAST_KEY_RECEIVE_UPDATE_SHARE_DATA = "BROADCAST_KEY_RECEIVE_UPDATE_SHARE_DATA";
    public static final String BROADCAST_KEY_NOOIE_SERVICE_STATE = "BROADCAST_KEY_NOOIE_SERVICE_STATE";

    // Intent key
    public static final String INTENT_KEY_RECEIVE_IS_SYSTEM_PUSH = "INTENT_KEY_RECEIVE_IS_SYSTEM_PUSH";
    public static final String INTENT_KEY_RECEIVE_PUSH_MSG = "INTENT_KEY_RECEIVE_PUSH_MSG";
    public static final String INTENT_KEY_COUNTRY_CODE = "INTENT_KEY_COUNTRY_CODE";
    public static final String INTENT_KEY_VERIFY_CODE = "INTENT_KEY_VERIFY_CODE";
    public static final String INTENT_KEY_NOTIFICATIONS_LEVEL = "INTENT_KEY_NOTIFICATIONS_LEVEL";
    public static final String INTENT_KEY_DEVICE_ID = "INTENT_KEY_DEVICE_ID";
    public static final String INTENT_KEY_SHARE_DEVICE_INFO = "INTENT_KEY_SHARE_DEVICE_INFO";
    public static final String INTENT_KEY_NICK_NAME = "INTENT_KEY_NICK_NAME";
    public static final String INTENT_KEY_AVATAR = "INTENT_KEY_AVATAR";
    public static final String INTENT_KEY_DATA_TYPE = "INTENT_KEY_DATA_TYPE";
    public static final String INTENT_NOLINE_DEVICE_LIST = "INTENT_NOLINE_DEVICE_LIST";
    public static final String INTENT_KEY_PUSH_MSG = "INTENT_KEY_PUSH_MSG";
    public static final String INTENT_KEY_TIME_STAMP = "INTENT_KEY_TIME_STAMP";
    public static final String INTENT_KEY_SHARED_USER_LIST = "INTENT_KEY_SHARED_USER_LIST";
    public static final String INTENT_KEY_DEVICE_NAME = "INTENT_KEY_DEVICE_NAME";
    public static final String INTENT_KEY_URL = "INTENT_KEY_URL";
    public static final String INTENT_KEY_TITLE = "INTENT_KEY_TITLE";
    public static final String INTENT_KEY_ACCOUNT = "INTENT_KEY_ACCOUNT";
    public static final String INTENT_KEY_WEEK = "INTENT_KEY_WEEK";
    public static final String INTENT_KEY_START = "INTENT_KEY_START";
    public static final String INTENT_KEY_END = "INTENT_KEY_END";
    public static final String INTENT_KEY_VERIFY_TYPE = "INTENT_KEY_VERIFY_TYPE";
    public static final String INTENT_KEY_SSID = "INTENT_KEY_SSID";
    public static final String INTENT_KEY_PSD = "INTENT_KEY_PSD";
    public static final String INTENT_KEY_SSID_5 = "INTENT_KEY_SSID_5";
    public static final String INTENT_KEY_PSD_5 = "INTENT_KEY_PSD_5";
    public static final String INTENT_KEY_WIFI_INFO = "INTENT_KEY_WIFI_INFO";
    public static final String INTENT_KEY_SKIP_QR_CODE_CONFIG_NETWORK = "INTENT_KEY_SKIP_QR_CODE_CONFIG_NETWORK";
    public static final String INTENT_KEY_NEED_FOUND_SDCARD_FIRST_TIME_RECORD = "INTENT_KEY_NEED_FOUND_SDCARD_FIRST_TIME_RECORD";
    public static final String INTENT_KEY_NEED_FOUND_CLOUD_FIRST_TIME_RECORD = "INTENT_KEY_NEED_FOUND_CLOUD_FIRST_TIME_RECORD";
    public static final String INTENT_KEY_IPC_MODEL = "IPC_MODEL";
    public static final String INTENT_KEY_DEVICE_PLATFORM = "INTENT_KEY_DEVICE_PLATFORM";
    public static final String INTENT_KEY_RECEIVE_JG_PUSH = "INTENT_KEY_RECEIVE_JG_PUSH";
    public static final String INTENT_KEY_RECEIVE_FCM_PUSH = "INTENT_KEY_RECEIVE_FCM_PUSH";
    public static final String INTENT_KEY_DEVICE_IP = "INTENT_KEY_DEVICE_IP";
    public static final String INTENT_KEY_DEVICE_PORT = "INTENT_KEY_DEVICE_PORT";
    public static final String INTENT_KEY_PHONE_CODE = "INTENT_KEY_PHONE_CODE";
    public static final String INTENT_KEY_COUNTRY_NAME = "INTENT_KEY_COUNTRY_NAME";
    public static final String INTENT_KEY_COUNTRY_KEY = "INTENT_KEY_COUNTRY_KEY";
    public static final String INTENT_KEY_DATA_ID = "INTENT_KEY_DATA_ID";
    public static final String INTENT_KEY_DATA_PARAM = "INTENT_KEY_DATA_PARAM";
    public static final String INTENT_KEY_DATA_PARAM_1 = "INTENT_KEY_DATA_PARAM_1";
    public static final String INTENT_KEY_DATA_PARAM_2 = "INTENT_KEY_DATA_PARAM_2";
    public static final String INTENT_KEY_DATA_PARAM_3 = "INTENT_KEY_DATA_PARAM_3";
    public static final String INTENT_KEY_DATA_PARAM_4 = "INTENT_KEY_DATA_PARAM_4";
    public static final String INTENT_KEY_ROUTE_SOURCE = "INTENT_KEY_ROUTE_SOURCE";
    public static final String INTENT_KEY_IS_ADMIN = "INTENT_KEY_IS_ADMIN";
    public static final String INTENT_KEY_BLE_DEVICE = "INTENT_KEY_BLE_DEVICE";
    public static final String INTENT_KEY_BLE_SEC = "INTENT_KEY_BLE_SEC";
    public static final String INTENT_KEY_CONNECTION_MODE = "INTENT_KEY_CONNECTION_MODE";
    public static final String INTENT_KEY_EVENT_ID = "INTENT_KEY_EVENT_ID";
    public static final String INTENT_KEY_START_PARAM = "INTENT_KEY_START_PARAM";
    public static final String INTENT_KEY_HOME_PAGE_ACTION = "INTENT_KEY_HOME_PAGE_ACTION";

    // router key
    public static final String INTENT_KEY_ONLINE_MSG = "INTENT_KEY_ONLINE_MSG";
    public static final String INTENT_KEY_ONLINE_DEVIVE = "INTENT_KEY_ONLINE_DEVIVE";
    public static final String INTENT_KEY_PARETAL_CONTROL_RULE_MSG = "INTENT_KEY_PARETAL_CONTROL_RULE_MSG";
    public static final String INTENT_KEY_DEVICE_MAC = "INTENT_KEY_DEVICE_MAC";
    public static final String INTENT_KEY_DEVICE_WIFI_TYPE = "INTENT_KEY_DEVICE_WIFI_TYPE";
    public static final String INTENT_KEY_DEVICE_IS_WHITE = "INTENT_KEY_DEVICE_IS_WHITE";
    public static final String INTENT_KEY_DEVICE_ISBIND = "INTENT_KEY_DEVICE_ISBIND";
    public static final String INTENT_KEY_ROUTER_SSID = "INTENT_KEY_ROUTER_SSID";
    public static final String INTENT_KEY_ROUTER_SSID_5G = "INTENT_KEY_ROUTER_SSID_5G";
    public static final String INTENT_KEY_DEVICE_SETTING = "INTENT_KEY_DEVICE_SETTING";
    public static final String INTENT_KEY_DEVICE_RETURN_INFO = "INTENT_KEY_DEVICE_RETURN_INFO";
    public static final String INTENT_KEY_SSID_SWITCH = "INTENT_KEY_SSID_SWITCH";
    public static final String INTENT_KEY_SSID_SWITCH_2G = "INTENT_KEY_SSID_SWITCH_2G";
    public static final String INTENT_KEY_SSID_SWITCH_5G = "INTENT_KEY_SSID_SWITCH_5G";
    public static final String INTENT_KEY_DEVICE_UPGRADE_VERSION = "INTENT_KEY_DEVICE_UPGRADE_VERSION";

    //intent filter
    public static final String INTENT_FILTER_NOOIE_SYS = "gncc.message.sys";
    public static final String INTENT_FILTER_NOOIE_DEVICE = "gncc.message.device";
    public static final String INTENT_FILTER_NOOIE_HOME = "gncc.message.home";

    //common use
    public static final String MIN_SOUND_DETECT_VERSION = "2.6.30";
    public static final String MIN_SLEEP_SUPPORT_VERSION = "2.6.26";
    public static final String MIN_NOOIE_NIGHT_VISION_720 = "2.6.63";
    public static final String MIN_NOOIE_NIGHT_VISION_1080 = "2.1.65";
    public static final String MIN_NOOIE_NIGHT_VISION_100 = "1.3.57";
    public static final String MIN_NOOIE_NIGHT_VISION_200 = "2.1.50";
    public static final String MIN_NOOIE_ALARM_AUDIO_200 = "2.1.50";
    public static final String MIN_NOOIE_SYNC_TIME_720 = "2.6.75";
    public static final String MIN_NOOIE_SYNC_TIME_1080 = "2.1.86";
    public static final String MIN_NOOIE_SYNC_TIME_100 = "1.3.79";
    public static final String MIN_NOOIE_SYNC_TIME_200 = "2.1.58";
    public static final String MIN_DEVICE_REMOVE_SELF_420 = "1.1.25";
    public static final String MIN_DEVICE_REMOVE_SELF_530 = "1.1.4";
    public static final String MIN_DEVICE_REMOVE_SELF_810 = "1.0.62";
    public static final String MIN_DEVICE_REMOVE_SELF_810_HUB = "3.1.59";
    public static final String MIN_DEVICE_DETECTION_ZONE_420 = "1.1.39";
    public static final String MIN_DEVICE_DETECTION_ZONE_530 = "1.3.8";
    public static final String MIN_DEVICE_DETECTION_ZONE_530A = "5.0.0";
    public static final String MIN_DEVICE_DETECTION_ZONE_540 = "1.3.8";
    public static final String MIN_DEVICE_DETECTION_ZONE_650 = "5.0.0";
    public static final String MIN_DEVICE_DETECTION_ZONE_730 = "1.1.41";
    public static final String MIN_DEVICE_DETECTION_ZONE_730_F23 = "1.0.5";
    public static final String MIN_DEVICE_DETECTION_ZONE_120 = "5.0.0";
    public static final String MIN_DEVICE_DETECTION_ZONE_210 = "1.1.0";
    public static final String MIN_DEVICE_DETECTION_ZONE_220 = "1.0.9";
    public static final int AUDIO_STATE_ON = 1;
    public static final int AUDIO_STATE_OFF = 0;
    public static final int ALARM_AUDIO_STATE_ON = 1;
    public static final int ALARM_AUDIO_STATE_OFF = 0;

    public static final int NOOIE_MSG_TYPE_SYS = 1;
    public static final int NOOIE_MSG_TYPE_DEVICE = 2;

    public static final String NOOIE_INTENT_KEY_DETECT_TYPE = "NOOIE_INTENT_KEY_DETECT_TYPE";
    public static final int NOOIE_DETECT_TYPE_MOTION = 0;
    public static final int NOOIE_DETECT_TYPE_SOUND = 1;
    public static final int DETECT_TYPE_PIR = 2;
    public static final int NOOIE_DETECT_STATUS_CLOSE = 0;
    public static final int NOOIE_DETECT_STATUS_OPEN = 1;
    public static final int NOOIE_DETECT_STATUS_IGNORE= -1;
    public static final int CAM_SETTING_TYPE_NORMAL = 0;
    public static final int CAM_SETTING_TYPE_DEVICE_OFFLINE = 1;
    public static final int CAM_INFO_TYPE_NORMAL = 0;
    public static final int CAM_INFO_TYPE_DIRECT = 1;

    public static final int NOOIE_NIGHT_VISION_MODE_DAY = 1;
    public static final int NOOIE_NIGHT_VISION_MODE_AUTO = 2;

    public static final int NOOIE_SD_STATUS_NORMAL = 0;
    public static final int NOOIE_SD_STATUS_FORMATING = 1;
    public static final int NOOIE_SD_STATUS_NO_SD = 2;
    public static final int NOOIE_SD_STATUS_DAMAGE= 3;
    public static final int NOOIE_SD_STATUS_MOUNTING= 4;

    public static final int HUB_SD_STATUS_NORMAL = 0;
    public static final int HUB_SD_STATUS_FORMATING = 1;
    public static final int HUB_SD_STATUS_NO_SD = 2;
    public static final int HUB_SD_STATUS_DAMAGE= 3;

    public static final int NOOIE_PLAYBACK_TYPE_LIVE = 0;
    public static final int NOOIE_PLAYBACK_TYPE_SD = 1;
    public static final int NOOIE_PLAYBACK_TYPE_CLOUD = 2;
    public static final int NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL = 0;
    public static final int NOOIE_PLAYBACK_SOURCE_TYPE_DIRECT = 1;

    public static final String NOOIE_AREA_US = "us";
    public static final String NOOIE_AREA_EU = "eu";
    public static final String NOOIE_AREA_CN = "cn";

    public static final int NOOIE_CUSTOM_NAME_TYPE_USER = 0;
    public static final int NOOIE_CUSTOM_NAME_TYPE_DEVICE = 1;

    public static final boolean FORCE_USE_JPUSH = false;

    public static final int NOOIE_AUTO_LOGIN_REPORT_YES = 1;
    public static final int NOOIE_AUTO_LOGIN_REPORT_NO = 0;

    public static final int DANALE_DEV_REPORT_YES = 1;
    public static final int DANALE_DEV_REPORT_NO = 0;

    public static final int APP_NOTIFICATION_REQUEST_YES = 1;
    public static final int APP_NOTIFICATION_REQUEST_NO = 0;

    public static final int DEVICE_GUIDE_USED = 1;
    public static final int DEVICE_GUIDE_NOT_USED = 0;

    public static final int STATE_ON = 1;
    public static final int STATE_OFF = 0;

    public static final int CLOUD_PACK_DEFAULT_DAY_NUM = 7;

    public static final int NOOIE_DEVELOP_MODE_DEFAULT = 0;

    public static final int USER_REGISTER_VERIFY = 1;
    public static int USER_FIND_PWD_VERIFY = 2;

    public static final String NORMAL_DEVICE_PUUID = "1";
    public static final int DEVICE_POWER_MODE_NORMAL = 1;
    public static final int DEVICE_POWER_MODE_LP_ACTIVE = 2;
    public static final int DEVICE_POWER_MODE_LP_SLEEP = 3;

    public static final int LP_CAMERA_PLAY_LIMIT_TIME = 240;

    public static final int REMOVE_DEVICE_TYPE_IPC = 1;
    public static final int REMOVE_DEVICE_TYPE_GATEWAY = 2;

    public static final int CONNECTION_MODE_NONE = 0;
    public static final int CONNECTION_MODE_AP = 1;
    public static final int CONNECTION_MODE_QC = 2;
    public static final int CONNECTION_MODE_AP_DIRECT = 3;
    public static final int CONNECTION_MODE_LAN = 4;

    public static final String AP_FUTURE_CODE_PREFIX_VICTURE = "victure_";
    public static final String AP_FUTURE_CODE_PREFIX_VICTURE_REPLACE_TAG = "victure-";
    public static final int AP_FUTURE_CODE_VICTURE_SSID_LEN = 20;
    public static final String AP_FUTURE_PREFIX = "victure";
    public static final int AP_FUTURE_ID_LEN = 14;
    public static final String AP_FUTURE_CODE_PREFIX_GNCC = "securitycam_";
    public static final String AP_FUTURE_CODE_PREFIX_GNCC_FILE_TAG = "gncc_";
    public static final String AP_FUTURE_CODE_PREFIX_GNCC_REPLACE_TAG = "gncc-";
    public static final String AP_FUTURE_PREFIX_GNCC = "securitycam";

    public static final String WIFI_FUTURE_CODE_5 = "_5";
    public static final String WIFI_FUTURE_CODE_5G = "_5g";

    public static final String HB_SERVER_EMPTY = "0.0.0.0";

    public static final String VICTURE_AP_DIRECT_DEVICE_ID = "victure_000011112222";

    public static final int EDIT_MODE_NORMAL = 1;
    public static final int EDIT_MODE_EDITABLE= 2;
    public static final int EDIT_MODE_EDITING = 3;

    public static final int CMD_STATE_ENABLE = 1;
    public static final int CMD_STATE_DISABLE = 0;

    public static final int STATUS_BAR_DARK_MODE = 1;
    public static final int STATUS_BAR_LIGHT_MODE = 2;
    public static final int STATUS_BAR_LIGHT_BLUE_MODE = 3;

    public static final int PRODUCT_TYPE_ROUTER = 0;
    public static final int PRODUCT_TYPE_CARD = 1;
    public static final int PRODUCT_TYPE_GUN = 2;
    public static final int PRODUCT_TYPE_HEAD = 3;
    public static final int PRODUCT_TYPE_LOW_POWER = 4;
    public static final int PRODUCT_TYPE_MINI = 5;
    public static final int PRODUCT_TYPE_LOCK = 205;

    public static final int ROUTER_TYPE_NET_GEAR = 1;
    public static final int ROUTER_TYPE_ASUS = 2;
    public static final int ROUTER_TYPE_D_LINK = 3;
    public static final int ROUTER_TYPE_TP_LINK = 4;

    public static final int CLOUD_RECORD_REQUEST_NORMAL = 1;
    public static final int CLOUD_RECORD_REQUEST_MORE = 2;

    public static final int DEVICE_ID_MAX_LEN = 32;
    public static final int DEVICE_NAME_MAX_LEN = 50;
    public static final int DEVICE_WIFI_MAX_LEN = 32;
    public static final int DEVICE_WIFI_MIN_LEN = 4;

    public final static String EventTypeLive = "EventTypeLive";
    public final static String EventTypeSDPlayBack = "EventTypeSDPlayBack";
    public final static String EventTypeCloudPlayBack = "EventTypeCloudPlayBack";

    public final static int BTN_CLICK_GAP_TIME = 500;

    public static final int PLAY_DISPLAY_TYPE_NORMAL = 1;
    public static final int PLAY_DISPLAY_TYPE_DETAIL = 2;

    public static final int ROUTE_SOURCE_NORMAL = 0;
    public static final int ROUTE_SOURCE_ADD_DEVICE = 1;

    public static final String MODEL_VALUE_OF_530_540 = "PC530/PC540";

    public static final String THIRD_PARTY_CONTROL_PARENT_URL = "file:///android_asset/html/thirdpartycontrol/";
    public static final String THIRD_PARTY_CONTROL_ALEXA_PATH = "alexa.html";
    public static final String THIRD_PARTY_CONTROL_GOOGLE_ASSISTANT_PATH = "google.html";

    public static final int GESTURE_MOVE_LEFT = 1;
    public static final int GESTURE_MOVE_TOP = 2;
    public static final int GESTURE_MOVE_RIGHT = 3;
    public static final int GESTURE_MOVE_BOTTOM = 4;
    public static final int GESTURE_TOUCH_DOWN = 5;
    public static final int GESTURE_TOUCH_UP = 6;

    public static final String KEY_TWO_AUTH_DEVICE_NAME = "KEY_TWO_AUTH_DEVICE_NAME";
    public static final String KEY_TWO_AUTH_DEVICE_PHONE_ID = "KEY_TWO_AUTH_DEVICE_PHONE_ID";
    public static final String KEY_TWO_AUTH_DEVICE_MODEL = "KEY_TWO_AUTH_DEVICE_MODEL";
    public static final String KEY_TWO_AUTH_DEVICE_LAST_TIME = "KEY_TWO_AUTH_DEVICE_LAST_TIME";

    public static final int LP_SUIT_ADD_DEVICE_TYPE_HUM_AND_CAM = 1;
    public static final int LP_SUIT_ADD_DEVICE_TYPE_CAM_WITH_ROUTER = 2;

    public static final int TYPE_FILE_SETTING_CONFIGURE_NONE = 0;
    public static final int TYPE_FILE_SETTING_CONFIGURE_MODE = 1;
    public static final int TYPE_FILE_SETTING_CONFIGURE_SNAP_NUMBER = 2;
    public static final int TYPE_FILE_SETTING_CONFIGURE_RECORDING_TIME = 3;

    public static int DEFAULT_CAMERA_TYPE = 0;
    public static int ADD_CAMERA_TYPE = 1;
    public static int DRAG_CAMERA_TYPE = 2;
    public static int AP_DIRECT_CAMERA_TYPE = 3;

    public static final int DEVICE_ACCOUNT_MAX_LENGTH = 320;

    public static final int INCORRECT_LIGHT_PAGE_ERROR_TYPE_SUB_DEVICE = 1;
    public static final int INCORRECT_LIGHT_PAGE_ERROR_TYPE_ROUTER_DEVICE = 2;

    public static final int FLASH_LIGHT_MODE_FULL_COLOR_NIGHT_VISION = 1;
    public static final int FLASH_LIGHT_MODE_FLASH_WARNING = 2;
    public static final int FLASH_LIGHT_MODE_CLOSE = 0;

    public static int BLUETOOTH_SCAN_TYPE_NEW = 1;
    public static int BLUETOOTH_SCAN_TYPE_EXIST = 2;

    public static int DEVICE_MEDIA_MODE_VIDEO = 0;
    public static int DEVICE_MEDIA_MODE_IMAGE = 1;
    public static int DEVICE_MEDIA_MODE_VIDEO_IMAGE = 2;

    public static int LP_DEVICE_COUNTDOWN_TYPE_PLAYBACK = 1;
    public static int LP_DEVICE_COUNTDOWN_TYPE_SHORT_LINK = 2;

    public static final int AP_DEVICE_TYPE_NORMAL = 0;
    public static final int AP_DEVICE_TYPE_BLE_LP = 1;
    public static final int AP_DEVICE_TYPE_IPC = 2;

    public static final String DEFAULT_PASSWORD_AP_P2P = "12345678";
    public static final String DEFAULT_UUID_AP_P2P = "victure_ap";
    public static final String DEFAULT_SERVER_AP_P2P = "192.168.43.1";
    public static final int DEFAULT_PORT_AP_P2P = 23000;

    public static final int CHANGE_BLE_AP_DEVICE_PASSWORD_RESULT_SUCCESS = 1;
    public static final int CHANGE_BLE_AP_DEVICE_PASSWORD_RESULT_OLD_PW_ERROR = 2;
    public static final int CHANGE_BLE_AP_DEVICE_PASSWORD_RESULT_FAIL = 3;

    public static final int HOME_PAGE_ACTION_NORMAL= 0;
    public static final int HOME_PAGE_ACTION_SWITCH_CONNECTION_MODE = 1;

    public static final String CLOUD_PACK_PARAM_KEY_UUID = "uuid";
    public static final String CLOUD_PACK_PARAM_KEY_MODEL = "model";
    public static final String CLOUD_PACK_PARAM_KEY_ENTER_MARK = "enter_mark";
    public static final String CLOUD_PACK_PARAM_KEY_ORIGIN = "origin";

    public static final int BLE_USER_TYPE_NORMAL = 1;
    public static final int BLE_USER_TYPE_ADMIN = 2;

    public static final int LOCK_RECORD_NAME_FOR_USE_OPEN = 0;
    public static final int LOCK_RECORD_NAME_FOR_FINGER_OPEN = 0x01;
    public static final int LOCK_RECORD_NAME_FOR_PSW_OPEN = 0x02;
    public static final int LOCK_RECORD_NAME_FOR_APP_OPEN = 0x03;
    public static final int LOCK_RECORD_NAME_FOR_REMOTE_OPEN = 0x04;

    //request code
    public static final int REQUEST_CODE_UPDATE_TO_NOOIE = 0x01;
    public static final int REQUEST_CODE_SELECT_COUNTRY = 0x02;
    public static final int REQUEST_CODE_SELECT_SCHEDULE = 0x03;
    public static final int REQUEST_CODE_SET_DETECTION_ZONE = 0x04;
    public static final int REQUEST_CODE_CUSTOM_NAME = 0x05;
    public static final int REQUEST_CODE_DELETE_FILE = 0x06;
    public static final int REQUEST_CODE_SIGN_IN = 0x07;
    public static final int REQUEST_CODE_INPUT_DEVICE_ID = 0x08;
    public static final int REQUEST_CODE_FOR_CAMERA = 0x09;
    public static final int REQUEST_CODE_FOR_TWO_AUTH_LOGIN = 0x10;

    public static final int REQUEST_CODE_FOR_ENABLE_BLUE = 0x401;
    public static final int REQUEST_CODE_FOR_LOCATION_PERM = 0x402;
    public static final int REQUEST_CODE_FOR_OPENING_LOCATION_PERM_SETTING = 0x403;

    //permission group
    public static final String[] PERM_GROUP_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] PERM_GROUP_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] PERM_GROUP_STORAGE_API_30 = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] PERM_GROUP_CAMERA = {
            Manifest.permission.CAMERA
    };

    public static final String[] PERM_GROUP_PHONE = {
            Manifest.permission.READ_PHONE_STATE
    };

    public interface Suffix {
        String MP4 = ".mp4";
        String DNAV = ".dnav";
        String JPEG = ".jpg";
        String PNG = ".png";
    }

    public static final String PARAM_KEY_DEVICE_ID = "PARAM_KEY_DEVICE_ID";
    public static final String PARAM_KEY_PDEVICE_ID = "PARAM_KEY_PDEVICE_ID";
    public static final String PARAM_KEY_MODEL = "PARAM_KEY_MODEL";
    public static final String PARAM_KEY_CONNECTION_MODE = "PARAM_KEY_CONNECTION_MODE";
    public static final String PARAM_KEY_DEVICE_SSID = "PARAM_KEY_DEVICE_SSID   ";



    /*Router*/
    public static final String ROUTER_CONNECT_STATE = "ROUTER_CONNECT_STATE";

    public static final String IPC_MODEL_A1 = "A1";
    public static final String IPC_MODEL_C1 = "C1";
    public static final String IPC_MODEL_Q1 = "Q1";
    public static final String IPC_MODEL_T1 = "T1";
    public static final String IPC_MODEL_P1 = "P1";
    public static final String IPC_MODEL_P2 = "P2";
    public static final String IPC_MODEL_P3 = "P3";
    public static final String IPC_MODEL_P4 = "P4";
    public static final String IPC_MODEL_K1 = "K1";
    public static final String IPC_MODEL_K2 = "K2";
    public static final String IPC_MODEL_P3PRO = "P3 Pro";
    public static final String IPC_MODEL_M1 = "M1";
    public static final String IPC_MODEL_W0_CAM = "W0-CAM";
    public static final String IPC_MODEL_W0_HUB = "W0-HUB";
    public static final String IPC_MODEL_W1 = "W1";
    public static final String IPC_MODEL_W2 = "W2";

    public static final int SHARE_DEVICE_MAX_COUNT = 3;

}
