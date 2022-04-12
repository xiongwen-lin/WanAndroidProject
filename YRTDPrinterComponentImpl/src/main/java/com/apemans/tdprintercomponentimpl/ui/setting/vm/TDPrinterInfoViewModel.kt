package com.apemans.tdprintercomponentimpl.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.apemans.yruibusiness.base.BaseViewModel
import com.apemans.tdprintercomponentimpl.repository.TDPrinterManagerRepository
import com.apemans.tdprintercomponentimpl.ui.setting.bean.TDPrinterInfoUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/10 6:14 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
open class TDPrinterInfoViewModel : BaseViewModel() {

    val uiState = MutableLiveData(TDPrinterInfoUIState())

    fun loadDeviceInfo(deviceId: String) {
        viewModelScope.launch {
            var info = TDPrinterManagerRepository.getDeviceInfo(deviceId)
                .flowOn(Dispatchers.IO)
                .catch {
                }
                .single()
            info?.let {
                uiState.value = uiState.value?.apply {
                    this.deviceInfo = it
                }
            }
        }
    }

}