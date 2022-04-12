package com.apemans.yruibusiness.utils

import android.content.Context

abstract class LoadingFactory {
    abstract fun showLoading(context: Context, message: String = "")
    abstract fun showMessage(message: String, delay: Int = 2)
    abstract fun dismissLoading(delay: Int = 0)
}