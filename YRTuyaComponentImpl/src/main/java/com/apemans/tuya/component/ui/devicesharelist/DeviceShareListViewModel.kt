package com.apemans.tuya.component.ui.devicesharelist

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class DeviceShareListViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun queryDevShareUserList(deviceId: String) =
        TuyaRepository.queryDevShareUserList(deviceId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}