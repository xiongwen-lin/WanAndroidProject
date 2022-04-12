package com.apemans.customserviceapi.webapi

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/11/22-10:02
 */
data class MessageData (
    var id: Int,
    var msg: String?,
    var share_id: Int,
    var status: Int,
    var time: Int,
    var type: Int,
    var uid: Int,
    var uuid: String
)