package com.afar.osaio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.business.apisdk.client.define.*
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.userapi.startRouterActivityCheckLogin
import com.apemans.yrcxsdk.data.YRCXSDKDataManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // 注册服务
        YRMiddleServiceManager.registerAllService(application, getExcludeRegisterPackageList())
        YRCXSDKDataManager.initDataListener()
        initYRApiSdk()
        startRouterActivityCheckLogin(ACTIVITY_PATH_MAIN) {
            finish()
        }
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
     * 初始化YRApiSdk
     * 1、调用YRApiManager.init()接口，初始化appId、appSecret、baseurl（获取全球服务器地址的baseUrl，如示例teckin的https://global.teckinhome.com/v2/）
     * 2、调用YRApiManager.registerGlobalObserver()接口，注册全局监听，用于观察接口请求异常通知，并针对不同的情况进行处理。
     * 3、确保数据共享库的uid、token、全球地址有效，接口请求和接口的签名需要用到
     * 4、对应请求接口请调用YRApi中开发的接口，如YRApi.getGlobalApi()代表和全球服务地址相关的接口
     * 5、分别获取全球时间、获取全球服务器地址、登陆成功后，同步相关信息到NetConfigure网络配置；具体示例如下面的updateNetConfigure方法
     */
    private fun initYRApiSdk() {

        /*
        中间件初始化暂时停用
        var initParam = mutableMapOf<String, Any>()
        initParam[YR_MIDDLE_SERVICE_PARAM_BASE_URL] = "https://global.osaio.net/v2/"
        YRMiddleServiceManager.requestAsync("yrcx://yrbusiness/init", initParam) {
            YRLog.d { "-->> debug MainActivity testApi requestAsync init code ${it?.code} " }
        }

         */
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

}