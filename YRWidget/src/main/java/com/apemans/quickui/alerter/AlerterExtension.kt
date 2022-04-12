package com.apemans.quickui.alerter

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.apemans.quickui.R
import com.tapadoo.alerter.Alerter

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/17 6:50 下午
 * 说明:
 *
 * 备注:统一下弹窗拓展方法
 *
 ***********************************************************/

fun Activity.alertInfo(block: () -> String) {
    buildAlerter(this, text = block.invoke()).show()
}

fun Activity.alertInfo(text: String) {
    buildAlerter(this, text = text).show()
}

fun Activity.alertInfo(title: String, msg: String) {
    buildAlerter(this, title, msg).show()
}

fun Activity.alertInfo(title: String, block: () -> String) {
    buildAlerter(this, title, block.invoke()).show()
}

private fun buildAlerter(activity: Activity, title: String? = null, text: String) : Alerter {

    try {
        return Alerter.create(activity, R.layout.layout_normal_alerter)
            .also {
                var alerterTitle: TextView? = it.getLayoutContainer()?.findViewById(R.id.alerterTitle)
                var alerterText: TextView? = it.getLayoutContainer()?.findViewById(R.id.alerterText)

                if (!title.isNullOrEmpty()) {
                    alerterTitle?.visibility = View.VISIBLE
                    alerterTitle?.text = title
                }
                alerterText?.text = text
            }
            .setBackgroundResource(R.color.transparent)
            .setDuration(1000)
            .hideIcon()
    } catch (e: Exception) {
    }

    return Alerter.create(activity)
        .setText(text)
        .setBackgroundResource(R.drawable.frame_alert_bg)
        .setDuration(1000)
        .hideIcon()
}