/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.apemans.quickui.databinding.LayoutLabelTextDetailSelectionBinding

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 10:56 上午
 * 说明:
 *
 * 备注:文本标签控件-支持详情文本和状态选中
 *
 ***********************************************************/
class LabelTextDetailSelectionView(context : Context, attrs : AttributeSet) : BaseLabelTextView<LayoutLabelTextDetailSelectionBinding>(context, attrs) {

    var mListener : ((Int) -> Unit)? = null

    override fun refreshViewOnChange(configure: LabelTextConfigure?) {
        super.refreshViewOnChange(configure)
        configure?.let {
            binding?.apply {
                tvLabelTextTitle.text = it.text
                tvLabelTextDetail.text = it.detailText
                tvLabelTextTitle.setTextColor(ContextCompat.getColor(context,it.textColor))
                tvLabelTextDetail.setTextColor(ContextCompat.getColor(context,it.detailTextColor))
                tvLabelTextSelection1.text = it.selection1
                tvLabelTextSelection2.text = it.selection2
                tvLabelTextSelection3.text = it.selection3
                tvLabelTextSelection4.text = it.selection4
                tvLabelTextSelection1.visibility = if (it.selection1.isNullOrEmpty()) GONE else VISIBLE
                tvLabelTextSelection2.visibility = if (it.selection2.isNullOrEmpty()) GONE else VISIBLE
                tvLabelTextSelection3.visibility = if (it.selection3.isNullOrEmpty()) GONE else VISIBLE
                tvLabelTextSelection4.visibility = if (it.selection4.isNullOrEmpty()) GONE else VISIBLE
                tvLabelTextSelection1.setTextColor(ContextCompat.getColor(context,it.selectionOffColor))
                tvLabelTextSelection2.setTextColor(ContextCompat.getColor(context,it.selectionOffColor))
                tvLabelTextSelection3.setTextColor(ContextCompat.getColor(context,it.selectionOffColor))
                tvLabelTextSelection4.setTextColor(ContextCompat.getColor(context,it.selectionOffColor))
                when(it.selectionIndex) {
                    LABEL_SELECTION_1 -> tvLabelTextSelection1.setTextColor(ContextCompat.getColor(context,it.selectionOnColor))
                    LABEL_SELECTION_2 -> tvLabelTextSelection2.setTextColor(ContextCompat.getColor(context,it.selectionOnColor))
                    LABEL_SELECTION_3 -> tvLabelTextSelection2.setTextColor(ContextCompat.getColor(context,it.selectionOnColor))
                    LABEL_SELECTION_4 -> tvLabelTextSelection2.setTextColor(ContextCompat.getColor(context,it.selectionOnColor))
                }
                ivLabelTextDividerLine.visibility = it.dividerLineVisible
            }
        }
    }

    override fun init() {
        super.init()
        binding = LayoutLabelTextDetailSelectionBinding.inflate(LayoutInflater.from(context), this, true)
        uiState.value?.apply {
            selectionOnColor = GlobalLabelTextConfigure.selectionOnColor
            selectionOffColor = GlobalLabelTextConfigure.selectionOffColor
            selectionIndex = LABEL_SELECTION_1
        }
        binding?.tvLabelTextSelection1?.setOnClickListener {
            refreshSelection(LABEL_SELECTION_1)
            mListener?.invoke(LABEL_SELECTION_1)
        }
        binding?.tvLabelTextSelection2?.setOnClickListener {
            refreshSelection(LABEL_SELECTION_2)
            mListener?.invoke(LABEL_SELECTION_2)
        }
        binding?.tvLabelTextSelection3?.setOnClickListener {
            refreshSelection(LABEL_SELECTION_3)
            mListener?.invoke(LABEL_SELECTION_3)
        }
        binding?.tvLabelTextSelection4?.setOnClickListener {
            refreshSelection(LABEL_SELECTION_4)
            mListener?.invoke(LABEL_SELECTION_4)
        }
    }

    private fun refreshSelection(index: Int) {
        updateUiState {
            it?.apply {
                selectionIndex = index
            }
        }
    }
}