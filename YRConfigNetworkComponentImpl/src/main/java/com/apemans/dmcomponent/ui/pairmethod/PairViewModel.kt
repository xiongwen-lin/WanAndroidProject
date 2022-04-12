package com.apemans.dmcomponent.ui.pairmethod

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.dmapi.webapi.BindDevice
import com.apemans.dmcomponent.repository.PairRepository
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.retry

class PairViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    private var lastTime = 0L
    private val delayTime = 3000

    fun scanPairedDevice(): LiveData<List<BindDevice>> {
        lastTime = System.currentTimeMillis()
        return PairRepository.scanPairedDevice()
            .showLoadingWith(loadingFlow)
            .retry(60) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime < delayTime) {
                    delay(delayTime - currentTime + lastTime)
                }
                true
            }
            .catchWith(exceptionFlow)
            .asLiveData()
    }
}