/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui.preference

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.apemans.quickui.R
import com.apemans.quickui.recyclerview.SmartLinearLayoutManager

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/6 11:31
 * 说明:
 *
 * 备注:
 * https://stackoverflow.com/questions/30531091/how-to-disable-recyclerview-scrolling
 ***********************************************************/
class PreferenceRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var recyclerView: RecyclerView
    private var settingTypeAdapter: CommonPreferenceSettingAdapter
    private val smartLinearLayoutManager: SmartLinearLayoutManager

    //数据源
    private var dataList: MutableList<PreferenceBean>? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_preference_recyclerview, this, true)
        recyclerView = rootView.findViewById(R.id.recyclerview)
        settingTypeAdapter = CommonPreferenceSettingAdapter()
        smartLinearLayoutManager = SmartLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        smartLinearLayoutManager.setCanScrollVertical(false)
        //recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = SmartLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //屏蔽默认刷新动画
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = settingTypeAdapter
    }

    fun bindSourceData(dataList: MutableList<PreferenceBean>) {
        this.dataList = dataList
        settingTypeAdapter.bindData(this.dataList!!)
    }

    fun addPreferenceSettingEventCallback(callback: PreferenceSettingEventCallback) {
        settingTypeAdapter.bindViewEvent(callback)
    }

    fun setCanScrollVertical(can: Boolean) {
        smartLinearLayoutManager.setCanScrollVertical(can)
    }

    fun setItemBackGround(@DrawableRes drawable:Int){

    }

    fun notifyItemChanged(position: Int) {
        settingTypeAdapter.notifyItemChanged(position)
    }

    fun notifyItemInserted(position: Int) {
        settingTypeAdapter.notifyItemInserted(position)
    }

    fun notifyItemRemoved(position: Int) {
        settingTypeAdapter.notifyItemRemoved(position)
    }


    fun notifyDataSetChanged() {
        settingTypeAdapter.notifyDataSetChanged()
    }

}