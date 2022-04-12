package com.apemans.usercomponent.repository

import android.os.Build
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.business.apisdk.client.define.KEY_ENV_API_TOKEN
import com.apemans.business.apisdk.client.define.KEY_ENV_TOKEN_UID_PARAMS
import com.apemans.business.apisdk.client.define.KEY_ENV_UID
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.TwoAuthMailBody
import com.apemans.userapi.request.TwoAutoLoginBody
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.baseinfo.configure.CountryUtil
import com.apemans.usercomponent.user.util.UserHelper
import com.apemans.usercomponent.userapi.UserApiHelper
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

object UserTwoAuthRepository {

    suspend fun sendTwoAuthCode(account : String, countryCode : String) =
        userTwoAuthPost(account, countryCode)
            .map {
                UserApiHelper.userApi.sendTwoAutoCode(TwoAuthMailBody(account, it))
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        respose.code
                    }
            }.single()

    suspend fun checkAndLogin(account : String, password : String, countryCode : String, verifyCode : String) =
        userTwoAuthPost(account, countryCode)
            .map {
                val twoAutoLoginBody = TwoAutoLoginBody(account, it, CountryUtil.getCurrentTimeZone()
                    .toFloat(), verifyCode, UserHelper.createPhoneId(application.context), Build.BRAND, Build.MODEL,"")
                UserApiHelper.userApi.twoAuthLogin(twoAutoLoginBody)
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        if (respose.code == HttpCode.SUCCESS_CODE) {
                            updateNetToken(respose.data.uid, respose.data.api_token, respose.data.refresh_token,account,password)
                        }
                        respose.code
                    }
            }.single()

    private fun userTwoAuthPost(account : String, countryCode : String) =
        UserApiHelper.userGlobalApi.getGlobalTimeAsFlow()
            .flowOn(Dispatchers.IO)
            .map {
                if (it.code == HttpCode.SUCCESS_CODE) {
                    val gapTime = it.data.time - System.currentTimeMillis()/1000L
                    updataGlobalTime(gapTime)
                    //同步全球时间到NetConfigure网络配置
//                    YRApiManager.updateNetConfigureGapTime(gapTime.toInt())
                }
                it.code
            }
            .map {
                var  countryCodeStr = countryCode
                // 根据账号获取国家码信息
                val listUser = SmartDatabase.getDatabase().userDao().getUserForAccount(account)
                if (listUser.size > 0) {
                    countryCodeStr = listUser[0].countryCode
                } else if (countryCodeStr == "") {
                    countryCodeStr = com.apemans.usercomponent.user.util.CountryUtil().getCurrentCountry(application.context).toString()
                }
                UserApiHelper.userGlobalApi.getGlobalUrlWithCountryAsFlow(countryCodeStr)
                    .flowOn(Dispatchers.IO)
                    .map {
                        if (it.code == HttpCode.SUCCESS_CODE) {
                            updataNetConfig(it)
                        }
                        countryCodeStr
                    }.single()
            }

    private fun updataGlobalTime(gapTime : Long) {
        var hashMap = HashMap<String,Any?>()
        hashMap["service_time"] = gapTime
//        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
//        YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
    }

    private fun updateNetToken(uid : String, token : String, refresh_token : String, account: String, password : String) {
        var hashMap = HashMap<String,Any?>()
        YRCXSDKDataManager.userAccount = account
        YRCXSDKDataManager.userPassword = password
        val tokenUidParams = mutableMapOf<String, Any>()
        tokenUidParams[KEY_ENV_UID] = uid
        tokenUidParams[KEY_ENV_API_TOKEN] = token
        hashMap[KEY_ENV_TOKEN_UID_PARAMS] = tokenUidParams
        hashMap["uid"] = uid
        hashMap["api-token"] = token
        hashMap["refresh_token"] = refresh_token
        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
        hashMap["userAccount"] = account
        hashMap["userPassword"] = password
        YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
    }

    private fun updataNetConfig(it : YRApiResponse<GlobalUrlResult>) {
        var hashMap = HashMap<String,Any?>()
        hashMap["web_baseurl"] = "${it.data.web}/"
        hashMap["s3_baseurl"] = "${it.data.s3}/"
        hashMap["region"] = it.data.region
        hashMap["p2p"] = it.data.p2p
        hashMap["ssUrl"] = it.data.ss
        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
        YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
    }

}