/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.quickui.label

import android.view.View
import com.apemans.quickui.R

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/9/22 3:01 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
/**
 * LabelTextConfigure 控件配置数据模型
 * text 主文本 适用所有控件
 * subText 副文本 适用LabelTextInfoIconView控件
 * detailText 详细文本 适用LabelTextDetailIconView、LabelTextDetailSwitchBtnView、LabelTextDetailSelectionView控件
 * textColor 副主文本字体颜色 适用所有控件
 * subTextColor 主文本字体颜色 适用LabelTextInfoIconView控件
 * detailTextColor 详细主文本字体颜色 详细文本 适用LabelTextDetailIconView、LabelTextDetailSwitchBtnView、LabelTextDetailSelectionView控件
 * rightIconRes 文本标签末尾图标 适用LabelTextIconView、LabelTextInfoIconView、LabelTextDetailIconView控件
 * rightIconVisibility 文本标签末尾图标显示 适用LabelTextIconView、LabelTextInfoIconView、LabelTextDetailIconView控件
 * switchOn 文本标签开关按钮状态 适用LabelTextSwitchBtnView、LabelTextDetailSwitchBtnView控件
 * selection1 文本标签单选或多选按钮文本（空间排序从左到右） 适用LabelTextSelectionView、LabelTextDetailSelectionView控件
 * selection2
 * selection3
 * selection4
 * selectionOnColor 文本标签单选或多选按钮文本选中颜色 适用LabelTextSelectionView、LabelTextDetailSelectionView控件
 * selectionOffColor 文本标签单选或多选按钮文本未选中颜色 适用LabelTextSelectionView、LabelTextDetailSelectionView控件
 * selectionIndex 文本标签单选或多选按钮文本选中索引 适用LabelTextSelectionView、LabelTextDetailSelectionView控件
 * dividerLineVisible label下划线显示 适用所有控件
 */

/**
 * 全局配置Label
 */
object GlobalLabelTextConfigure {
    var rightIconRes: Int = R.drawable.tools_right_arrow_gray

    var textColor: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config textColor,you can apply by R.color.theme_text_color")
            }
            return field
        }
    var subTextColor: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config textColor,you can apply by R.color.theme_sub_text_color")
            }
            return field
        }
    var detailTextColor: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config textColor,you can apply by R.color.theme_sub_text_color")
            }
            return field
        }
    var selectionOnColor: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config textColor,you can apply by R.color.theme_color ")
            }
            return field
        }
    var selectionOffColor: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config textColor,you can apply by R.color.theme_sub_text_color")
            }
            return field
        }
}

data class LabelTextConfigure(var text: String = "") {
    var subText: String = ""
    var detailText: String = ""
    var textColor: Int = GlobalLabelTextConfigure.textColor
    var subTextColor: Int = GlobalLabelTextConfigure.subTextColor
    var detailTextColor: Int = GlobalLabelTextConfigure.detailTextColor
    var rightIconRes: Int = R.drawable.tools_right_arrow_gray
    var rightIconVisibility: Int = View.VISIBLE
    var switchOn: Boolean = false
    var selection1: String = ""
    var selection2: String = ""
    var selection3: String = ""
    var selection4: String = ""
    var selectionOnColor: Int = GlobalLabelTextConfigure.selectionOnColor
    var selectionOffColor: Int = GlobalLabelTextConfigure.selectionOffColor
    var selectionIndex: Int = LABEL_SELECTION_1
    var dividerLineVisible : Int = View.VISIBLE
}

/**
 * 文本标签单选或多选按钮选中索引（空间排序从左到右）
 */
const val LABEL_SELECTION_1 = 0
const val LABEL_SELECTION_2 = 1
const val LABEL_SELECTION_3 = 2
const val LABEL_SELECTION_4 = 2
