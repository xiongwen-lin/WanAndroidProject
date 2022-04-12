package com.apemans.usercomponent.mine.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.apemans.logger.YRLog
import java.lang.Exception
import java.net.URLConnection
import java.util.regex.Pattern

object CommonUtil {

    interface OnDelayTimeFinishListener {
        fun onFinish()
    }

    fun delayAction(millisecond: Int, listener: OnDelayTimeFinishListener) {
        Handler(Looper.getMainLooper()).postDelayed({ listener.onFinish() }, millisecond.toLong())
    }

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
    fun getLocalVersionName(ctx: Context): String? {
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
     * format error message
     *
     * @param throwable
     * @return
     */
    fun errorMsg(throwable: Throwable?): String? {
        return ""
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    fun checkEmail(email: String?): Boolean {
        val expression =
            "[A-Z0-9a-z\\._\\%\\+\\-]+@[A-Za-z0-9\\._\\%\\+\\-]+\\.[A-Za-z0-9_\\%\\+\\-]{2,64}"
        return Pattern.matches(expression, email)
    }


    /**
     * Get the Mime Type from a File
     *
     * @param fileName 文件名
     * @return 返回MIME类型
     * thx https://www.oschina.net/question/571282_223549
     * add by fengwenhua 2017年5月3日09:55:01
     */
    fun getMimeType(fileName: String?): String? {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(fileName)
    }


    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    fun getSystemVersion(): String? {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    private val MIN_DELAY_TIME = 1000 // 两次点击间隔不能少于1000ms

    private var lastClickTime: Long = 0

    fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }


    fun gotoBrower(context: Context?, url: String?) {
        if (context == null || TextUtils.isEmpty(url)) {
            return
        }
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(url) //要跳转的网页
            intent.data = content_url
            if (intent.resolveActivity(context.packageManager) != null) {
                YRLog.e("-->> SystemUtil gotoBrower has browser")
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            YRLog.e("-->> SystemUtil gotoBrower fail e=" + if (e != null) e.message else "")
            e.printStackTrace()
        }
    }
}