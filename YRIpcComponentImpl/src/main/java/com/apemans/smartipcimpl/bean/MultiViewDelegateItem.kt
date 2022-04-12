package com.apemans.smartipcimpl.bean

import com.apemans.quickui.multitype.ICheckable
import com.apemans.dmapi.model.DeviceModel
import com.apemans.smartipcapi.webapi.PackageInfoResult

data class MultiViewDelegateItem<T>(
    override var groupId: Int = -1,
    override var isChecked: Boolean = false
) : ICheckable {

    var isSelected : Boolean = false
    var data : T? = null

    override fun toString(): String {
        return "isSelected $isSelected"
    }

}
