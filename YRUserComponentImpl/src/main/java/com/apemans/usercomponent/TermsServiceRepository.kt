/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.usercomponent

import android.content.Context
import android.text.TextUtils
import com.apemans.quickui.webview.SuperWebViewActivity
import com.apemans.apisdk.define.ApiConstant
import com.apemans.business.apisdk.client.configure.YRApiConfigure
import com.apemans.business.apisdk.client.define.*
import com.apemans.usercomponent.baseinfo.configure.CountryUtil
import com.apemans.usercomponent.baseinfo.encrypt.NooieEncrypt
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import java.util.HashMap

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/7 19:19
 * 说明: 用户协议 隐私政策数据Data
 *
 * 备注:
 *
 ***********************************************************/
object TermsServiceRepository {
    private const val URL_TERMS = "http://osaio.net/terms-%s"
    private const val URL_PRIVACY_POLICY_TEMPLATE = "http://osaio.net/privacy-policy-%s-%s"

    /**
     * 获取用户协议地址 服务条款
     */
    fun getTerms(context: Context): String {
        return String.format(URL_TERMS, com.apemans.base.utils.LanguageUtil.getLanguageKeyByLocale(context))
    }

    /**
     * 获取隐私政策地址
     */
    fun getPrivacyPolicyByRegion(context: Context): String {
        val countryCode = CountryUtil.getCurrentCountry(context)
        val region = getPrivacyPolicyRegionByCountry(countryCode)
        return String.format(URL_PRIVACY_POLICY_TEMPLATE, region, com.apemans.base.utils.LanguageUtil.getLanguageKeyByLocale(context))
    }

    /**
     * 获取webView url跳转时携带的 header map
     */
    fun getWebUrlAdditionalHttpHeaders(): Map<String, String> {
        val timestamp = System.currentTimeMillis() / 1000L
        val appId = YRCXSDKDataManager.getAppid()
        val mUid = YRCXSDKDataManager.getUid()
        val mToken = YRCXSDKDataManager.getUid()
        val sign: String = if (TextUtils.isEmpty(mUid) || TextUtils.isEmpty(mToken)) NooieEncrypt.signWithoutToken(YRCXSDKDataManager.getSecret(),
            appId,
            timestamp
        ) else {
            NooieEncrypt.signWithToken(YRCXSDKDataManager.getSecret(), appId, timestamp, mUid, mToken)
        }
        val headerMap: MutableMap<String, String> = HashMap()
//        headerMap[ApiConstant.API_KEY_APP_ID] = appId
//        headerMap[ApiConstant.API_KEY_TIMESTAMP] = timestamp.toString()
//        headerMap[ApiConstant.API_KEY_UID] = mUid
//        headerMap[ApiConstant.API_KEY_TOKEN] = mToken
//        headerMap[ApiConstant.API_KEY_SIGN] = sign
        headerMap[HEADER_KEY_API_APP_ID] = appId!!
        headerMap[HEADER_KEY_API_TIME_STAMP] = timestamp.toString()
        headerMap[HEADER_KEY_API_UID] = mUid!!
        headerMap[HEADER_KEY_API_TOKEN] = mToken!!
        headerMap[HEADER_KEY_API_SIGN] = sign

        return headerMap
    }

    /**
     * 跳转隐私政策
     */
    fun openPrivacyWebSite(context: Context) {
        val privacyUrl = getPrivacyPolicyByRegion(context)
        SuperWebViewActivity.openSuperWebViewActivity(
            context,
            privacyUrl,
            getWebUrlAdditionalHttpHeaders(),
            context.getString(R.string.privacy_policy),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.theme_title_text_color),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.white),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.transparent),
            R.drawable.ic_back,
            true
        )
    }

    /**
     * 跳转用户协议 /服务条款
     */
    fun openTermsServiceWebSite(context: Context) {
        val termsServiceUrl = getTerms(context)
        SuperWebViewActivity.openSuperWebViewActivity(
            context,
            termsServiceUrl,
            getWebUrlAdditionalHttpHeaders(),
            context.getString(R.string.terms_of_service),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.theme_title_text_color),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.white),
            com.apemans.base.utils.CompatUtil.getColor(context, R.color.transparent),
            R.drawable.ic_back,
            true
        )
    }

    fun getPrivacyPolicyRegionByCountry(countryCode: String): String {
        return if (checkIsAmericaRegion(countryCode)) {
            "us"
        } else {
            "de"
        }
    }

    private val usCountryCodeList: MutableList<String> = mutableListOf()

    private fun initPrivacyPolicyCountryCodeList() {
        if (usCountryCodeList.isEmpty()) {
            usCountryCodeList.add("44")
            usCountryCodeList.add("501")
            usCountryCodeList.add("1")
            usCountryCodeList.add("1")
            usCountryCodeList.add("506")
            usCountryCodeList.add("192")
            usCountryCodeList.add("1876")
            usCountryCodeList.add("52")
            usCountryCodeList.add("51")
            usCountryCodeList.add("56")
            usCountryCodeList.add("54")
            usCountryCodeList.add("598")
            usCountryCodeList.add("595")
            usCountryCodeList.add("55")
        }
    }

    private fun checkIsAmericaRegion(countryCode: String): Boolean {
        initPrivacyPolicyCountryCodeList()
        return !TextUtils.isEmpty(countryCode) && usCountryCodeList.contains(countryCode)
    }

}