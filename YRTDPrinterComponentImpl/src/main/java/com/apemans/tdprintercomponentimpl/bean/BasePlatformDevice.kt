package com.apemans.tdprintercomponentimpl.bean

open class BasePlatformDevice {

    var uuid: String? = ""
    var name: String? = ""
    var online: Boolean = false
    var version: String? = ""
    var iconUrl: String? = ""
    var productId: String? = ""
    var platform: String? = ""
    var category: String? = ""
    var extra: Map<String, Any?>? = null
}