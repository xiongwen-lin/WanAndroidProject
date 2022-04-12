package com.apemans.dmcomponent.ui.pairmethod.fragment.connecthotspot

import androidx.lifecycle.asLiveData
import com.apemans.dmcomponent.utils.CountryUtil
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retry

class ConnectHotspotViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    lateinit var ssid: String
    lateinit var pwd: String

    fun sendHotspotCmd() = callbackFlow {
        val map = mapOf(
            "uid" to obtainUid(),
            "ssid" to "TP-LINK_HyFi_49",
            "psd" to "nooie666",
//            "ssid" to ssid,
//            "psd" to pwd,
            "region" to obtainRegion(),
            "zone" to CountryUtil.getCurrentTimezone() + ".00",
            "encrypt" to "WPA"
        )
        YRMiddleServiceManager.requestAsync(
            "yrcx://yripccomponentdevice/sendstartnetspotpaircmd",
            mapOf("extra" to Gson().toJson(map))
        ) {
            val code = it.data as? Int
            if (code == 1) {
                trySend(code)
            } else {
                cancel("$code", HotspotCmdException(code ?: -1, "$code"))
            }
        }
        awaitClose {  }
    }
        .retry(3) {
            it is CancellationException && it.cause is HotspotCmdException
        }
        .catchWith(exceptionFlow)
        .asLiveData()

    fun getHotspotState() = callbackFlow {
        YRMiddleServiceManager.requestAsync(
            "yrcx://yripccomponentdevice/sendgetnetspotpairstatecmd",
            emptyMap()
        ) {
            val data = it.data as? CmdResult
            val code = data?.code
            if (code == 1) {
                trySend(code)
            } else if(code == 3){

            } else {
                cancel("$code", HotspotCmdException(code ?: -1, "$code"))
            }
        }
        awaitClose {  }
    }
        .retry {
            if (it is CancellationException && it.cause is HotspotCmdException) {
                delay(2000)
                true
            } else {
                false
            }
        }
        .catchWith(exceptionFlow)
        .asLiveData()

    private fun obtainUid() : String {
        return ""
    }

    private fun obtainRegion() : String {
        return ""
    }
}

class HotspotCmdException(
    val code: Int,
    message: String
) : Exception(message)

data class CmdResult(val code: Int)