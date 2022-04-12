package com.apemans.tuyahome.component.bean

data class Device(
    val uuid: String,
    val name: String,
    val online: Boolean,
    val version: String,
    val iconUrl: String,
    val productId: String,
    val platform: String,
    val category: String,
    var extra: Map<String, Any>? = null,
    var eventSchema: Map<String, EventSchema>? = null
) {

    val showSwitch: Boolean
        get() {
            return eventSchema?.get("switch") != null
        }

    val switchOn: Boolean
        get() {
            val event = eventSchema?.get("switch")
            return if (event != null) {
                return event.value["state"] as Boolean? ?: false
            } else {
                false
            }
        }
}

data class EventSchema(
    val id: String,
    val url: String,
    val param: Map<String, Any>,
    val value: Map<String, Any>
)