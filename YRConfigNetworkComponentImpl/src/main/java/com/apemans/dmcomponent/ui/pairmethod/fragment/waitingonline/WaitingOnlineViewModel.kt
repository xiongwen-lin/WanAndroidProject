package com.apemans.dmcomponent.ui.pairmethod.fragment.waitingonline

import com.apemans.yruibusiness.base.RequestViewModel

/**
 * @author Dylan Cai
 */
class WaitingOnlineViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

//    private var lastTime = 0L
//    private val delayTime = 3000
//
//    fun scanPairedDevice(): LiveData<List<BindDevice>> {
//        lastTime = System.currentTimeMillis()
//        return PairRepository.scanPairedDevice()
//            .showLoadingWith(loadingFlow)
//            .retry(60) {
//                val currentTime = System.currentTimeMillis()
//                if (currentTime - lastTime < delayTime) {
//                    delay(delayTime - currentTime + lastTime)
//                }
//                true
//            }
//            .catchWith(exceptionFlow)
//            .asLiveData()
//    }
}