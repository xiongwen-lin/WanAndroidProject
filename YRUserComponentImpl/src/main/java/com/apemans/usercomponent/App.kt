package com.apemans.usercomponent

import android.graphics.Color
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.*
import com.apemans.business.apisdk.middleservice.YR_MIDDLE_SERVICE_PARAM_BASE_URL
import com.apemans.yruibusiness.base.BaseComponentApplication
import com.apemans.logger.YRLog
import com.apemans.logger.YRLogManager
import java.io.File

/**
 * @author Dylan Cai
 */
class App : BaseComponentApplication() {

    override fun onCreate() {
        super.onCreate()
        com.apemans.yruibusiness.ui.toolbar.ToolbarConfig.apply {
            titleTextSize = 18f
            backgroundColor = Color.WHITE
        }

        instance = this
        initYRLoggerSDK()
        // 注册服务
        YRMiddleServiceManager.registerAllService(this, getExcludeRegisterPackageList())
        initYRApiSdk()
        updateNetConfigure()
//        initTuya()
    }

    private fun initTuya() {
        // 请不要修改初始化顺序
//        Fresco.initialize(this)
//
//        // SDK 初始化
//        TuyaHomeSdk.setDebugMode(true)
//        TuyaHomeSdk.init(this)
//
//        // 业务包初始化
//        TuyaWrapper.init(this, { errorCode, urlBuilder -> // 路由未实现回调
//            // 点击无反应表示路由未现实，需要在此实现， urlBuilder.target 目标路由， urlBuilder.params 路由参数
//            if (urlBuilder.params != null && urlBuilder.params.containsKey("extra_panel_dev_id")) {
//                Log.e("", "right event devId " + urlBuilder.params.getString("extra_panel_dev_id"))
//                val devId = urlBuilder.params.getString("extra_panel_dev_id")
//                if (!TextUtils.isEmpty(devId)) {
//                    val deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId)
//                    if (deviceBean != null) {
//                        //跳转
//                    }
//                }
//            }
//            Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString())
//        }) { serviceName -> // 服务未实现回调
//            Log.e("service not implement", serviceName)
//        }
//        TuyaOptimusSdk.init(this)
//        //TuyaWrapper.registerService(AbsBizBundleFamilyService::class.java, BizBundleFamilyServiceImpl())
//        L.setLogSwitcher(false)
//        TuyaHomeSdk.setLogInterception(
//            Log.VERBOSE
//        ) { i, s, s1 -> Log.e("","Tuya i $i s $s s1 $s1") }
    }

    private fun getExcludeRegisterPackageList() : List<String> {
        val list = mutableListOf<String>()
        list.add("anet.channel")
        list.add("com.bumptech")
        list.add("com.facebook")
        list.add("com.swmansion")
        list.add("com.tuya")
        list.add("org.apache")
        list.add("com.taobao")
        list.add("demo.")
        list.add("org.repackage")
        list.add("okhttp3.internal")
        return list
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

    /**
     * 初始化YRApiSdk
     * 1、调用YRApiManager.init()接口，初始化appId、appSecret、baseurl（获取全球服务器地址的baseUrl，如示例teckin的https://global.teckinhome.com/v2/）
     * 2、调用YRApiManager.registerGlobalObserver()接口，注册全局监听，用于观察接口请求异常通知，并针对不同的情况进行处理。
     * 3、确保数据共享库的uid、token、全球地址有效，接口请求和接口的签名需要用到
     * 4、对应请求接口请调用YRApi中开发的接口，如YRApi.getGlobalApi()代表和全球服务地址相关的接口
     * 5、分别获取全球时间、获取全球服务器地址、登陆成功后，同步相关信息到NetConfigure网络配置；具体示例如下面的updateNetConfigure方法
     */
    private fun initYRApiSdk() {

        var initParam = mutableMapOf<String, Any>()
        initParam[YR_MIDDLE_SERVICE_PARAM_BASE_URL] = "https://global.osaio.net/v2/"
        YRMiddleServiceManager.requestAsync("yrcx://yrbusiness/init", initParam) {
            YRLog.d { "-->> debug MainActivity testApi requestAsync init code ${it?.code} " }
        }
        var envParam = mutableMapOf<String, Any>()
        envParam[KEY_ENV_APP_ID] = "3dab98eee85b7ae8"
        envParam[KEY_ENV_APP_SECRET] = "7569f96ceac7c333e981a9604865e413"
        envParam[KEY_ENV_BASE_URL] = "https://global.osaio.net/v2/"
        envParam[KEY_ENV_SERVER_TIME] = System.currentTimeMillis() / 1000L
        envParam[KEY_ENV_WEB_URL] = "https://app.osaio.nooie.cn/v2/"
        envParam[KEY_ENV_S3_URL] = "https://app.osaio.nooie.cn/v3/cloud/"
        envParam[KEY_ENV_REGION] = "cn"

        val tokenUidParams = mutableMapOf<String, Any>()
        tokenUidParams[KEY_ENV_UID] = "b02343fa4bfe870e"
        tokenUidParams[KEY_ENV_API_TOKEN] = "eyJhbGciOiJoczI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NDYzNzgxNDcsInVpZCI6ImIwMjM0M2ZhNGJmZTg3MGUiLCJyZWciOiJjbiIsInNjaCI6IjNkYWI5OGVlZTg1YjdhZTgiLCJ0X2lkIjoib21nZndhZGx2dXdzbmd4bmNteHJlcWRpYmpjZWxweGwiLCJyZWZfdG9rIjoiYzE1ZmY5ODMtOTYwYS0xMWVjLThhMjctYTI3MzYwNjg1OTdiIiwicmVmX2V4cCI6MTY0Njk4Mjk0N30=.MzYwNjgxMzY0M2MzNDI5MGFhYmE2YjE2MTE5YWMyMmQzMzIwMGI4NjBjYTY3NTU0MDIwODk1NzkwNTZiNzM5MQ=="
        envParam[KEY_ENV_TOKEN_UID_PARAMS] = tokenUidParams

        val extensionHeaders = mutableMapOf<String, String>()
        extensionHeaders["User-Agent"] = "OSAIO_ANDROID_1.0.0"
        envParam[KEY_ENV_EXTENSION_HEADERS] = extensionHeaders

        envParam[KEY_ENV_GLOBAL_SHORT_URL] = listOf("global/time", "user/tuya", "account/country", "account/baseurl", "user/account")
        envParam[KEY_ENV_BEFORE_LOGIN_SHORT_URL] = listOf("global/time", "user/tuya", "account/country", "account/baseurl", "user/account", "login/login", "login/send", "register/send", "register/verify", "login/verify", "tuya/reset", "register/register")
        envParam[KEY_ENV_S3_SHORT_URL] = listOf("feedbackput_presignurl", "photoget_presignurl", "app/fetch/file_event")

        val responseCodeMap = mutableMapOf<String, List<String>>()
        responseCodeMap[RESPONSE_CODE_UPDATE_SERVER_TIME] = listOf(HttpCode.CODE_1004.toString())
        responseCodeMap[RESPONSE_CODE_LOGIN_EXPIRE] = listOf(HttpCode.CODE_1006.toString(), HttpCode.CODE_1059.toString())
        responseCodeMap[RESPONSE_CODE_LOGIN_BY_OTHER] = listOf(HttpCode.CODE_1056.toString())
        responseCodeMap[RESPONSE_CODE_ACCOUNT_MOVED] = listOf(HttpCode.CODE_1909.toString())

        envParam[KEY_ENV_RESPONSE_ERROR_CODE] = responseCodeMap

        YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters", envParam)
        YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",envParam)

        /*
        //初始化配置
        ApiManager.init(this, "3dab98eee85b7ae8", "7569f96ceac7c333e981a9604865e413", "https://global.osaio.net/v2/")
        //公共拓展header参数
        val extensionHeaders = mutableMapOf<String, String>()
        extensionHeaders["User-Agent"] = "OSAIO_ANDROID_1.0.0"
        val responseCodeMap = mutableMapOf<String, List<String>>()
        responseCodeMap[RESPONSE_CODE_UPDATE_SERVER_TIME] = listOf(HttpCode.CODE_1004.toString())
        responseCodeMap[RESPONSE_CODE_LOGIN_EXPIRE] = listOf(HttpCode.CODE_1006.toString(), HttpCode.CODE_1059.toString())
        responseCodeMap[RESPONSE_CODE_LOGIN_BY_OTHER] = listOf(HttpCode.CODE_1056.toString())
        responseCodeMap[RESPONSE_CODE_ACCOUNT_MOVED] = listOf(HttpCode.CODE_1909.toString())
        ApiManager.updateExtensionHeaders(extensionHeaders)
            .updateResponseCodeMap(responseCodeMap)

        ApiManager.registerGlobalObserver(object : IApiGlobalObserver {
            override fun onActionResult(code: Int, data: Bundle?) {
                when (code) {
                    YRApiActionCode.ACTION_CODE_UPDATE_SERVER_TIME -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    YRApiActionCode.ACTION_CODE_LOGIN_EXPIRE -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    YRApiActionCode.ACTION_CODE_LOGIN_BY_OTHER -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                    YRApiActionCode.ACTION_CODE_ACCOUNT_MOVED -> {
                        println("-->> debug App onActionResult code=$code")
                    }
                }
            }
        })

         */

    }

    /**
     * 分别获取全球时间、获取全球服务器地址、登陆成功后，同步相关信息到NetConfigure网络配置
     * 下面三处信息同步请根据具体流程进行调用
     */
    private fun updateNetConfigure() {
        //同步全球时间到NetConfigure网络配置
//        YRApiManager.updateNetConfigureGapTime(0)
//
//        //同步全球服务器地址到NetConfigure网络配置
//        YRApiManager
//            .updateNetConfigureWebUrl("http://161.189.2.210:8084/v2/")
//            .updateNetConfigureS3Url("http://161.189.2.210:8084/v3/cloud/")
//
//        //同步登陆信息到NetConfigure网络配置
//        YRApiManager
//            .updateNetConfigureUid("b02343fa4bfe870e")
//            .updateNetConfigureToken("df2acb1e2fc7b78bf588f3bfcb5b4bb0")
    }

    companion object {
        lateinit var instance : App
    }
}