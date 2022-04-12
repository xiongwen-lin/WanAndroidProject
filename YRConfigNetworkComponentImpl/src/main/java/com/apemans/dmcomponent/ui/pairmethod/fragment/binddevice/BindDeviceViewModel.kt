package com.apemans.dmcomponent.ui.pairmethod.fragment.binddevice

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.dmcomponent.repository.PairRepository

/**
 * @author Dylan Cai
 */
class BindDeviceViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    lateinit var uuid: String

    fun bindDevice() =
        PairRepository.bindDevice(uuid)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}