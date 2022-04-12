package com.apemans.tuya.component.ui.savegroup

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class SaveGroupViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun createGroup(productId: String, name: String, deviceIds: List<String>) =
        TuyaRepository.createGroup(productId, name, deviceIds)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}