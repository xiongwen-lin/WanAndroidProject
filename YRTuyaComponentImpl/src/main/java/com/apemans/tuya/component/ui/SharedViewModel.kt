package com.apemans.tuya.component.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * @author Dylan Cai
 */
class SharedViewModel : ViewModel() {

    val isSingleRowType = MutableLiveData(false)
    val saveGroupEvent = MutableSharedFlow<Unit>()
    val removeHomeEvent = MutableSharedFlow<Unit>()
    val renameHomeEvent = MutableSharedFlow<String>()
    val addDeviceEvent = MutableSharedFlow<Unit>()
}