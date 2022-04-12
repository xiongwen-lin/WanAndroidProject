package com.apemans.tuya.component.ui.addshareuser

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository
import kotlinx.coroutines.flow.transform

/**
 * @author Dylan Cai
 */
class AddShareUserViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun shareDevice(homeId: Long, email: String, countryCode: String, deviceId: String) =
        TuyaRepository.getUid(email)
            .transform {
                emit(TuyaRepository.addShareWithHomeId(homeId, countryCode, it, listOf(deviceId)))
            }
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}