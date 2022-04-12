package com.apemans.base.utils

import android.util.Log
import java.net.NetworkInterface
import java.net.SocketException

/**
 * @author caro
 * @date 2020/10/9
 */
object IpUtil {
    /**
     * 将ipInteger转化成string类型的Id字符串
     * 最终输出为如: 192.168.42.1
     * @param ipInteger
     * @return ip
     */
    fun intToIp(ipInteger: Int): String {
        val sb = StringBuffer()
        var b = ipInteger shr 0 and 0xff
        sb.append("$b.")
        b = ipInteger shr 8 and 0xff
        sb.append("$b.")
        b = ipInteger shr 16 and 0xff
        sb.append("$b.")
        b = ipInteger shr 24 and 0xff
        sb.append(b)
        return sb.toString()
    }

    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val enumIpAddress = en.nextElement().inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val inetAddress = enumIpAddress.nextElement()
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
            Log.e("IpUtil", "getLocalIpAddress SocketException:$ex")
        }
        return null
    }

}