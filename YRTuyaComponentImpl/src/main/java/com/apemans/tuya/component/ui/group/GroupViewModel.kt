package com.apemans.tuya.component.ui.group

import androidx.lifecycle.viewModelScope
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository
import com.tuya.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.launchIn

/**
 * @author Dylan Cai
 */
class GroupViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun sendCommand(deviceBean: DeviceBean, isOpen: Boolean) =
        TuyaRepository.sendCommand(deviceBean, isOpen)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .launchIn(viewModelScope)
}