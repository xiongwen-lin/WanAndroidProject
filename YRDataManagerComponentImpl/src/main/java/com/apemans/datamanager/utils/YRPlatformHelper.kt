package com.apemans.datamanager.utils

import com.apemans.datamanager.bean.YRPlatformDevice
import com.apemans.datamanager.constant.*
import com.apemans.datamanager.webapi.BindDevice

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/1/27 5:23 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRPlatformHelper {

    fun convertPlatformDevice(bindDevice: BindDevice) : YRPlatformDevice {
        return YRPlatformDevice().apply {
            uuid = bindDevice.uuid
            name = bindDevice.name
            online = bindDevice.online == 1
            version = bindDevice.version
            iconUrl = ""
            productId = ""
            platform = YR_PLATFORM_OF_YRCX
            category = YR_PLATFORM_CATEGORY_OF_IPC
            extra = mapOf()
            eventSchema = createEventSchema(this.eventSchema, this.uuid.orEmpty(), this.online, bindDevice.open_status == 1)
        }
    }

    fun convertPlatformNetSpotDevice() : YRPlatformDevice {
        return YRPlatformDevice().apply {
            uuid = "xxx"
            name = "Test"
            online = true
            version = "1.0.0"
            iconUrl = ""
            productId = ""
            platform = YR_PLATFORM_OF_YRCX
            category = YR_PLATFORM_CATEGORY_OF_IPC
            extra = mapOf()
            eventSchema = createEventSchema(this.eventSchema, this.uuid.orEmpty(), this.online, true)
        }
    }

    private fun createEventSchema(eventSchema: Map<String, Map<String, Any?>>?, deviceId: String, online: Boolean, on: Boolean) : Map<String, Map<String, Any?>> {
        val result = mutableMapOf<String, Map<String, Any?>>()
        if (!eventSchema.isNullOrEmpty()) {
            result.putAll(eventSchema)
        }
        var actionType : String = ""
        if (online) {
            val switchEvent = mutableMapOf<String, Any?>()
            switchEvent[YR_PLATFORM_KEY_EVENT_ID] = YR_PLATFORM_EVENT_ID_SWITCH
            switchEvent[YR_PLATFORM_KEY_EVENT_URL] = ""
            switchEvent[YR_PLATFORM_KEY_EVENT_PARAM] = mapOf(
                YR_PLATFORM_KEY_DEVICE_ID to deviceId,
                YR_PLATFORM_KEY_STATE to false
            )
            switchEvent[YR_PLATFORM_KEY_EVENT_VALUE] = on
            result[YR_PLATFORM_EVENT_ID_SWITCH] = switchEvent
            actionType = YR_PLATFORM_ACTION_TYPE_ONLINE_CLICK
        } else {
            actionType = YR_PLATFORM_ACTION_TYPE_OFFLINE_CLICK
        }

        val clickEvent = mutableMapOf<String, Any?>()
        clickEvent[YR_PLATFORM_KEY_EVENT_ID] = YR_PLATFORM_EVENT_ID_CLICK
        clickEvent[YR_PLATFORM_KEY_EVENT_URL] = ""
        clickEvent[YR_PLATFORM_KEY_EVENT_PARAM] = mapOf(
            YR_PLATFORM_KEY_DEVICE_ID to deviceId,
            YR_PLATFORM_KEY_ACTION_TYPE to ""
        )
        clickEvent[YR_PLATFORM_KEY_EVENT_VALUE] = mapOf(
            YR_PLATFORM_KEY_ACTION_TYPE to actionType
        )
        result[YR_PLATFORM_EVENT_ID_CLICK] = clickEvent

        return result
    }

    private fun convertSwitchState(on: Boolean) : String {
        return if (on) YR_PLATFORM_SWITCH_STATE_ON else YR_PLATFORM_SWITCH_STATE_OFF
    }
}