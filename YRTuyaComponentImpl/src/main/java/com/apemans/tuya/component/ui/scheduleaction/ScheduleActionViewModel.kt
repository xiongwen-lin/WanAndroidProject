package com.apemans.tuya.component.ui.scheduleaction

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

class ScheduleActionViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun addTimerWithTask(taskName: String, devId: String, loops: String, dps: Map<String, Any?>, time: String)=
        TuyaRepository.addTimerWithTask(taskName, devId, loops, dps, time)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}