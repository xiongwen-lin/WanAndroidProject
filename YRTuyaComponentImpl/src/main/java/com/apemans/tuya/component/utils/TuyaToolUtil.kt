package com.apemans.tuya.component.utils

import com.apemans.tuya.component.bean.TuyaMessageBean
import com.tuya.smart.sdk.bean.message.MessageBean

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/1/17 5:28 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object TuyaToolUtil {

    fun convertTuyaMessageBean(message: MessageBean?) : TuyaMessageBean {
        return message?.let {
            TuyaMessageBean().apply {
                id = it.id
                msgContent = it.msgContent
                time = it.time
            }
        } ?: TuyaMessageBean()
    }
}