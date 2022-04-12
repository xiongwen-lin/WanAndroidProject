package com.apemans.dmcomponent.ui.adddevice.items

import java.io.Serializable

data class DeviceGroup(
    var groupingTitle: String,
    var deviceTypeList: List<DeviceType> = emptyList()
) : Serializable