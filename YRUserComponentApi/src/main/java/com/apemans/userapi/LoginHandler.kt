@file:JvmName("LoginHandler")

package com.apemans.userapi

import android.app.Activity
import com.apemans.router.startRouterActivity

/**
 * @author Dylan Cai
 */
internal var loginObserver: (() -> Unit)? = null

@JvmName("post")
fun Activity.loginSuccess() {
    val path = intent.getStringExtra(KEY_ROUTER_PATH)
    if (path != null) {
        startRouterActivity(path, extras = intent.extras) { finish() }
    } else {
        finish()
    }
    loginObserver?.invoke()
    loginObserver = null
}