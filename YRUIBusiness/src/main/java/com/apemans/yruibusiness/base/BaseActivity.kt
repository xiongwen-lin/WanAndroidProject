/** * Copyright (c) 2021.海龙 Inc. All rights reserved. */
@file:Suppress("unused")

package com.apemans.yruibusiness.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.observer.IApiGlobalObserver
import com.apemans.business.apisdk.ApiManager
import com.apemans.business.apisdk.client.define.RESPONSE_CODE_ACCOUNT_MOVED
import com.apemans.business.apisdk.client.define.RESPONSE_CODE_LOGIN_BY_OTHER
import com.apemans.business.apisdk.client.define.RESPONSE_CODE_LOGIN_EXPIRE
import com.apemans.business.apisdk.client.define.RESPONSE_CODE_UPDATE_SERVER_TIME
import com.apemans.yruibusiness.constants.PushCode
import com.apemans.yruibusiness.utils.LoadingBuilder
import com.apemans.yruibusiness.utils.LoadingEvent
import com.apemans.yruibusiness.ui.loadingstate.LoadingHelper
import com.apemans.logger.YRLog
import com.apemans.messagepush.push.MessagePushListener
import com.apemans.messagepush.push.MessagePushManager
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.userapi.startRouterActivityCheckLogin
import com.apemans.yruibusiness.R
import com.apemans.yruibusiness.utils.viewbinding.inflateBindingWithGeneric
import com.dylanc.longan.finishAllActivities

/**
 * ViewBindingKTX + LoadingHelper
 *
 * @author Dylan Cai
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), LoadingEvent {

    lateinit var loadingHelper: LoadingHelper private set
    lateinit var binding: VB private set
    var _vm: BaseViewModel? = null
    //val viewModel: BaseViewModel get() = _vm!!
    lateinit var activity: Activity
    lateinit var context: Context
    private var activityStyle: Int = -1
    private val loadingDialog by lazy(LazyThreadSafetyMode.NONE) {
        LoadingBuilder.loadingFactory
    }

    protected open val contentView: View get() = binding.root
    protected open val enableLoadingHelper get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContent()
        activityStyle = registerTheme()
        if (activityStyle != -1) {
            setTheme(activityStyle)
        }
        binding = inflateBindingWithGeneric(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        activity = this
        context = this
        if (enableLoadingHelper) {
            loadingHelper = LoadingHelper(contentView)
            loadingHelper.setOnReloadListener(::onReload)
        }

        onViewCreated(savedInstanceState)
        registerGlobalObserver()
    }

    override fun showLoading(message: String) {
        loadingDialog.showLoading(this, message)
    }

    override fun showMessage(message: String, delay: Int) {
        loadingDialog.showMessage(message, delay)
    }

    override fun dismissLoading(delay: Int) {
        loadingDialog.dismissLoading(delay)
    }

    /**
     * 设置布局加载前调用
     */
    protected open fun beforeSetContent() {

    }

    protected abstract fun onViewCreated(savedInstanceState: Bundle?)

    /**
     * 注册Activity Style
     */
    protected open fun registerTheme(): Int = -1

    /**
     * 注册viewModule
     * @param clazz viewModule类
     */
    inline fun <reified T : BaseViewModel> registerViewModule(clazz: Class<T>): T {
        val m = ViewModelProvider(this, defaultViewModelProviderFactory)[clazz]
        m.registerLoadingEvent(this)
        _vm = m
        return m
    }

    /**
     * 注册viewModule
     * @param factory repo provider
     * @param clazz viewModule类
     */
    inline fun <reified T : BaseViewModel> registerViewModule(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
        val m = ViewModelProvider(this, factory).get(clazz)
        m.registerLoadingEvent(this)
        _vm = m
        return m
    }

    protected fun showLoadingView() = loadingHelper.showLoadingView()

    protected fun showContentView() = loadingHelper.showContentView()

    protected fun showErrorView() = loadingHelper.showErrorView()

    protected fun showEmptyView() = loadingHelper.showEmptyView()

    protected fun showCustomView(viewType: Any) = loadingHelper.showView(viewType)

    open fun onReload() {}

    override fun onResume() {
        super.onResume()
        MessagePushManager.registerMsgListener(object : MessagePushListener {
            override fun onReceiveMessage(message: Map<String, String>) {
                convertPushMessage(message)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        MessagePushManager.unRegisterMsgListener()
    }

    private fun registerGlobalObserver() {
        ApiManager.registerGlobalObserver(object : IApiGlobalObserver {
            override fun onActionResult(code: String?, data: Bundle?) {
                when (code) {
                    RESPONSE_CODE_UPDATE_SERVER_TIME -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    RESPONSE_CODE_LOGIN_EXPIRE -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    RESPONSE_CODE_LOGIN_BY_OTHER -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    RESPONSE_CODE_ACCOUNT_MOVED -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                }
            }
        })
    }

    private fun convertPushMessage(message : Map<String, String>) {
        YRLog.d { "-->> BaseActivity convertPushMessage message $message" }
        if (message.isNullOrEmpty() || !message.contains("code")) {
            return
        }
        var code = message["code"]
        YRLog.d { "-->> BaseActivity convertPushMessage code $code" }
        convertJPushCustomMessage(code!!)
    }

    private fun convertJPushCustomMessage(code: String) {
        if (code == "${PushCode.PUSH_SHARE_MSG_TO_SHARER}" || code == "${PushCode.PUSH_SHARE_STATUS_TO_OWNER}" || code == "${PushCode.OWNER_REMOVE_SHARE_NOTIFY_SHARER}" || code == "${PushCode.SHARER_REMOVER_SHARE_NOTIFY_OWNER}") {

        } else if (code == "${PushCode.DEVICE_UPDATE_START}" || code == "${PushCode.DEVICE_UPDATE_SUCCESS}" || code == "${PushCode.DEVICE_UPDATE_FAILED}") {

        } else if (code == "${PushCode.USER_OTHER_PLACE_UPDAET}") {
            showForceLogoutDialog()
        } else if (code == "${PushCode.PUSH_ACTIVE}") {

        } else if (code == "${PushCode.PUSH_LOW_BATTERY}") {

        }
    }

    private fun showForceLogoutDialog() {
        SmartDialog.build(supportFragmentManager)
            .setTitle("被迫下线")
            .setContentText("你的账号在其他设备登录")
            .setPositiveTextName(resources.getString(R.string.confirm))
            .setOnPositive {
                var hashMap = HashMap<String,Any?>()
                hashMap["logout"] = ""
                YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
                startRouterActivityCheckLogin(ACTIVITY_PATH_MAIN) {
                    finishAllActivities()
                }
                it.dismiss()
            }
            .show()
        }
}