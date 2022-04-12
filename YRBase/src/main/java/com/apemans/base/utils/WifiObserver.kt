package com.apemans.base.utils

import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


/**
 * WiFi连接状态
 * LifecycleOwner 生命周期管理
 */
class WifiObserver(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    private val event: Event,
    val numLevels: Int = 4,
) {

    interface Event {
        fun onLost()
        fun onAvailable()
        fun wifiRSSILevelChange(rssiLevel: Int)
    }

    private var visible = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        /*网络丢失的回调*/
        override fun onLost(network: Network) {
            super.onLost(network)
            mainHandler.removeCallbacks(lostRunnable)
            mainHandler.post(lostRunnable)
        }

        /*网络可用的回调*/
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            mainHandler.removeCallbacks(availableRunnable)
            mainHandler.post(availableRunnable)
        }

        /*当建立网络连接时，回调连接的属性*/
        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }

        /*按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次*/
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onCapabilitiesChanged(
            network: Network, networkCapabilities: NetworkCapabilities,
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                //判断是wifi还是移动网络
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiRssiLevel =
                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q) {
                            wifiManager.calculateSignalLevel(networkCapabilities.signalStrength)
                        } else {
                            WifiManager.calculateSignalLevel(networkCapabilities.signalStrength, numLevels)
                        }
                    //Log.i("WifiObserver", "onCapabilitiesChanged - wifiRssiLevel: $wifiRssiLevel")
                    event.wifiRSSILevelChange(wifiRssiLevel)
                }
            }
        }

        /*在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调*/
        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        /*按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调*/
        override fun onUnavailable() {
            super.onUnavailable()
        }

    }

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> observe()
                    Lifecycle.Event.ON_DESTROY -> {
                        unObserve()
                        mainHandler.removeCallbacksAndMessages(null)
                    }
                    Lifecycle.Event.ON_START -> visible = true
                    Lifecycle.Event.ON_STOP -> visible = false
                }
            }
        })

    }

    private val lostRunnable = Runnable {
        event.onLost()
    }
    private val availableRunnable = Runnable {
        event.onAvailable()
    }

    private fun observe() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(NetworkRequest.Builder().build(), networkCallback)
    }

    private fun unObserve() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}