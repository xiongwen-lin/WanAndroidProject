package com.apemans.tuya.component.ui.schedulelist

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class ScheduleListViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun getTimerWithTask(taskName: String, devId: String) =
        TuyaRepository.getTimerWithTask(taskName, devId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}