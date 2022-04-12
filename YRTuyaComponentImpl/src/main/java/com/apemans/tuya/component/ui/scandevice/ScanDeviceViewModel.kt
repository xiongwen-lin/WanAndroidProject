package com.apemans.tuya.component.ui.scandevice

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class ScanDeviceViewModel : com.apemans.yruibusiness.base.RequestViewModel() {
    fun ezPair(ssid: String, pwd: String) =
        TuyaRepository.ezPair(ssid, pwd)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

}