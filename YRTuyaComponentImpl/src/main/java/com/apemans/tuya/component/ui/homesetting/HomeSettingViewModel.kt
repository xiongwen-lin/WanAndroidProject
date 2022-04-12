package com.apemans.tuya.component.ui.homesetting

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class HomeSettingViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun removeHome(homeId: Long) =
        TuyaRepository.dismissHome(homeId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun updateHome(homeId: Long, name: String) =
        TuyaRepository.updateHome(homeId, name)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}