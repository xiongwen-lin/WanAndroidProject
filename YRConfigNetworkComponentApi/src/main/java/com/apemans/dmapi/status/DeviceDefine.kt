package com.apemans.dmapi.status

object DeviceDefine {

    const val ONLINE = 1;
    const val OFFLINE = 0;

    const val SWITCH_ON = 1;
    const val SWITCH_OFF = 0;

    const val BIND_TYPE_OWNER = 1;
    const val BIND_TYPE_SHARER = 2;

    const val THIRD_CONTROL_ENABLE = 1;

    const val MODEL_TYPE_NORMAL = 1;
    const val MODEL_TYPE_LOW_POWER = 2;

    const val THEFT_ON = 2;
    const val THEFT_OFF = 1;

    const val NOTICE_ON = 2;
    const val NOTICE_OFF = 1;

    const val UPGRADE_TYPE_UNKNOWN = -1;
    const val UPGRADE_TYPE_NORMAL = 0;
    const val UPGRADE_TYPE_DOWNLOADING = 1;
    const val UPGRADE_TYPE_DOWNLOADED = 2;
    const val UPGRADE_TYPE_INSTALLING = 3;
    const val UPGRADE_TYPE_SUCCESS = 4;
    const val UPGRADE_TYPE_FAIL = 5;
    const val UPGRADE_TYPE_FINISH = 6;
}