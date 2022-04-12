package com.apemans.usercomponent.mine.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import java.lang.Exception
import java.net.URLConnection
import java.util.regex.Pattern

object Util {
    /**
     * get version code
     *
     * @param ctx
     * @return
     */
    fun getLocalVersion(ctx: Context): Int {
        var localVersion = 0
        try {
            val packageInfo = ctx.applicationContext
                .packageManager
                .getPackageInfo(ctx.packageName, 0)
            localVersion = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return localVersion
    }

    /**
     * get version name
     *
     * @param ctx
     * @return
     */
    fun getLocalVersionName(ctx: Context): String {
        var localVersion = ""
        try {
            val packageInfo = ctx.applicationContext
                .packageManager
                .getPackageInfo(ctx.packageName, 0)
            localVersion = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return localVersion
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    fun checkEmail(email: String?): Boolean {
        var flag = false
        flag = try {
            //use do
            //String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            //String check = "^\\\\w+((-\\\\w+)|(\\\\.\\\\w+))*@\\\\w+(\\\\.\\\\w{2,3}){1,3}$";
            //            final String check = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            //final String check ="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,32}){1,32}$";
            val check =
                "[A-Z0-9a-z\\._\\%\\+\\-]+@[A-Za-z0-9\\._\\%\\+\\-]+\\.[A-Za-z0-9_\\%\\+\\-]{2,64}"
            val regex = Pattern.compile(check)
            val matcher = regex.matcher(email)
            matcher.matches()
        } catch (e: Exception) {
            false
        }
        return flag
    }

    /**
     * Get the Mime Type from a File
     *
     * @param fileName 文件名
     * @return 返回MIME类型
     * thx https://www.oschina.net/question/571282_223549
     * add by fengwenhua 2017年5月3日09:55:01
     */
    private fun getMimeType(fileName: String?): String {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(fileName)
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val deviceModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = Build.BRAND

    fun convertDeviceVersion(verStr: String): Int {
        var verNum = 0
        if (!TextUtils.isEmpty(verStr)) {
            try {
                val verArr = verStr.split("\\.".toRegex()).toTypedArray()
                val len = verArr.size
                if (len < 3) {
                    return 0
                }
                verNum = verArr[0].toInt() * 1000 + verArr[1].toInt() * 100 + verArr[2].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return verNum
    }

    fun delayTask(millisecond: Int, listener: OnDelayTaskFinishListener) {
        Handler(Looper.getMainLooper()).postDelayed({ listener.onFinish() }, millisecond.toLong())
    }

    /*fun formatWeek(week: Week?): String {
        return when (week) {
            Mon -> NooieApplication.get().getString(R.string.w_monday)
            Tues -> NooieApplication.get().getString(R.string.w_tuesday)
            Wed -> NooieApplication.get().getString(R.string.w_wednesday)
            Thur -> NooieApplication.get().getString(R.string.w_thursday)
            Fri -> NooieApplication.get().getString(R.string.w_friday)
            Sat -> NooieApplication.get().getString(R.string.w_saturday)
            Sun -> NooieApplication.get().getString(R.string.w_sunday)
            else -> NooieApplication.get().getString(R.string.unknown)
        }
    }*/

    interface OnDelayTaskFinishListener {
        fun onFinish()
    }
}