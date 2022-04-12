package com.apemans.tuya.component.ui.addhome

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class AddHomeViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun addHome(name: String) =
        TuyaRepository.createHome(name)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}