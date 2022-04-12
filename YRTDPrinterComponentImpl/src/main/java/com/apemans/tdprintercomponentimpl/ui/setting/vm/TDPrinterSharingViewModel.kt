package com.apemans.tdprintercomponentimpl.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.yruibusiness.base.BaseViewModel
import com.apemans.logger.YRLog
import com.apemans.tdprintercomponentimpl.constant.API_BIND_TYPE_OWNER
import com.apemans.tdprintercomponentimpl.repository.TDPrinterManagerRepository
import com.apemans.tdprintercomponentimpl.webapi.DeviceRelation
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
open class TDPrinterSharingViewModel : com.apemans.yruibusiness.base.BaseViewModel() {

    val deviceRelationState = MutableLiveData(listOf<DeviceRelation>())

    fun startShareDevice(deviceId: String, shares: List<String>) {
        viewModelScope.launch {
            var responses = TDPrinterManagerRepository.shareDevice(deviceId, shares)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            val failResponses = responses.filterNot {
                YRLog.d { "->> IpcSettingViewModel startShareDevice account ${it.key} code ${it.value?.code}" }
                it.value?.code == HttpCode.SUCCESS_CODE
            }
        }
    }

    fun loadDeviceRelation(deviceId: String) {
        viewModelScope.launch {
            var response = TDPrinterManagerRepository.getDeviceRelation(deviceId, 1, 100)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            var responseSuccess = response?.code == HttpCode.SUCCESS_CODE
            if (responseSuccess) {
                var owner = response?.data?.data?.filter {
                    it.type == API_BIND_TYPE_OWNER
                }.orEmpty()

                var sharers = response?.data?.data?.filter {
                    it.type != API_BIND_TYPE_OWNER
                }.orEmpty()

                var binders = mutableListOf<DeviceRelation>()
                binders.addAll(owner)
                binders.addAll(sharers)
                deviceRelationState.value = binders
            }
        }
    }

    fun startRemoveShares(deviceId: String, id: Int) {
        viewModelScope.launch {
            var response = TDPrinterManagerRepository.deleteSharing(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
            val responseSuccess = response?.code == HttpCode.SUCCESS_CODE
            if (responseSuccess) {
                loadDeviceRelation(deviceId)
            } else {
            }
        }
    }

}