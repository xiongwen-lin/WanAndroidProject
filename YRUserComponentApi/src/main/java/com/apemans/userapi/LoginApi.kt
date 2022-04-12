package com.apemans.userapi

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.apemans.router.startRouterActivity

/***********************************************************
 * @Author : Dylan Cai
 * @Date   : 2021/8/16
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
const val KEY_ROUTER_PATH = "router_path"
internal const val KEY_CHECK_LOGIN = "router_check_login"
internal var loginActivityPath: String? = null


@JvmOverloads
@JvmName("startActivityCheckLogin")
fun Context.startRouterActivityCheckLogin(
    path: String,
    vararg pairs: Pair<String, Any?>,
    extras: Bundle = Bundle(),
    onArrival: ((Postcard) -> Unit)? = null
) {
    extras.putBoolean(KEY_CHECK_LOGIN, true)
    startRouterActivity(path, *pairs, extras = extras, callback = com.apemans.userapi.LoginNavCallback(this, onArrival))
}

fun executeCheckLogin(action: () -> Unit) =
    if (loginActivityPath != null && LoginInterceptor.checkLogin?.invoke() == false) {
        startRouterActivity(loginActivityPath!!)
        loginObserver = action
    } else {
        action.invoke()
    }

@JvmName("enableLoginInterceptor")
fun enableRouterLoginInterceptor(loginPath: String, onCheckLogin: () -> Boolean) {
    loginActivityPath = loginPath
    LoginInterceptor.checkLogin = onCheckLogin
}