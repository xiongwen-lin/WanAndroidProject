package com.apemans.tuya.component.ui.devicedetails

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class DeviceDetailsViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun removeDevice(deviceId: String) =
        TuyaRepository.removeDevice(deviceId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun removeShareDevice(deviceId: String) =
        TuyaRepository.removeShareDevice(deviceId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun resetFactory(deviceId: String) =
        TuyaRepository.resetFactory(deviceId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}