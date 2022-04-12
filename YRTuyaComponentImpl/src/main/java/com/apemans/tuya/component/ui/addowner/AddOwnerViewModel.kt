package com.apemans.tuya.component.ui.addowner

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class AddOwnerViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun addMember(homeId: Long, email: String, countryCode: String, name: String, isAdmin: Boolean) =
        TuyaRepository.addMember(homeId, email, countryCode, name, isAdmin)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}