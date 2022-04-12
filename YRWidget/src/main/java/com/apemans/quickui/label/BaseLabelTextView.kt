/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 10:56 上午
 * 说明:
 *
 * 备注:文件标签控件基类
 *
 ***********************************************************/
open class BaseLabelTextView<T>(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    var binding : T? = null
    /*
    var uiState = MutableLiveData(LabelTextConfigure().apply {
        textColor = R.color.theme_text_color
    })
     */
    var uiState = MutableLiveData(LabelTextConfigure())

    init {
        init()
    }

    /**
     * 控件初始化，重写该方法以初始化控件状态
     */
    open fun init() {
    }

    /**
     * 在获取实例后，立即调用，否则更新uiState时，页面无法更新
     */
    fun setupConfigure(owner : LifecycleOwner) {
        uiState.observe(owner, {
            refreshViewOnChange(it)
        })
    }

    /**
     * 当控件的LabelTextConfigure配置参数发生变化时，会对调该方法，重写该方法并实现相关控件状态刷新
     */
    open fun refreshViewOnChange(configure: LabelTextConfigure?) {
    }

    /**
     * 更新控件的LabelTextConfigure配置参数
     */
    fun updateUiState(block : (value : LabelTextConfigure?) -> LabelTextConfigure?) {
        uiState.value = block.invoke(uiState.value)
    }

    /**
     * 更新控件的LabelTextConfigure配置参数
     */
    fun update(block : LabelTextConfigure.() -> Unit) {
        uiState.value = uiState.value!!.apply(block)
    }

}