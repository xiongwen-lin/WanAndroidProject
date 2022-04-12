package com.apemans.base.utils

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = 0,
    exitResId: Int = 0
) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}


inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = 0,
    exitResId: Int = 0,
    intentBody: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.intentBody()
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    let { Toast.makeText(it, text, duration).show() }

fun Context.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    let { Toast.makeText(it, textId, duration).show() }


val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)


fun Context.inflateLayout(
    @LayoutRes layoutId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) = inflater.inflate(layoutId, parent, attachToRoot)

/**
 * 屏幕宽度(px)
 */
val Context.screenWidthPx: Int get() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度(px)
 * 调用 context?.screenWidthPx
 */
val Context.screenHeightPx: Int get() = resources.displayMetrics.heightPixels
val Application.screenHeightPx: Int get() = resources.displayMetrics.heightPixels

/**
 * 屏幕的密度
 */
inline val Context.density: Float get() = resources.displayMetrics.density

/**
 * Font
 *
 */
inline val Context.scaledDensity: Float get() = resources.displayMetrics.scaledDensity

/**
 * dp 转为 px
 */
fun Context.dp2px(value: Int): Int = (density * value).toInt()

fun Context.dp2pxs(value: Int): Int = (screenHeightPx * value).toInt()

/**
 * dp 转为 px
 */
fun Context.dp2px(value: Float): Int = (density * value).toInt()


fun Context.sp2px(value: Float): Int = (scaledDensity * value + 0.5f).toInt()

/**
 * px 转为 dp
 */
fun Context.px2dp(value: Int): Float = value.toFloat() / density

/*****获取资源***/
fun Context.getCompatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getCompatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)

/******获取各种系统Manager*****/
val Context.inputManager: InputMethodManager?
    get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

val Context.notificationManager: NotificationManager?
    get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager

/*************是否开启通知*************/
fun Context.isNotificationOpen(): Boolean {
    return try {
        NotificationManagerCompat.from(this).areNotificationsEnabled()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}