package com.afar.osaio

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco
import com.afar.osaio.widget.BizBundleFamilyServiceImpl
import com.nooie.sdk.NooieNativeSDK
import com.nooie.sdk.device.bean.LogLevel
import com.nooie.sdk.device.bean.LogState
import com.apemans.quickui.label.GlobalLabelTextConfigure
import com.apemans.business.apisdk.ApiManager
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.logger.YRLogManager
import com.apemans.messagepush.push.MessagePushManager
import com.apemans.messagepush.push.base.MessagePushConstant
import com.apemans.router.startRouterActivity
import com.apemans.tuya.module.api.ACTIVITY_PATH_TUYA_DEVICE_DETAILS
import com.tuya.smart.android.common.utils.L
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk
import com.tuya.smart.wrapper.api.TuyaWrapper
import java.io.File

/**
 * @author Dylan Cai
 */
class App : com.apemans.yruibusiness.base.BaseComponentApplication(needLogin = true) {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initSmartIpcSdk()
        initYRLoggerSDK()
        SmartDatabase.init(this, "osaio")
        //updateNetConfigure()
        //TuyaHomeSdk.init(this)
        initTuya()
        initMessagePush()
        initThemeConfigure()

        //YRApiSdk的初始化必须放在中间件之前，因为中间件遍历类时会提前实例化使用YRApiSdk的类，导致闪退
        ApiManager.init("https://global.osaio.net/v2/")

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initTuya() {
        // 请不要修改初始化顺序
        Fresco.initialize(this)

        // SDK 初始化
        TuyaHomeSdk.setDebugMode(false)
        TuyaHomeSdk.init(this)

        // 业务包初始化
        TuyaWrapper.init(this, { errorCode, urlBuilder -> // 路由未实现回调
            // 点击无反应表示路由未现实，需要在此实现， urlBuilder.target 目标路由， urlBuilder.params 路由参数
            if (urlBuilder.params != null && urlBuilder.params.containsKey("extra_panel_dev_id")) {
                Log.e("", "right event devId " + urlBuilder.params.getString("extra_panel_dev_id"))
                val devId = urlBuilder.params.getString("extra_panel_dev_id")
                if (!TextUtils.isEmpty(devId)) {
                    val deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId)
                    if (deviceBean != null) {
                        //跳转
                        startRouterActivity(
                            ACTIVITY_PATH_TUYA_DEVICE_DETAILS,
                            "device_id" to deviceBean.devId,
                            "is_share" to deviceBean.getIsShare()
                        )
                    }
                }
            }
            Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString())
        }) { serviceName -> // 服务未实现回调
            Log.e("service not implement", serviceName)
        }
        TuyaOptimusSdk.init(this)
        TuyaWrapper.registerService(AbsBizBundleFamilyService::class.java, BizBundleFamilyServiceImpl())
        L.setLogSwitcher(true)
        TuyaHomeSdk.setLogInterception(
            Log.VERBOSE
        ) { i, s, s1 -> Log.e("", "Tuya i $i s $s s1 $s1") }
    }

    /**
     * 初始化messagePushComponent模块
     * 1、如果支持友盟推送，设置友盟推送相关配置
     */
    private fun initMessagePush() {
        /**
         * context
         * param 配置参数，如下
         * MessagePushConstant.PARAM_KEY_UM_APP_KEY - 友盟app key
         * MessagePushConstant.PARAM_KEY_UM_MESSAGE_SECRET - 友盟message secret
         * MessagePushConstant.PARAM_KEY_UM_LOG_ENABLE - 友盟日志调试打印，可选且默认关闭
         */
        val param = Bundle();
        param.putString(MessagePushConstant.PARAM_KEY_UM_APP_KEY, "61237f7fe0080c5063767777");
        param.putString(MessagePushConstant.PARAM_KEY_UM_MESSAGE_SECRET, "2a39e8b12b59ef13e5920a91a51e44ba");
        param.putBoolean(MessagePushConstant.PARAM_KEY_UM_LOG_ENABLE, true);
//        MessagePushManager.init(this, param)
    }

    /**
     * 初始化Logger
     * 1、调用YRApiManager.init()接口，初始化配置，具体配置参数如下。
     */
    private fun initYRLoggerSDK() {
        /**
         * 初始化日志设置
         * LoganConfig 配置参数
         * cachePath mmap缓存路径
         * logFilePath file文件路径
         * maxFileSize 删除文件最大值
         * day 删除天数
         * encryptKey 128位ase加密Key
         * encryptIv 128位aes加密IV
         * debug 是否为debug模式, true 表示使用调试模式，日志输出到控制台。false表示关闭调试模式，日志输出到文件中
         */
        var cachePath = getFilesDir().getAbsolutePath()
        var logFilePath =
            getExternalFilesDir(null)!!.getAbsolutePath() + File.separator + "yrlog_v1";
        var maxFileSize = 10 * 1024 * 1024L
        YRLogManager.init(true) {
            it.setCachePath(cachePath)
                .setPath(logFilePath)
                .setMaxFile(maxFileSize)
                .setDay(10)
                .setEncryptKey16("0123456789012345".encodeToByteArray())
                .setEncryptIV16("0123456789012345".encodeToByteArray())
        }
    }

    private fun initSmartIpcSdk() {
        NooieNativeSDK.getInstance(this).init(LogState.LOG_CONSOLE, getExternalFilesDir("")!!.getAbsolutePath() + File.separator + "Osaio");
        NooieNativeSDK.getInstance(this).setLogLevel(LogLevel.LOG_LEVEL_V.getIntValue());
    }

    private fun initThemeConfigure() {
        GlobalLabelTextConfigure.apply {
            textColor = R.color.theme_text_color
            selectionOnColor = R.color.theme_color
            selectionOffColor = R.color.theme_sub_text_color
        }
    }

    companion object {
        lateinit var instance: App
    }
}