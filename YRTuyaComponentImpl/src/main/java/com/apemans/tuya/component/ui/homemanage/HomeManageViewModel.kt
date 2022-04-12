package com.apemans.tuya.component.ui.homemanage

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class HomeManageViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun queryMemberList(homeId: Long) =
        TuyaRepository.queryMemberList(homeId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun queryUserShareList(homeId: Long) =
        TuyaRepository.queryUserShareList(homeId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun queryHomeDeviceList(homeId: Long) =
        TuyaRepository.queryHomeDeviceList(homeId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}