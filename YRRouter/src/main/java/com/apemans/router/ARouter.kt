@file:JvmName("ARouterUtils")
@file:Suppress("unused")

package com.apemans.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Dylan Cai
 */

@JvmName("init")
fun Application.initARouter(application: Application) {
    val isDebug = application.packageManager.getApplicationInfo(packageName, 0).flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    if (isDebug) {
        ARouter.openLog()
        ARouter.openDebug()
    }
    ARouter.init(this)
}

fun <T : IProvider> findRouterService(clazz: Class<T>): T? =
    ARouter.getInstance().navigation(clazz)

@Suppress("UNCHECKED_CAST")
fun <T : IProvider> findRouterService(path: String): T? =
    ARouter.getInstance().build(path).navigation() as T?

inline fun <reified T : IProvider> routerServices() =
    lazy {
        try {
            findRouterService(T::class.java)!!
        } catch (e: Exception) {
            val canonicalName = T::class.java.canonicalName
            throw IllegalArgumentException("There's no router service matched. Interface is $canonicalName")
        }
    }

inline fun <reified T : IProvider> routerServices(path: String) =
    lazy {
        try {
            findRouterService<T>(path)!!
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("There's no router service matched. path = [$path]")
        }
    }

@JvmOverloads
fun findRouterFragment(path: String, vararg pairs: Pair<String, Any?>, arguments: Bundle? = null): Fragment? =
    ARouter.getInstance().build(path).with(arguments).with(*pairs).navigation() as Fragment?

fun routerFragments(path: String, block: Postcard.() -> Unit = {}) =
    lazy {
        try {
            ARouter.getInstance().build(path).apply(block).navigation() as Fragment
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("There's no router fragment matched. path = [$path]")
        }
    }

@JvmName("startActivity")
fun startRouterActivity(path: String, vararg pairs: Pair<String, Any?>, extras: Bundle? = null) {
    ARouter.getInstance().build(path).with(extras).with(*pairs).navigation()
}

@JvmOverloads
@JvmName("startActivity")
fun Context.startRouterActivity(path: String, vararg pairs: Pair<String, Any?>, extras: Bundle? = null, callback: NavigationCallback? = null) {
    ARouter.getInstance().build(path).with(extras).with(*pairs).navigation(this, callback)
}

@JvmName("startActivity")
fun Context.startRouterActivity(path: String, vararg pairs: Pair<String, Any?>, extras: Bundle? = null, onArrival: (Postcard) -> Unit) =
    startRouterActivity(path, *pairs, extras = extras, callback = NavCallback(onArrival = onArrival))

@JvmOverloads
@JvmName("startActivity")
fun Fragment.startRouterActivity(path: String, vararg pairs: Pair<String, Any?>, extras: Bundle? = null, callback: NavigationCallback? = null) =
    requireContext().startRouterActivity(path, *pairs, extras = extras, callback = callback)

@JvmName("startActivity")
fun Fragment.startRouterActivity(path: String, vararg pairs: Pair<String, Any?>, extras: Bundle? = null, onArrival: (Postcard) -> Unit) =
    requireContext().startRouterActivity(path, *pairs, extras = extras, onArrival = onArrival)

@JvmOverloads
@JvmName("startActivityForResult")
fun Activity.startRouterActivityForResult(path: String, requestCode: Int, vararg pairs: Pair<String, Any?>, extras: Bundle? = null) {
    ARouter.getInstance().build(path).with(extras).with(*pairs).navigation(this, requestCode)
}

//@JvmOverloads
//@JvmName("startActivityForResult")
//fun Fragment.startRouterActivityForResult(path: String, requestCode: Int, vararg pairs: Pair<String, Any?>, extras: Bundle? = null) =
//    requireActivity().startRouterActivityForResult(path, requestCode, *pairs, extras = extras)

@JvmOverloads
@JvmName("startActivityForResult")
fun Fragment.startRouterActivityForResult(path: String, requestCode: Int, vararg pairs: Pair<String, Any?>, extras: Bundle? = null) {
    val postcard = ARouter.getInstance().build(path).with(extras).with(*pairs)
    LogisticsCenter.completion(postcard)
    startActivityForResult(Intent(requireContext(), postcard.destination).putExtras(postcard.extras), requestCode)
}
