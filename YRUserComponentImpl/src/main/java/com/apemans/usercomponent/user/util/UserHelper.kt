package com.apemans.usercomponent.user.util

import android.content.Context
import android.os.Build
import com.apemans.usercomponent.baseinfo.configure.PhoneUtil
import com.apemans.usercomponent.baseinfo.encrypt.MD5Util
import com.dylanc.longan.application
import com.dylanc.longan.context
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object UserHelper {
    /**
     * 32位MD5加密
     * @param text -- 待加密内容
     * @return
     */
    fun md5Decode32(text: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest:ByteArray = instance.digest(text.toByteArray())
            var sb : StringBuffer = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                var i :Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取国家码 cn
     */
    fun getCountryZipCode() : String {
        val locale : Locale = application.context.resources.configuration.locale;
        val  countryZipCode = locale.country;
        return countryZipCode
    }

    fun getCurrentTimeZone(): Int {
        return getTimezoneCodeById(getTimeZoneId())
    }

    private fun getTimeZoneId(): String? {
        val tz = TimeZone.getDefault()
        return tz.id
    }

    private fun getTimezoneCodeById(timezoneId: String?): Int {
        val timeZoneByRawOffset: Int = if (Build.VERSION.SDK_INT >= 24) {
            val timeZone =
                android.icu.util.TimeZone.getTimeZone(timezoneId)
            timeZone.rawOffset
        } else {
            val timeZone = SimpleTimeZone.getTimeZone(timezoneId)
            timeZone.rawOffset
        }
        return timeZoneByRawOffset / 1000 / 3600
    }

    fun createPhoneId(context: Context?): String {
        val infoSb = StringBuilder()
        infoSb.append(Build.BRAND)
        infoSb.append(Build.BOARD)
        infoSb.append(Build.HARDWARE)
        infoSb.append(Build.MODEL)
        infoSb.append(context?.let { PhoneUtil.getAndroidId(it) })
        return MD5Util.MD5Hash(infoSb.toString())
    }
}