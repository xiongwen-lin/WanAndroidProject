package com.apemans.dmcomponent.repository

import com.apemans.business.apisdk.ApiManager
import com.apemans.dmapi.webapi.BindDevice
import com.apemans.dmapi.webapi.DeviceBindingBody
import com.apemans.dmapi.webapi.UpdateDeviceNameBody
import com.apemans.dmcomponent.webapi.IDeviceApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

/**
 * @author Dylan Cai
 */
object PairRepository {

    private const val QUERY_BIND_TYPE_NONE = 0
    private const val QUERY_BIND_TYPE_SUCCESS = 1
    private const val QUERY_BIND_TYPE_BOUND_BY_OTHER = 2
    private const val QUERY_BIND_TYPE_FAIL = 3
    private const val QUERY_BIND_TYPE_REPEAT = 4

    private const val BIND_TYPE_OWNER = 1
    private const val BIND_TYPE_SHARE = 2

    private const val ONLINE_STATUS_OFF = 0
    private const val ONLINE_STATUS_ON = 1

    private val api by lazy { ApiManager.getService(IDeviceApi::class.java) }

    fun scanPairedDevice() = flow<List<BindDevice>> {
        val data = api.getDeviceBindStatus().single().data
        when (data.type) {
            QUERY_BIND_TYPE_NONE -> {
                throw IllegalStateException("")
            }
            QUERY_BIND_TYPE_SUCCESS -> {
                val list = api.getRecentBindDevices().single().data
//                for (device in list) {
//                    if (device.online == ONLINE_STATUS_ON) {
//                        break
//                    } else {
//                        throw IllegalStateException("")
//                    }
//                }
                if (list.isNotEmpty())
                    emit(list)
                else
                    throw IllegalStateException("")
            }
            QUERY_BIND_TYPE_BOUND_BY_OTHER -> {
                throw IllegalStateException("")
            }
            QUERY_BIND_TYPE_FAIL -> {
                throw IllegalStateException("")
            }
            QUERY_BIND_TYPE_REPEAT -> {
                throw IllegalStateException("")
            }
        }
    }

    fun bindDevice(uuid: String) = api.bindDevice(DeviceBindingBody(uuid))

    fun updateDeviceName(deviceId: String, name: String) =
        api.updateDeviceName(UpdateDeviceNameBody(deviceId, name))
}