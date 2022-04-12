package com.apemans.tuya.component.ui.tuyadevices

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository
import com.tuya.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.flatMapConcat

/**
 * @author Dylan Cai
 */
class TuyaDevicesViewModel : com.apemans.yruibusiness.base.RequestViewModel() {
    val deviceList = TuyaRepository.deviceList

    fun sendCommand(deviceBean: DeviceBean, isOpen: Boolean) =
        TuyaRepository.sendCommand(deviceBean, isOpen)
            .flatMapConcat {
                TuyaRepository.updateDevicesAndGroups()
            }
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}