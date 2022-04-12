/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.ipcselfdevelopscheme.define

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/16 3:08 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

/**
 * 自有Ipc方案命令类型
 * IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_NORMAL：常规Ipc命令，支持设备包括：PC420等长链接设备
 * IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM：低功耗Ipc命令，支持设备包括：EC810-Cam等
 * IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_HUB：网关Ipc命令，支持设备包括：EC810-Hub等网关类设备
 * IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM_HUB：单机低功耗Ipc命令，支持设备包括：W1-Cam、打猎相机等单机低功耗设备
 */
const val IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_UNKNOWN = 0
const val IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_NORMAL = 1
const val IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM = 2
const val IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_HUB = 3
const val IPC_SELF_DEVELOP_SCHEME_CMD_TYPE_LP_CAM_HUB = 4

const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_NORMAL= 1
const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_LP_CAM = 2
const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_HUB = 3
const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_ROTATE = 4
const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_GUN = 5
const val IPC_SELF_DEVELOP_SCHEME_MODEL_TYPE_MINI = 6

const val IPC_SELF_DEVELOP_SCHEME_CONN_TYPE_IPC = 1
const val IPC_SELF_DEVELOP_SCHEME_CONN_TYPE_HUB = 2
