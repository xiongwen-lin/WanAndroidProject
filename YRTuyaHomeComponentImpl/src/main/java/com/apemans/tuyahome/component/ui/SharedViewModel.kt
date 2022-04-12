package com.apemans.tuyahome.component.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * @author Dylan Cai
 */
class SharedViewModel : ViewModel() {

    val isSingleRowType = MutableLiveData(false)
}