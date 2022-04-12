package com.apemans.dmcomponent.ui.pairdevice.connecting

import androidx.lifecycle.asLiveData
import com.apemans.dmcomponent.repository.PairRepository
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith

/**
 * @author Dylan Cai
 */
class ConnectingViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun scanPairedDevice() =
        PairRepository.scanPairedDevice()
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}