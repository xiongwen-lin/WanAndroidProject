package com.apemans.tuya.component.ui.creategroup.lamp

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class LampGroupViewModel : com.apemans.yruibusiness.base.RequestViewModel() {
//    val list = TuyaRepository.lampDevicesToAddGroup

    fun queryList(homeId: Long, productId: String) =
        TuyaRepository.queryDeviceListToAddGroup(homeId, productId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}