package com.apemans.smartipcimpl.utils

import java.lang.StringBuilder
import java.util.regex.Pattern

/**
 * IPv4IntTransformer
 * IPv4地址和int数字的互相转换
 *
 * @author Administrator
 * @date 2019/4/19
 */
object IPv4IntTransformer {
    /**
     * IPv4地址转换为int类型数字
     *
     */
    fun ip2Integer(ipv4Addr: String): Int {
        // 判断是否是ip格式的
        if (!isIPv4Address(ipv4Addr)) {
            return 0
        }

        // 匹配数字
        val number = "\\d+"
        val pattern = Pattern.compile(number)
        val matcher = pattern.matcher(ipv4Addr)
        var result = 0
        var counter = 0
        while (matcher.find()) {
            val value = matcher.group().toInt()
            result = value shl 8 * (3 - counter++) or result
        }
        return result
    }

    /**
     * 判断是否为ipv4地址
     *
     */
    private fun isIPv4Address(ipv4Addr: String): Boolean {
        //0-255的数字
        val lower = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])"
        val regex = "$lower(\\.$lower){3}"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(ipv4Addr)
        return matcher.matches()
    }

    /**
     * 将int数字转换成ipv4地址
     *
     */
    fun integer2Ip(ip: Int): String {
        val sb = StringBuilder()
        var num = 0
        // 是否需要加入'.'
        var needPoint = false
        for (i in 0..3) {
            if (needPoint) {
                sb.append('.')
            }
            needPoint = true
            val offset = 8 * (3 - i)
            num = ip shr offset and 0xff
            sb.append(num)
        }
        return sb.toString()
    }

    fun bigNumToIP(ip: Long): String {
        val sb = StringBuilder()
        for (i in 0..3) {
            sb.append(ip ushr i * 8 and 0x000000ff)
            if (i != 3) {
                sb.append('.')
            }
        }
        return sb.toString()
    }

    fun littleNumToIP(ip: Long): String {
        val sb = StringBuilder()
        for (i in 3 downTo 0) {
            sb.append(ip ushr i * 8 and 0x000000ff)
            if (i != 0) {
                sb.append('.')
            }
        }
        return sb.toString()
    }

    /*
    fun convertIpAndPort(url: String): UrlBean {
        val urlBean = UrlBean()
        urlBean.setUrl(url)
        try {
            if (!TextUtils.isEmpty(url)) {
                if (url.contains("https://")) {
                    urlBean.setEncryption(true)
                    var ip = url.replace("https://", "")
                    ip = ip.substring(0, ip.indexOf("/"))
                    if (ip.contains(":")) {
                        val ipAndPort = ip.split(":".toRegex()).toTypedArray()
                        if (ipAndPort != null && ipAndPort.size == 2) {
                            urlBean.setIp(ipAndPort[0])
                            urlBean.setPort(Integer.valueOf(ipAndPort[1]))
                        }
                    } else {
                        urlBean.setIp(ip)
                        urlBean.setPort(443)
                    }
                } else if (url.contains("http://")) {
                    urlBean.setEncryption(false)
                    var ip = url.replace("http://", "")
                    ip = ip.substring(0, ip.indexOf("/"))
                    if (ip.contains(":")) {
                        val ipAndPort = ip.split(":".toRegex()).toTypedArray()
                        if (ipAndPort != null && ipAndPort.size == 2) {
                            urlBean.setIp(ipAndPort[0])
                            urlBean.setPort(Integer.valueOf(ipAndPort[1]))
                        }
                    } else {
                        urlBean.setIp(ip)
                        urlBean.setPort(80)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return urlBean
    }

     */
}