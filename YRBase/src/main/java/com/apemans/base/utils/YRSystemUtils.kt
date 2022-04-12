package com.apemans.base.utils

import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.longan.application
import com.dylanc.longan.topActivity

/**
 * 系统功能服务
 */
object YRSystemUtils {

    /**
     * 跳转到系统wifi界面
     */
    private const val WIFI_IP_REQUEST = 8080
    fun gotoSystemWifiView() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        topActivity.startActivityForResult(intent, WIFI_IP_REQUEST)
    }

    /**
     * 获取当前网络地址
     */
    fun getSystemNetIp() : String {
        return getNetIp()
    }

    // 获取连接网络ip地址
    private fun getNetIp(): String {
        val wifiManager = application.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val dhcp = wifiManager.dhcpInfo
        val ip = dhcp.gateway
        return intToIp(ip)
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

}