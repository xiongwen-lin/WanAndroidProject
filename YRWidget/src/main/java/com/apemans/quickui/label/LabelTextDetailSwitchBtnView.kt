/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.apemans.quickui.databinding.LayoutLabelTextDetailSwitchBtnBinding

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 10:56 上午
 * 说明:
 *
 * 备注:文本标签控件-支持详情文本和开关
 *
 ***********************************************************/
class LabelTextDetailSwitchBtnView(context : Context, attrs : AttributeSet) : BaseLabelTextView<LayoutLabelTextDetailSwitchBtnBinding>(context, attrs) {

    var mListener : ((Boolean) -> Unit)? = null

    override fun refreshViewOnChange(configure: LabelTextConfigure?) {
        super.refreshViewOnChange(configure)
        configure?.let {
            binding?.apply {
                tvLabelTextTitle.text = it.text
                tvLabelTextDetail.text = it.detailText
                tvLabelTextTitle.setTextColor(ContextCompat.getColor(context,it.textColor))
                tvLabelTextDetail.setTextColor(ContextCompat.getColor(context,it.detailTextColor))
                if (sbLabelText.isChecked != it.switchOn) {
                    sbLabelText.toggleNoCallback()
                }
                ivLabelTextDividerLine.visibility = it.dividerLineVisible
            }
        }
    }

    override fun init() {
        super.init()
        binding = LayoutLabelTextDetailSwitchBtnBinding.inflate(LayoutInflater.from(context), this, true)
        uiState.value?.apply {
            switchOn = false
        }
        binding?.sbLabelText?.setOnCheckedChangeListener { view, isChecked ->
            mListener?.invoke(isChecked)
        }
    }
}