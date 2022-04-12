/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.quickui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/21 07:32
 * 说明: 自定义通用导航栏Toolbar
 *
 * 备注:
 *
 ***********************************************************/

/**
 * 全局配置SuperToolbar
 */
object SuperToolbarConfig {
    //标题大小
    var titleSizeDimension: Int = R.dimen.supertoolbar_default_title_fontsize

    //标题Color
    var titleColor: Int = R.color.supertoolbar_default_title_color

    //底部Line颜色
    var bottomLienColor: Int = R.color.supertoolbar_default_bottom_line_color

    //返回图标
    var imgReturnDrawableRes: Int = -1
        get() {
            if (field == -1) {
                throw RuntimeException("Please config imgReturnDrawableRes by SuperToolbarConfig")
            }
            return field
        }
}

class SuperToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private var txtTitle: TextView
    private var imgReturn: ImageView
    private var imgRight: ImageView
    private var viewLine: View
    private var root: FrameLayout

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_super_toolbar, this, true)
        txtTitle = rootView.findViewById(R.id.txtTitle)
        imgReturn = rootView.findViewById(R.id.imgReturn)
        imgRight = rootView.findViewById(R.id.imgRight)
        root = rootView.findViewById(R.id.root)
        viewLine = rootView.findViewById(R.id.viewLine)

        if (attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.SuperToolbar, defStyleAttr, R.style.Widget_DefaultToolbarStyle)
            txtTitle.text = attr.getString(R.styleable.SuperToolbar_superToolbar_title)
            //txtTitle.textSize =  attr.getDimension(R.styleable.SuperToolbar_superToolbar_titleSize, resources.getDimension(SuperToolbarConfig.titleSizeDimension))
            txtTitle.textSize = px2sp(
                context,
                attr.getDimension(R.styleable.SuperToolbar_superToolbar_titleSize, resources.getDimension(SuperToolbarConfig.titleSizeDimension))
            )
            txtTitle.setTextColor(attr.getColor(R.styleable.SuperToolbar_superToolbar_titleTextColor, SuperToolbarConfig.titleColor))

            //默认显示导航返回图标
            val showNavIcon = attr.getBoolean(R.styleable.SuperToolbar_superToolbar_showNavIcon, true)
            showActionReturn(showNavIcon)
            if (showNavIcon) {
                if (attr.hasValue(R.styleable.SuperToolbar_superToolbar_navIcon)) {
                    imgReturn.setImageDrawable(attr.getDrawable(R.styleable.SuperToolbar_superToolbar_navIcon))
                } else {
                    imgReturn.setImageResource(SuperToolbarConfig.imgReturnDrawableRes)
                }
            }

            //默认不显示底部Line
            val showBottomLine = attr.getBoolean(R.styleable.SuperToolbar_superToolbar_showBottomLine, false)
            showBottomLine(showBottomLine)
            viewLine.setBackgroundColor(attr.getColor(R.styleable.SuperToolbar_superToolbar_bottomLineColor, SuperToolbarConfig.bottomLienColor))

            //右边按钮通过属性设置
            if (attr.hasValue(R.styleable.SuperToolbar_superToolbar_rightIcon)) {
                imgRight.visibility = VISIBLE
                imgRight.setImageDrawable(attr.getDrawable(R.styleable.SuperToolbar_superToolbar_rightIcon))
            }

            attr.recycle()
        }
    }

    fun setNavigationOnClickListener(listener: OnClickListener) {
        imgReturn.setOnClickListener(listener)
    }

    fun setRightActionOnClickListener(listener: (View) -> Unit) {
        imgRight.clickWithDuration(1000, listener)
    }

    fun setNavigationOnClickListener(listener: (View) -> Unit) {
        imgReturn.clickWithDuration(1000, listener)
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun setTitleColor(@ColorInt color: Int) {
        txtTitle.setTextColor(color)
    }

    fun setTitle(title: Int) {
        txtTitle.setText(title)
    }

    fun setTitleOnClickListener(listener: (View) -> Unit) {
        txtTitle.clickWithDuration(1000, listener)
    }

    fun setNavReturnIcon(@DrawableRes iconRes: Int) {
        imgReturn.setImageResource(iconRes)
    }

    fun showRightAction(show: Boolean) {
        imgRight.isVisible = show
    }

    fun showActionReturn(show: Boolean) {
        imgReturn.isVisible = show
    }

    fun showBottomLine(show: Boolean) {
        viewLine.isVisible = show
    }

    fun setRightActionIcon(@DrawableRes iconRes: Int) {
        imgRight.setImageResource(iconRes)
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        root.setBackgroundColor(color)
    }

    fun setTitleTypeFace(tf: Typeface) {
        txtTitle.typeface = tf
    }

    val title: String get() = txtTitle.text.toString()

    val leftIcon: ImageView get() = imgReturn

    val rightIcon: ImageView get() = imgRight

}