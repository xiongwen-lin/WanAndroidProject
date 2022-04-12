package com.apemans.tuyahome.component.ui.tuyahome

import androidx.lifecycle.viewModelScope
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.router.routerServices
import com.apemans.tuya.module.api.TuyaService
import kotlinx.coroutines.flow.launchIn

/**
 * @author Dylan Cai
 */
class TuyaHomeViewModel : RequestViewModel() {

    private val tuyaService: TuyaService by routerServices()

    val homeList = tuyaService.homeList
    val deviceList = tuyaService.deviceList

    fun loadData() =
        tuyaService.queryHomeList()
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .launchIn(viewModelScope)

    fun selectHome(homeId: Long) =
        tuyaService.selectHome(homeId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .launchIn(viewModelScope)
}