package com.apemans.smartipcimpl.ui.actioncameralist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dylanc.longan.application
import com.apemans.quickui.multitype.ItemsLiveData
import com.apemans.base.utils.NetUtil
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.ipcchipproxy.scheme.bean.NetSpotConfigure
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.logger.YRLog
import com.apemans.smartipcapi.bean.IpcSendCmdResult
import com.apemans.smartipcimpl.bean.IpcDevice.Companion.DEVICE_TYPE_IPC
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.middleservice.YRMiddleServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 */
class ActionCameraListViewModel : IpcControlViewModel() {

    val ipcDeviceList get() = DeviceManagerRepository.ipcDeviceList
    val hubIpcDeviceList get() = DeviceManagerRepository.hubIpcDeviceList
    val netSpotIpcDeviceList get() = DeviceManagerRepository.netSpotIpcDeviceList

    val dvIpcDeviceList = ItemsLiveData<IpcDevice>()
    val ipcDvModeEnable = MutableLiveData<Boolean>()

    fun loadIpcDeviceList(uid: String) {
        viewModelScope.launch {
            DeviceManagerRepository.getIpcDeviceListFlow(uid)
                .flowOn(Dispatchers.IO)
                .catch {
                    YRLog.d { it }
                }
                .collect { list ->
                    YRLog.d { list }

                    list?.filter {
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

    fun loadNetSpotDevice() {
        if (!checkIsNetSpotMode()) {
            removeNetSpot()
            ipcDvModeEnable.value = false
            return
        }
        viewModelScope.launch {
            var ipcDvDevice = getIpcDeviceForNetSpot(obtainUid())
            DeviceControlHelper.updateNetSpotDeviceConfigure(getNetSpotDeviceInfo(), ipcDvDevice)

            var isWifiConnect = NetUtil.isWifiConnected(application)
            if (isWifiConnect && DeviceControlHelper.checkNetSpotDeviceConfigureValid()) {
                ipcDvModeEnable.value = checkIsNetSpotMode()
                DeviceControlHelper.getNetSpotDeviceConfigure()?.ipcDevice?.let {
                    dvIpcDeviceList.value = listOf(IpcDevice(device = it).apply { deviceType = DEVICE_TYPE_IPC })
                }
            } else {
                removeNetSpot()
                ipcDvModeEnable.value = false
            }
        }
    }

    fun checkNetSpotDeviceOnNetworkChange() {
        YRLog.d { "-->> checkNetSpotDeviceOnNetworkChange 1001" }
        if (!checkIsNetSpotMode()) {
            YRLog.d { "-->> checkNetSpotDeviceOnNetworkChange 1002" }
            removeNetSpot()
            ipcDvModeEnable.value = false
            return
        }
        YRLog.d { "-->> checkNetSpotDeviceOnNetworkChange 1003" }
        viewModelScope.launch {
            var isWifiConnect = NetUtil.isWifiConnected(application)
            var isNotRemoveNetSpot = isWifiConnect && NetUtil.getConnectedWifiSSID(application).let {
                !NetUtil.isUnKnowSsid(it) && !DeviceControlHelper.checkNetSpotSsidValid(it)
            }
            YRLog.d { "-->> checkNetSpotDeviceOnNetworkChange 1004 isWifiConnect $isWifiConnect isNotRemoveNetSpot $isNotRemoveNetSpot" }
            if (!isNotRemoveNetSpot) {
                removeNetSpot()
                ipcDvModeEnable.value = false
            }
        }
    }

    fun testNetSpot(callback : ((Boolean) -> Unit)? = null) {

        startNetSpotP2p("Victure_01578b", "04:7e:4a:23:57:8b", "HC320") {
            getNetSpotDeviceInfo()?.let {
                ipcSchemeCore()
                    ?.configureCache()
                    ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            }
            callback?.invoke(it)
        }



        /*
        startNetSpotTcp("Victure_7ca7b0ed5765") {
            getNetSpotDeviceInfo()?.let {
                ipcSchemeCore()
                    ?.configureCache()
                    ?.updateDeviceConfigure(DeviceControlHelper.createYRIpcConfigure(it))
            }
            callback?.invoke(it)
        }

         */
    }

    fun testYRIpcMiddleService() {
        var netspotConfigure = NetSpotConfigure().apply {
            uid = obtainUid()
            ssid = "TP-LINK_HyFi_49"
            psd = "nooie666"
            region = "cn"
            zone = "8.0"
            encrypt = "WPA"
        }
        val params1 = mutableMapOf<String, Any>()
        params1["extra"] = JsonConvertUtil.convertToJson(netspotConfigure).orEmpty()
        YRMiddleServiceManager.requestAsync("yrcx://yripccomponentdevice/sendstartnetspotpaircmd", params1) {
            var response = it as? YRMiddleServiceResponse<IpcSendCmdResult<Int>>
            YRLog.d { "-->> debug testYRIpcMiddleService sendstartnetspotpaircmd response ${response.toString()}" }
            val params11 = mutableMapOf<String, Any>()
            YRMiddleServiceManager.requestAsync("yrcx://yripccomponentdevice/sendgetnetspotpairstatecmd", params11) {
                var response11 = it as? YRMiddleServiceResponse<IpcSendCmdResult<Int>>
                YRLog.d { "-->> debug testYRIpcMiddleService sendgetnetspotpairstatecmd response11 ${response11.toString()}" }
            }
        }
    }

    private fun obtainUid() : String {
        return ""
    }

}