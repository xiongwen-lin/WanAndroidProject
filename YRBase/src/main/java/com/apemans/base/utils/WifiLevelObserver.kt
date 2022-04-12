package com.apemans.base.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 设置等级Level计算方法过时
 * WifiManager.calculateSignalLevel(
 * wifiManager.connectionInfo.rssi,
 * levels)
 * 故不提供设置Level
 */
@RequiresApi(Build.VERSION_CODES.R)
class WifiLevelObserver(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    private val levelCallback: (Int) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiManager.RSSI_CHANGED_ACTION -> {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                levelCallback(wifiManager.calculateSignalLevel(wifiManager.connectionInfo.rssi))
            }
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        stop()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        start()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        source.lifecycle.removeObserver(this)
                    }
                    else -> {
                        //Do nothing
                    }
                }
            }
        })
    }

    private fun start() {
        context.applicationContext.registerReceiver(
            this,
            IntentFilter(WifiManager.RSSI_CHANGED_ACTION)
        )

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        levelCallback(wifiManager.calculateSignalLevel(wifiManager.connectionInfo.rssi))
    }

    private fun stop() {
        context.applicationContext.unregisterReceiver(this)
    }
}