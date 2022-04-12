/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.yruibusiness.base

import com.apemans.yruibusiness.utils.LoadingBuilder
import com.apemans.yruibusiness.utils.LoadingDialogFactory
import com.apemans.yruibusiness.utils.awesomefont.FontViewPumpHelper
import com.apemans.router.initARouter
import com.apemans.userapi.enableRouterLoginInterceptor
import com.apemans.userapi.paths.ACTIVITY_PATH_FIRST_START
import com.apemans.userapi.paths.ACTIVITY_PATH_LOGIN
import com.apemans.yrcxsdk.data.YRCXSDKDataManager

/**
 * 组件的 Application，在 [BaseApplication] 的基础上增加 ARouter 的配置
 * 构造方法可以配置是否需要登录，如果传 true，则开启登录拦截
 *
 * @author Dylan Cai
 */
abstract class BaseComponentApplication(private val needLogin: Boolean = false) : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化ARouter
        initARouter(this)

        //注册统一Loading dialog
        LoadingBuilder.registerLoadingFactory(LoadingDialogFactory())

        //全局字体库设置
        FontViewPumpHelper.configViewPump("fonts/PingFang_Regular.ttf")

        //登录拦截
        if (needLogin) {
            if (YRCXSDKDataManager.firstLogin) {
                enableRouterLoginInterceptor(ACTIVITY_PATH_FIRST_START) { !YRCXSDKDataManager.firstLogin }
            } else {
                enableRouterLoginInterceptor(ACTIVITY_PATH_LOGIN) { "" != YRCXSDKDataManager.userAccount }
            }
        }

    }
}