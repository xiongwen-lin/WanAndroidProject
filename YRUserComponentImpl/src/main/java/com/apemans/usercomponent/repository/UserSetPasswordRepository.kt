package com.apemans.usercomponent.repository

import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.*
import com.apemans.usercomponent.user.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.userapi.request.*
import com.apemans.usercomponent.userapi.UserApiHelper
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.nooie.sdk.encrypt.NooieEncryptService
import org.json.JSONObject

object UserSetPasswordRepository {

    fun updateUserInfo(oldPassword: String, newPassword: String) =
        UserApiHelper.userApi.updateUserInfo(ResetUserPasswordBody(UserHelper.md5Decode32(oldPassword),
                UserHelper.md5Decode32(newPassword)))
            .flowOn(Dispatchers.IO)
            .map {
                it.code
            }

    suspend fun signUpBySDK(account: String, psd: String, countryCode : String, verifyCode: String) =
        userSetPasswordPost(account, countryCode)
            .map {
                UserApiHelper.userApi.register(RegisterBody(account, UserHelper.md5Decode32(psd), verifyCode, it))
                    .flowOn(Dispatchers.IO)
                    .map { response ->
                        if (response.code == HttpCode.SUCCESS_CODE) {
                            val tuyaUid = NooieEncryptService.getInstance().getTuyaPsd(response.data.uid)
                            updateNetToken(response.data.uid, response.data.api_token, tuyaUid, account, psd)
                            if (tuyaUid != null) {
                                response.code = loginTuyaAccount(it, response.data.uid, tuyaUid).single()
                            }
                        }
                        response.code
                    }
            }.single()


    suspend fun resetPasswordBySDK(account : String, password : String, verifyCode : String, countryCode : String) =
        userSetPasswordPost(account, countryCode)
            .map {
                UserApiHelper.userApi.resetPassword(ResetPwBody(account, UserHelper.md5Decode32(password), verifyCode))
                    .flowOn(Dispatchers.IO)
                    .map {
                        it.code
                    }
            }.single()


    private fun loginTuyaAccount (countryCode : String, uid : String, passwd : String) = flow {
        var resultCode = setTuyaAccount(countryCode, uid, passwd)
        emit(resultCode)
    }.flowOn(Dispatchers.IO)

    private suspend fun setTuyaAccount(countryCode : String, uid : String, passwd : String) = suspendCoroutine<Int> {
        var hashMap = HashMap<String, Any>()
        hashMap["countryCode"] = countryCode
        hashMap["uid"] = uid
        hashMap["password"] = passwd
        YRMiddleServiceManager.requestAsync("yrcx://yrtuya/loginwithuid", hashMap) { user ->
            if (HttpCode.SUCCESS_CODE == user.code) {
                val info = JSONObject(user.data.toString())
                if (info["headPic"].toString().isNotEmpty()) {
                    YRCXSDKDataManager.userHeadPic = info["headPic"].toString()
                    var tuyaPhoto = HashMap<String,Any?>()
                    tuyaPhoto["userHeadPic"] = info["headPic"].toString()
                    YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",tuyaPhoto)
                }
                it.resume(HttpCode.SUCCESS_CODE)
            } else {
                it.resume(HttpCode.CODE_1999)
            }
        }
    }

    private fun userSetPasswordPost(account: String, countryCode : String) =
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
                    val countryUtil = CountryUtil()
                    countryCodeStr = countryUtil.getCurrentCountry(application.context).toString()
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

    private fun updateNetToken(uid : String, token : String, tuyaUid: String?, account: String, password: String) {
        var hashMap = HashMap<String,Any?>()
        // YRBusiness 解析方式
        val tokenUidParams = mutableMapOf<String, Any>()
        tokenUidParams[KEY_ENV_UID] = uid
        tokenUidParams[KEY_ENV_API_TOKEN] = token
        hashMap[KEY_ENV_TOKEN_UID_PARAMS] = tokenUidParams
        // YRCXSDK解析方式
        hashMap["uid"] = uid
        hashMap["api-token"] = token
        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
        if (tuyaUid != null) {
            YRCXSDKDataManager.userAccount = account
            YRCXSDKDataManager.userPassword = password
            hashMap["userAccount"] = account
            hashMap["userPassword"] = password
        }
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