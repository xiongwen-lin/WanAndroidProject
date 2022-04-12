package com.apemans.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 11:38 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object NotificationUtil {

    fun isNotificationEnabled(context: Context?) : Boolean {
        return context?.let {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } ?: true
    }

    fun openNotificationSetting(context: Context?) {
        if (context == null) {
            return
        }
        try {
            val localIntent = Intent()
            // 直接跳转到应用通知设置的代码
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ) {
                localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                localIntent.putExtra("app_package", context.packageName)
                localIntent.putExtra("app_uid", context.applicationInfo.uid)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                localIntent.addCategory(Intent.CATEGORY_DEFAULT)
                localIntent.data = Uri.parse("package:" + context.packageName)
            } else {
                // 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts("package", context.packageName, null)
                } else {
                    localIntent.action = Intent.ACTION_VIEW
                    localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails")
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
                }
            }
            localIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(localIntent)
        } catch (e: Exception) {
        }
    }

}