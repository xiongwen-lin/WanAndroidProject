package com.apemans.dmcomponent.ui.pairmethod.fragment.savename

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.apemans.dmcomponent.repository.PairRepository
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith

class SaveNameViewModel : com.apemans.yruibusiness.base.RequestViewModel() {
    var name = MutableLiveData<String>()
    lateinit var deviceId: String

    fun saveName(name: String) =
        PairRepository.updateDeviceName(deviceId, name)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}