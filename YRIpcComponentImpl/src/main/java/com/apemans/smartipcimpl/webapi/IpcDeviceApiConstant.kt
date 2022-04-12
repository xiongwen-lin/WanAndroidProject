/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.smartipcimpl.webapi

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/14 9:38 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

object IpcDeviceApiConstant {

    const val API_PATH_OF_DEVICE_BIND_STATUS = "device/bind-status"
    const val API_PATH_OF_DEVICE_RECENT_BIND = "device/newlist"
    const val API_PATH_OF_DEVICE_UPDATE_NAME = "device/up-name"
    const val API_PATH_OF_DEVICE_LIST = "device/list"
    const val API_PATH_OF_DEVICE_INFO = "device/info"
    const val API_PATH_OF_DEVICE_BIND_LIST = "device/bind-list"
    const val API_PATH_OF_DEVICE_DELETE_SHARING = "device/del-bind/{id}"
    const val API_PATH_OF_DEVICE_SEND_SHARING = "share/send"
    const val API_PATH_OF_DEVICE_FEEDBACK_SHARING = "share/feedback"
    const val API_PATH_OF_DEVICE_UPGRADE_STATUS = "device/upgrade-status"
    const val API_PATH_OF_DEVICE_UPDATE_SORT = "device/up-sort"
    const val API_PATH_OF_DEVICE_BINDING = "device/app-bind"
    const val API_PATH_OF_DEVICE_UPDATE_CONFIGURE = "device/up-timing-config"
    const val API_PATH_OF_DEVICE_UPDATE_NOTICE = "device/up-notice"
    const val API_PATH_OF_DEVICE_LEVEL_LIST = "device/level-list"
    const val API_PATH_OF_DEVICE_CHILD_LIST = "device/gateway-child-list"
    const val API_PATH_OF_DEVICE_DELETE_GATEWAY = "device/del-gateway"
    const val API_PATH_OF_DEVICE_DELETE_DEVICE = "device/delete"

    const val API_PATH_OF_PACK_INFO = "pack/info"
    const val API_PATH_OF_PACK_SHARING_INFO = "pack/share-get"
    const val API_PATH_OF_PACK_ALL_INFO = "pack/get-all"
    const val API_PATH_OF_PACK_ORDER_CANCEL = "order/cancel"
    const val API_PATH_OF_VERSION_HARD = "version/hard"
    const val API_PATH_OF_VERSION_GATEWAY_CHILD = "version/gateway-child"
    const val API_PATH_OF_FETCH_CLOUD_VIDEO_LIST = "app/fetch/file_event"
    const val API_PATH_OF_FETCH_DOORBELL_CLOUD_VIDEO_LIST = "app/fetch/doorbell/file_event"

}