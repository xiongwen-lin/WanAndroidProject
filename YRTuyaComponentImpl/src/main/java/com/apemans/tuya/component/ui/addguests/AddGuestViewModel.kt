package com.apemans.tuya.component.ui.addguests

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class AddGuestViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun queryUserShared(homeId: Long, email: String) =
        TuyaRepository.queryUserShared(homeId, email)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun addShareWithHomeId(homeId: Long, countryCode: String, userAccount: String, deviceIds: List<String>) =
        TuyaRepository.addShareWithHomeId(homeId, countryCode, userAccount, deviceIds)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}