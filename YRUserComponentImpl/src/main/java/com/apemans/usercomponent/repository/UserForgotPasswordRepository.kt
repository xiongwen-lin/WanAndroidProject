package com.apemans.usercomponent.repository

import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.*
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.RegisterSendBody
import com.apemans.userapi.request.RegisterVerifyBody
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.user.util.CountryUtil
import com.apemans.usercomponent.userapi.UserApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

object UserForgotPasswordRepository {

    suspend fun sendVerifyCode(account : String, countryCode : String, verifyType : Int) =
        userForgotPassword(account, countryCode)
            .map {
                UserApiHelper.userApi.forgotPwSendCode(RegisterSendBody(account, it))
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        respose.code
                    }
            }.single()

    suspend fun checkVerifyCode(account: String, verifycode : String, countryCode : String, type : Int) =
        userForgotPassword(account, countryCode)
            .map {
                UserApiHelper.userApi.loginVerifyCode(RegisterVerifyBody(account, verifycode))
                    .flowOn(Dispatchers.IO)
                    .map { respose ->
                        respose.code
                    }
            }.single()

    fun checkAccountSourceForgetPassword(account : String) =
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
                    }.single()
            }

    private fun userForgotPassword(account : String, countryCode : String) =
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