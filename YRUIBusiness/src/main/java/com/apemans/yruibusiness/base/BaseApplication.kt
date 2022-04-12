package com.apemans.yruibusiness.base

import android.app.Application
import com.apemans.yruibusiness.ui.loadingstate.*
import com.tencent.mmkv.MMKV

/**
 * @author Dylan Cai
 */
abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LoadingHelper.setDefaultAdapterPool {
            register(ViewType.LOADING, LoadingStateAdapter())
            register(ViewType.ERROR, ErrorStateAdapter())
            register(ViewType.EMPTY, EmptyStateAdapter())
        }
        MMKV.initialize(this)
    }
}