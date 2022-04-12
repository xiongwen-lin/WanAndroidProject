package com.apemans.usercomponent.user.widget

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.apemans.usercomponent.R
import com.dylanc.longan.appVersionCode
import com.dylanc.longan.appVersionName
import com.dylanc.longan.application
import com.github.lzyzsd.jsbridge.BridgeWebView
import java.lang.Exception

class WebViewDialogView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    var vWebViewDialogOutsider: View? = null
    var vWebViewDialogContentContainer: View? = null
    var wvWebViewDialogLoader: BridgeWebView? = null
    var btnCancel: Button? = null
    var btnConfirm: Button? = null
    private var mListener: WebViewDialogListener? = null
    private val HTTP_HEADER_USER_AGENT_PREFIX = "OSAIO_ANDROID_"
    val layoutId: Int
        get() = R.layout.layout_webview_dialog

    fun loadContent(url: String?) {
        if (wvWebViewDialogLoader == null) {
            return
        }
        if (url != null) {
            wvWebViewDialogLoader?.loadUrl(url)
        }
    }

    fun setCacheEnable(enable: Boolean) {
        if (wvWebViewDialogLoader == null) {
            return
        }
        wvWebViewDialogLoader?.settings?.setAppCacheEnabled(enable)
        if (!enable) {
            wvWebViewDialogLoader?.settings?.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    fun setListener(listener: WebViewDialogListener?) {
        mListener = listener
    }

    fun release() {
        if (wvWebViewDialogLoader != null) {
//            wvWebViewDialogLoader?.setWebViewClient(null)
            wvWebViewDialogLoader?.webChromeClient = null
        }
        setListener(null)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(layoutId, this, false)
        addView(view)
        vWebViewDialogOutsider = view.findViewById(R.id.vWebViewDialogOutsider)
        vWebViewDialogContentContainer = view.findViewById(R.id.vWebViewDialogContentContainer)
        wvWebViewDialogLoader = view.findViewById(R.id.wvWebViewDialogLoader)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        setWebViewSetting(wvWebViewDialogLoader)
//        setupWebViewClient(wvWebViewDialogLoader)
        setOnViewClick()
    }

    private fun setWebViewSetting(webView: WebView?) {
        if (webView == null) {
            return
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = createUserAgent()
        webView.setBackgroundColor(ContextCompat.getColor(application, R.color.transparent))
        setCacheEnable(false)
    }

    private fun setupWebViewClient(webView: WebView?) {
        /*if (webView == null) {
            return
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return super.shouldOverrideUrlLoading(webView, url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (mListener != null) {
                    mListener!!.onPageFinished()
                }
                //changeBackground(view, url);
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                if (mListener != null) {
                    mListener!!.onPageStarted()
                }
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                YRLog.d("-->> WebViewDialogView onReceivedTitle title=$title")
            }
        }*/
    }

    private fun setOnViewClick() {
        if (vWebViewDialogOutsider == null || btnConfirm == null || btnCancel == null) {
            return
        }
        vWebViewDialogOutsider!!.setOnClickListener {
            if (mListener != null) {
                mListener!!.onOutSideClick()
            }
        }
        btnConfirm?.setOnClickListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onConfirm()
            }
        })
        btnCancel?.setOnClickListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onCancel()
            }
        })
    }

    private fun changeBackground(webView: WebView, url: String) {
        try {
            if (url.isEmpty()) {
                return
            }
            if (url.contains("privacy-policy")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(
                        "document.querySelector('.box').style.backgroundColor = '#ffffff';"
                    ) { }
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun createUserAgent(): String? {
        return if (TextUtils.isEmpty(HTTP_HEADER_USER_AGENT_PREFIX)) {
            ""
        } else HTTP_HEADER_USER_AGENT_PREFIX + appVersionName/*BuildConfig.VERSION_NAME*/ + "_" + appVersionCode/*BuildConfig.VERSION_CODE*/
    }

    interface WebViewDialogListener {
        fun onCancel()
        fun onConfirm()
        fun onOutSideClick()
        fun onPageStarted()
        fun onPageFinished()
    }

    init {
        init(context)
    }
}