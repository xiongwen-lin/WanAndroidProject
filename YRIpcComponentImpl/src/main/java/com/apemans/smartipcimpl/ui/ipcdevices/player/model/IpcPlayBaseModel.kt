package com.apemans.smartipcimpl.ui.ipcdevices.player.model

import android.media.AudioManager
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.middleservice.YRMiddleServiceResponse
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.ui.actioncameralist.IpcControlViewModel

import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.DateTimeBean
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.FunctionMenuItem
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.NooieDeviceHelper
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.nooie.common.bean.PhoneVolume
import com.nooie.common.utils.log.NooieLog
import com.nooie.common.utils.tool.SystemUtil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/**
 * @Author:dongbeihu
 * @Description: IPC player实现接口功能
 * @Date: 2021/12/2-15:27
 */
class IpcPlayBaseModel : IpcControlViewModel() {
    val ipcDeviceList get() = DeviceManagerRepository.ipcDeviceList

    fun loadIpcDeviceList(uid: String) {
        viewModelScope.launch {
            DeviceManagerRepository.getIpcDeviceListFlow(uid)
                .flowOn(Dispatchers.IO)
                .catch {
                    YRLog.d { it }
                }
                .collect {
                    YRLog.d { it }

                    it?.filter {
                        it.deviceType == IpcDevice.DEVICE_TYPE_IPC || it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
                    }.forEach {
                        (it?.device?.deviceInfo as IpcDeviceInfo)?.run {
                            //startP2PConnection(this)

                            val params = mutableMapOf<String, Any>()
                            params["extra"] = JsonConvertUtil.convertToJson(DeviceControlHelper.convertIpcP2PConnectionInfo(this, obtainUid())) ?: ""
                            var response: YRMiddleServiceResponse<Boolean>? = YRMiddleServiceManager.request("yrcx://yripccomponentdevice/startp2pconnection", params) as? YRMiddleServiceResponse<Boolean>
                            /*
                            val params1 = mutableMapOf<String, String>()
                            params1["deviceSsid"] = "Victure_015bd3"
                            var response1: YRMiddleServiceResponse<Boolean>? = YRMiddleServiceManager.request("yrcx://yripccomponentdevice/checknetspotssidvalid", params1) as? YRMiddleServiceResponse<Boolean>

                             */
                        }
                    }
                }
        }
    }

    private fun obtainUid() : String {
        return ""
    }

}