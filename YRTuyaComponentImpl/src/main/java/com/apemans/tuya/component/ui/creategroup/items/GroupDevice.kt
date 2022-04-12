package com.apemans.tuya.component.ui.creategroup.items

import com.apemans.quickui.multitype.ICheckable
import com.tuya.smart.sdk.bean.GroupDeviceBean

/**
 * @author Dylan Cai
 */
data class GroupDevice(
    val device: GroupDeviceBean,
    override var isChecked: Boolean = false
) : ICheckable