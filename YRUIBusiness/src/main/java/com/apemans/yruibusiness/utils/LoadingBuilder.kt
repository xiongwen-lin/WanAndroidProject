package com.apemans.yruibusiness.utils

import android.content.Context

/**
 * Loading Builder 通过实现LoadingFactory定制你的Loading Dialog
 */
object LoadingBuilder {
    lateinit var loadingFactory: LoadingFactory
        private set

    fun registerLoadingFactory(loadingFactory: LoadingFactory): LoadingBuilder {
        LoadingBuilder.loadingFactory = loadingFactory
        return this
    }

    fun Context.showLoading(message: String = "") {
        loadingFactory.showLoading(this, message)
    }

    fun Context.showMessage(message: String, delay: Int = 0) {
        loadingFactory.showMessage(message, delay)
    }

    fun dismissLoading(delay: Int = 0) {
        loadingFactory.dismissLoading(delay)
    }

}