/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.apemans.quickui.R
import com.apemans.quickui.databinding.LayoutLabelTextInfoIconBinding

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 10:56 上午
 * 说明:
 *
 * 备注:文本标签控件-支持副文本
 *
 ***********************************************************/
class LabelTextInfoIconView(context : Context, attrs : AttributeSet) : BaseLabelTextView<LayoutLabelTextInfoIconBinding>(context, attrs) {

    override fun refreshViewOnChange(configure: LabelTextConfigure?) {
        super.refreshViewOnChange(configure)
        configure?.let {
            binding?.apply {
                tvLabelTextTitle.text = it.text
                tvLabelTextInfo.text = it.subText
                tvLabelTextTitle.setTextColor(ContextCompat.getColor(context,it.textColor))
                tvLabelTextInfo.setTextColor(ContextCompat.getColor(context,it.subTextColor))
                ivLabelTextRightIcon.setImageResource(it.rightIconRes)
                ivLabelTextRightIcon.visibility = it.rightIconVisibility
                ivLabelTextDividerLine.visibility = it.dividerLineVisible
            }
        }
    }

    override fun init() {
        super.init()
        binding = LayoutLabelTextInfoIconBinding.inflate(LayoutInflater.from(context), this, true)
        uiState.value?.apply {
            rightIconRes = R.drawable.tools_right_arrow_gray
            rightIconVisibility = GONE
        }
    }
}