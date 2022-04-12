package com.apemans.dmcomponent.utils

import android.content.Context
import android.net.wifi.WifiManager
import com.dylanc.longan.application

/**
 * @author Dylan Cai
 */

val wifiSSID: String?
    get() = (application.getSystemService(Context.WIFI_SERVICE) as WifiManager?)
        ?.connectionInfo?.ssid?.let {
            if (it.length > 2 && it.first() == '"' && it.last() == '"') {
                it.substring(1, it.length - 1)
            } else {
                it
            }
        }