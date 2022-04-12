package com.apemans.tuya.component.ui.groupmanager

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class GroupManagerViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun updateDeviceListForGroup(groupId: Long, deviceIds: List<String>) =
        TuyaRepository.updateDeviceListForGroup(groupId, deviceIds)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}