package com.apemans.usercomponent.repository

import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.userapi.UserApiHelper
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AccountRepository {

    fun loadUserInfo() =
        UserApiHelper.userApi.getUserInfo()
            .map {
                it
            }
            .flowOn(Dispatchers.IO)

    fun logout() =
        UserApiHelper.userApi.logout()
            .map {
                it.code
            }
            .flowOn(Dispatchers.IO)


    fun setPortrait(photo: File) = flow {
        //把数据发送出去
        val result = setPortrait2(photo)
        emit(result)
    }.flowOn(Dispatchers.IO)

    private suspend fun setPortrait2(photo: File) = suspendCoroutine<String> {
        var hashMap = HashMap<String, Any>()
        hashMap["file"] = photo
        YRMiddleServiceManager.requestAsync("yrcx://yrtuya/uploaduseravatar", hashMap) { tuya ->
            if (HttpCode.SUCCESS_CODE == tuya.code) {
                if (null != tuya.data) {
                    YRCXSDKDataManager.userHeadPic = tuya.data.toString()
                    var tuyaPhoto = HashMap<String,Any?>()
                    tuyaPhoto["userHeadPic"] = tuya.data.toString()
                    YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",tuyaPhoto)
                }
                it.resume(ConstantValue.SUCCESS)
            } else {
                it.resume(ConstantValue.ERROR)
            }
        }
    }
}