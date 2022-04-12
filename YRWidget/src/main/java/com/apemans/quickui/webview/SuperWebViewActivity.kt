/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.apemans.quickui.R
import com.apemans.quickui.SuperToolbar
import com.apemans.quickui.darkMode
import com.apemans.quickui.immersive
import java.io.Serializable

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/7 17:26
 * 说明: 应用内打开Web Http url
 *
 * 备注:
 *
 ***********************************************************/
class SuperWebViewActivity : AppCompatActivity() {
    private lateinit var superToolbar: SuperToolbar
    private lateinit var superWebView: WebView

    companion object {

        /**
         * @param packageContext from
         * @param url 打开地址
         * @param additionalHttpHeaders uri 请求头
         * @param title 标题
         * @param titleColor 标题颜色
         * @param statusBarColor 状态栏主题颜色
         * @param webviewBackgroundColor WebView主题颜色
         * @param navIcon 返回按钮
         * @param darkMode 暗黑导航栏
         */
        @JvmStatic
        fun openSuperWebViewActivity(
            packageContext: Context,
            url: String,
            additionalHttpHeaders: Map<String, String>?,
            title: String,
            @ColorInt titleColor: Int,
            @ColorInt statusBarColor: Int,
            @ColorInt webviewBackgroundColor: Int,
            @DrawableRes navIcon: Int,
            darkMode: Boolean = true
        ) {
            val intent = Intent(packageContext, SuperWebViewActivity::class.java)
            val bundle = Bundle()
            bundle.putString("url", url)
            if (additionalHttpHeaders != null) {
                bundle.putSerializable("additionalHttpHeaders", additionalHttpHeaders as Serializable)
            }
            bundle.putString("title", title)
            bundle.putInt("titleColor", titleColor)
            bundle.putInt("statusBarColor", statusBarColor)
            bundle.putInt("webviewBackgroundColor", webviewBackgroundColor)
            bundle.putInt("nav_return_icon", navIcon)
            bundle.putBoolean("darkMode", darkMode)
            intent.putExtras(bundle)
            packageContext.startActivity(intent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        superToolbar = findViewById(R.id.superToolbar)
        superWebView = findViewById(R.id.webView)
        val bundle = intent.extras
        bundle?.let {
            val url = it.getString("url")
            val title = it.getString("title")
            val titleColor = it.getInt("titleColor")
            val statusBarColor = it.getInt("statusBarColor")
            val webviewBackgroundColor = it.getInt("webviewBackgroundColor")
            val navIcon = it.getInt("nav_return_icon")
            val darkMode = it.getBoolean("darkMode")

            var additionalHttpHeaders: Map<String, String>? = null
            if (it.containsKey("additionalHttpHeaders")) {
                additionalHttpHeaders = it.getSerializable("additionalHttpHeaders") as Map<String, String>
            }

            immersive(statusBarColor)
            darkMode(darkMode)

            superToolbar.setBackgroundColor(statusBarColor)
            superWebView.setBackgroundColor(webviewBackgroundColor)
            //superWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
            title?.let { superToolbar.setTitle(title) }
            superToolbar.setTitleColor(titleColor)
            superToolbar.setNavReturnIcon(navIcon)

            superWebView.settings.javaScriptEnabled = true
            superWebView.settings.allowContentAccess = true
            superWebView.settings.allowFileAccess = true
            superWebView.settings.setAllowFileAccessFromFileURLs(true)
            superWebView.settings.setAppCacheEnabled(true)
            superWebView.settings.loadWithOverviewMode = true
            superWebView.settings.useWideViewPort = true
            superWebView.settings.setPluginState(WebSettings.PluginState.ON)
            superWebView.settings.domStorageEnabled = true
            if (url != null && additionalHttpHeaders != null) {
                superWebView.loadUrl(url, additionalHttpHeaders)
            }
            if (url != null && additionalHttpHeaders == null) {
                superWebView.loadUrl(url)
            }
            superWebView.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    //hideLoading()
                    // TODO: 2021/9/7 全局配置loading 和  
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    //showLoading()
                }
            })
        }
        superToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (superWebView.canGoBack()) {
            superWebView.goBack()
        } else {
            finish()
        }
    }
}