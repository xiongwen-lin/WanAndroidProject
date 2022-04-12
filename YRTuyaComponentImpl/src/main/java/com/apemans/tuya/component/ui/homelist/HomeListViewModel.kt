package com.apemans.tuya.component.ui.homelist

import androidx.lifecycle.viewModelScope
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository
import kotlinx.coroutines.flow.launchIn

/**
 * @author Dylan Cai
 */
class HomeListViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    val homeList = TuyaRepository.homeList

    fun queryHomeList() = TuyaRepository.queryHomeList()
        .showLoadingWith(loadingFlow)
        .catchWith(exceptionFlow)
        .launchIn(viewModelScope)
}