package com.apemans.usercomponent.repository

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.business.apisdk.client.define.KEY_ENV_API_TOKEN
import com.apemans.business.apisdk.client.define.KEY_ENV_TOKEN_UID_PARAMS
import com.apemans.business.apisdk.client.define.KEY_ENV_UID
import com.apemans.usercomponent.user.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import com.nooie.sdk.encrypt.NooieEncryptService
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.logger.YRLog
import com.apemans.userapi.request.*
import com.apemans.usercomponent.baseinfo.configure.PhoneUtil
import com.apemans.usercomponent.userapi.UserApiHelper
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object UserSigninRepository {

    private const val MAX_VERIFY_CODE_LIMIT_TIME = 180L
    private var limitTime = MAX_VERIFY_CODE_LIMIT_TIME

    private val _mVerifyCodeLimitTime = MutableLiveData<String>()
    val mVerifyCodeLimitTime: LiveData<String> get() = _mVerifyCodeLimitTime

    fun signIn(account: String, password: String, countryCode: String) =
        userSigninPost(account, countryCode)
            .map {
                var loginBody = LoginBody(account, UserHelper.md5Decode32(password), it, UserHelper.createPhoneId(application.context),
                    UserHelper.getCurrentTimeZone().toFloat(), Build.BRAND)
                UserApiHelper.userApi.login(loginBody)
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        if (respose.code == HttpCode.SUCCESS_CODE) {
                            updateNetToken(respose.data.uid, respose.data.api_token,account,password,respose.data.register_country)
                            val uidTuYa = NooieEncryptService.getInstance().getTuyaPsd(respose.data.uid)
                            respose.code = loginTuyaAccount(/*it*/respose.data.register_country, respose.data.uid, uidTuYa).single()
                        }
                        YRLog.d {
                            "11111111111111111111111111respose.code: ${respose.code}"
                        }
                        respose.code
                    }.single()
            }

    fun sendRegisterVerifyCode(account: String, countryCode: String) =
        userSigninPost(account, countryCode)
            .map {
                UserApiHelper.userApi.registerSendCode(RegisterSendBody(account, it))
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        respose.code
                    }.single()
            }

    fun checkRegisterVerifyCodeBySDK(account: String, verifyCode: String, countryCode: String) =
        userSigninPost(account, countryCode)
            .map {
                UserApiHelper.userApi.registerVerifyCode(RegisterVerifyBody(account, verifyCode))
                    .flowOn(Dispatchers.IO)
                    .map {
                        it.code
                    }.single()
            }

    fun reportUserInfo(
        account: String, password: String, zone: Float, country: String,
        type: Int, nickname: String, photo: String, phoneCode: String,
        deviceType: Int, pushType: Int, pushToken: String, appVersion: String,
        appVersionCode: String, phoneModel: String, phoneBrand: String,
        phoneVersion: String, phoneScreen: String, language: String, packageName: String
    ) =
        UserApiHelper.userApi.uploadUserInfo(UploadUserBody(pushType, deviceType, pushToken, phoneCode,
            country, zone, nickname,
            photo, appVersion, appVersionCode, phoneModel, phoneBrand, phoneVersion, phoneScreen,
            language, packageName, PhoneUtil.getPhoneName(application.context)))
            .flowOn(Dispatchers.IO)
            .map {
                YRLog.d { "-->> debug reportUserInfo${it.code}" }
                if (it.code == HttpCode.SUCCESS_CODE) {
                    // 更新push token
                    YRLog.d { "-->> debug reportUserInfo" }
                }
                it.code
            }

    suspend fun checkAccountSourceForSignIn(account : String, psd : String) =
        UserApiHelper.userGlobalApi.getGlobalTimeAsFlow()
            .flowOn(Dispatchers.IO)
            .map {
                if (it.code == HttpCode.SUCCESS_CODE) {
                    val gapTime = it.data.time - System.currentTimeMillis() / 1000L
                    updataGlobalTime(gapTime)
                    //同步全球时间到NetConfigure网络配置
//                    YRApiManager.updateNetConfigureGapTime(gapTime.toInt())
                }
                it.code
            }.map {
                var countryCodeStr = ""
                // 根据账号获取国家码信息
                val listUser = SmartDatabase.getDatabase().userDao().getUserForAccount(account)
                if (listUser.size > 0) {
                    countryCodeStr = listUser[0].countryCode
                } else if (countryCodeStr == "") {
                    countryCodeStr = CountryUtil().getCurrentCountry(application.context).toString()
                }
                UserApiHelper.userGlobalApi.getGlobalUrlWithCountryOrAccountAsFlow(countryCodeStr, account)
                    .flowOn(Dispatchers.IO)
                    .map {
                        if (it.code == HttpCode.SUCCESS_CODE) {
                            updataNetConfig(it)
                        }
                        it
                    }
            }.single()

    private fun loginTuyaAccount(countryCode: String, uid: String, passwd: String) = flow {
        var resultCode = setTuyaAccount(countryCode, uid, passwd)
        emit(resultCode)
    }.flowOn(Dispatchers.IO)

    private suspend fun setTuyaAccount(countryCode: String, uid: String, passwd: String) = suspendCoroutine<Int> {
        var hashMap = HashMap<String, Any>()
        hashMap["countryCode"] = countryCode
        hashMap["uid"] = uid
        hashMap["password"] = passwd
        YRMiddleServiceManager.requestAsync("yrcx://yrtuya/loginwithuid", hashMap) { user ->
            YRLog.d { "11111111111111111111111111user.code: ${user.code}" }
            if (HttpCode.SUCCESS_CODE == user.code) {
                val info = JSONObject(user.data.toString())
                if (info["headPic"].toString().isNotEmpty()) {
                    YRCXSDKDataManager.userHeadPic = info["headPic"] as String?
                    var tuyaPhoto = HashMap<String,Any?>()
                    tuyaPhoto["userHeadPic"] = info["headPic"].toString()
                    YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",tuyaPhoto)
                }
                it.resume(HttpCode.SUCCESS_CODE)
            } else {
                it.resumeWithException(IllegalStateException(user.code.toString()))
            }
        }
    }

    private fun userSigninPost(account: String, countryCode : String) =
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
                    countryCodeStr = CountryUtil().getCurrentCountry(application.context).toString()
                }
                UserApiHelper.userGlobalApi.getGlobalUrlWithCountryOrAccountAsFlow(countryCodeStr, account)/*getGlobalUrlWithCountryAsFlow(countryCodeStr)*/
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

    private fun updateNetToken(uid : String, token : String, account: String, password: String, countryCode: String) {
        var hashMap = HashMap<String,Any?>()
        val tokenUidParams = mutableMapOf<String, Any>()
        tokenUidParams[KEY_ENV_UID] = uid
        tokenUidParams[KEY_ENV_API_TOKEN] = token
        hashMap[KEY_ENV_TOKEN_UID_PARAMS] = tokenUidParams
        hashMap["uid"] = uid
        hashMap["api-token"] = token
        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
        YRCXSDKDataManager.userAccount = account
        YRCXSDKDataManager.userPassword = password
        YRCXSDKDataManager.userCountryCode = countryCode
        hashMap["userAccount"] = account
        hashMap["userPassword"] = password
        hashMap["userCountryCode"] = countryCode
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