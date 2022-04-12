package com.apemans.tuya.component

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.apemans.base.middleservice.*
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.tuya.component.bean.TuyaMessageBean
import com.apemans.tuya.component.constants.isOpen
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.utils.TuyaToolUtil
import com.dylanc.longan.topActivity
import com.google.gson.Gson
import com.tuya.smart.android.user.api.IBooleanCallback
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.api.MicroContext
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.panelcaller.api.AbsPanelCallerService
import com.tuya.smart.sdk.api.ITuyaDataCallback
import com.tuya.smart.sdk.bean.message.MessageBean
import com.tuya.smart.wrapper.api.TuyaWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class YRTuyaKit : YRMiddleService() {

    override fun registerSelf(p0: Application) {
        YRMiddleServiceManager.registerServiceClass("yrtuya", YRTuyaKit::class.java.name, "涂鸦组件")
    }

    override fun requestAsync(protocol: MutableMap<String, String>, parameters: MutableMap<String, Any?>, listener: YRMiddleServiceListener) {
        when (protocol[YRMiddleServiceFunctionName].orEmpty().lowercase()) {
            "querymessage" -> {
                TuyaRepository.getMessageList(object : ITuyaDataCallback<List<MessageBean>> {
                    override fun onSuccess(messageList: List<MessageBean>?) {
                        val tuyamessageList: List<TuyaMessageBean> = messageList?.map {
                            TuyaToolUtil.convertTuyaMessageBean(it)
                        }.orEmpty()
                        val resultJson = JsonConvertUtil.convertToJson(tuyamessageList) ?: ""
                        listener.onCall(okResponse(resultJson))
                    }

                    override fun onError(errorCode: String, errorMessage: String) {
                        listener.onCall(okResponse(""))
                    }
                })
            }
            "loginwithuid" -> {
                val countryCode = parameters["countryCode"] as String?
                val uid = parameters["uid"] as String?
                val pwd = parameters["password"] as String?
                TuyaHomeSdk.getUserInstance().loginWithUid(countryCode, uid, pwd, object : ILoginCallback {
                    override fun onSuccess(user: User) {
                        listener.onCall(okResponse(JsonConvertUtil.convertToJson(user).orEmpty()))
                    }

                    override fun onError(code: String, error: String) {
                        listener.onCall(YRMiddleServiceResponse(-100, error, null))
                    }
                })
            }
            "uploaduseravatar" -> {
                val file = parameters["file"] as File?
                TuyaHomeSdk.getUserInstance().uploadUserAvatar(file, object : IBooleanCallback {
                    override fun onSuccess() {
                        listener.onCall(okResponse(TuyaHomeSdk.getUserInstance().user?.headPic))
                    }

                    override fun onError(code: String?, error: String?) {
                        YRMiddleServiceResponse(-100, error, null)
                    }
                })
            }
            "onlogin" -> {
                TuyaWrapper.onLogin()
                listener.onCall(okResponse(null))
            }
        }
    }

    override fun request(protocol: MutableMap<String, String>, parameters: MutableMap<String, Any>): YRMiddleServiceResponse<*> {
        return when (protocol[YRMiddleServiceFunctionName].orEmpty().lowercase()) {
            "startpanel" -> {
                val service = MicroContext.getServiceManager().findServiceByInterface<AbsPanelCallerService>(AbsPanelCallerService::class.java.name)
                service.goPanelWithCheckAndTip(topActivity, parameters["deviceId"] as String?)
                YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, null, null)
            }
            "onlogin" -> {
                TuyaWrapper.onLogin()
                YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, null, null)
            }
            else -> TODO()
        }
    }

    override fun listening(
        protocol: MutableMap<String, String>,
        lifeCycle: Any,
        parameters: MutableMap<String, Any>,
        listener: YRMiddleServiceListener
    ) {
        val lifecycleOwner = lifeCycle as LifecycleOwner
        when (protocol[YRMiddleServiceFunctionName].orEmpty().lowercase()) {
            "querydevice" -> {
                if (parameters["type"] == "tuya" || parameters["type"] == "all") {
                    GlobalScope.launch {
                        TuyaRepository.queryDeviceList()
                            .catch {
                                listener.onCall(YRMiddleServiceResponse(-100, it.message, null))
                            }
                            .collect { list ->
                                val devices = list.orEmpty().map {
                                    mapOf(
                                        "uuid" to it.devId,
                                        "name" to it.name,
                                        "online" to it.isOnline,
                                        "version" to it.appRnVersion,
                                        "iconUrl" to it.iconUrl,
                                        "productId" to it.devId,
                                        "platform" to "tuya",
                                        "category" to "ea",
                                        "eventSchema" to
                                                mapOf(
                                                    "click" to mapOf(
                                                        "id" to "1",
                                                        "url" to "yrcx://yrtuya/startpanel",
                                                        "param" to mapOf(
                                                            "deviceId" to it.devId,
                                                            "actionType" to ""
                                                        ),
                                                        "value" to mapOf(
                                                            "actionType" to "onlineClick"
                                                        )
                                                    ),
                                                    "switch" to mapOf(
                                                        "id" to "2",
                                                        "url" to "yrcx://yrtuya/switch",
                                                        "param" to mapOf(
                                                            "deviceId" to it.devId,
                                                            "state" to ""
                                                        ),
                                                        "value" to mapOf(
                                                            "state" to it.isOpen
                                                        )
                                                    )
                                                ),
                                    )
                                }
                                listener.onCall(
                                    YRMiddleServiceResponse(
                                        YRMiddleConst.MIDDLE_SUCCESS, null,
                                        Gson().toJson(devices)
                                    )
                                )
                            }
                    }
                } else {
                    listener.onCall(YRMiddleServiceResponse(YRMiddleConst.MIDDLE_SUCCESS, null, Gson().toJson(mapOf("m" to "[]"))))
                }
            }
        }
    }
}