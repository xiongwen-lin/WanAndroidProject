/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.apemans.quickui.R
import com.apemans.quickui.databinding.LayoutLabelTextDetailIconBinding

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 10:56 上午
 * 说明:
 *
 * 备注:文本标签控件-支持详情文本显示
 *
 ***********************************************************/
class LabelTextDetailIconView(context : Context, attrs : AttributeSet) : BaseLabelTextView<LayoutLabelTextDetailIconBinding>(context, attrs) {

    override fun refreshViewOnChange(configure: LabelTextConfigure?) {
        super.refreshViewOnChange(configure)
        configure?.let {
            binding?.apply {
                tvLabelTextTitle.text = it.text
                tvLabelTextDetail.text = it.detailText
                tvLabelTextTitle.setTextColor(ContextCompat.getColor(context,it.textColor))
                tvLabelTextDetail.setTextColor(ContextCompat.getColor(context,it.detailTextColor))
                ivLabelTextRightIcon.setImageResource(it.rightIconRes)
                ivLabelTextRightIcon.visibility = it.rightIconVisibility
                ivLabelTextDividerLine.visibility = it.dividerLineVisible
                ivLabelTextDividerLine.visibility = it.dividerLineVisible
            }
        }
    }

    override fun init() {
        super.init()
        binding = LayoutLabelTextDetailIconBinding.inflate(LayoutInflater.from(context), this, true)
        uiState.value?.apply {
            rightIconRes = R.drawable.tools_right_arrow_gray
            rightIconVisibility = VISIBLE
        }
    }
}