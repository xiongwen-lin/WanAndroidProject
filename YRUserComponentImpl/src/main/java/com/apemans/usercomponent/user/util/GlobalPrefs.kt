package com.apemans.usercomponent.user.util

import android.text.TextUtils
import com.apemans.logger.YRLog
import com.apemans.usercomponent.baseinfo.base.BasisData
import java.lang.Exception

object GlobalPrefs {

    fun setPrivacyIsReadByAccount(account: String?, isRead: Boolean) {
        if (TextUtils.isEmpty(account)) {
            return
        }
        try {
            BasisData.getInstance().putBool(account, isRead)
        } catch (e: Exception) {
            YRLog.e { e }
        }
    }

    fun getPrivacyIsReadByAccount(account: String?): Boolean {
        if (TextUtils.isEmpty(account)) {
            return false
        }
        try {
            return BasisData.getInstance().getBool(account, false)
        } catch (e: Exception) {
            YRLog.e { e }
        }
        return false
    }
}