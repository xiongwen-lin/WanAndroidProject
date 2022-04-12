/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.yruibusiness.base

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/18 15:02
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

abstract class BaseComponentActivity<VB : ViewBinding> : BaseActivity<VB>() {
    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(newBase)
        //Global font attach Wrap the Activity Context:
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    fun registerOnViewClick(vararg views: View, block: (Int?) -> Unit) {
        views.forEach {
            it.setOnClickListener { view ->
                block(view?.id)
            }
        }
    }

    fun unregisterOnViewClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(null)
        }
    }

}