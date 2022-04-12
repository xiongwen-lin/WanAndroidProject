@file:Suppress("unused")

package com.apemans.yruibusiness.ui.toolbar

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dylanc.longan.dp
import com.apemans.base.R

/**
 * @author Dylan Cai
 */

enum class NavIconType {
    NORMAL, NONE
}

open class ToolbarConfig(
    var title: String? = null,
    @StringRes var titleRes: Int = -1,
    var navIconType: NavIconType = NavIconType.NORMAL,
    @DrawableRes var leftIcon: Int = Companion.leftIcon,
    @DrawableRes var rightIcon: Int = -1,
    var rightText: String? = null,
    @StringRes var rightTextRes: Int = -1,
    @DrawableRes var rightSecondIcon: Int = -1,
    var rightSecondText: String? = null,
    @StringRes var rightSecondTextRes: Int = -1,
    var titleTextSize: Float = Companion.titleTextSize,
    var height: Float = Companion.height,
    @ColorInt var titleTextColor: Int = Companion.titleTextColor,
    @ColorInt var backgroundColor: Int = Companion.backgroundColor
) {
    var onLeftBtnClick: (View) -> Unit = { (it.context as? Activity)?.finish() }
        private set

    var onRightBtnClick: (View) -> Unit = {}
        private set

    var onRightSecondBtnClick: (View) -> Unit = {}
        private set

    fun leftIcon(@DrawableRes icon: Int, onClick: (View) -> Unit) {
        leftIcon = icon
        onLeftBtnClick = onClick
    }

    fun rightText(@StringRes resId: Int, onClick: (View) -> Unit) {
        rightTextRes = resId
        onRightBtnClick = onClick
    }

    fun rightText(text: String, onClick: (View) -> Unit) {
        rightText = text
        onRightBtnClick = onClick
    }

    fun rightIcon(@DrawableRes icon: Int, onClick: (View) -> Unit) {
        rightIcon = icon
        onRightBtnClick = onClick
    }

    fun rightSecondText(text: String, onClick: (View) -> Unit) {
        rightSecondText = text
        onRightSecondBtnClick = onClick
    }

    fun rightSecondText(@StringRes rightText: Int, onClick: (View) -> Unit) {
        rightSecondTextRes = rightText
        onRightSecondBtnClick = onClick
    }

    fun rightSecondIcon(@DrawableRes icon: Int, onClick: (View) -> Unit) {
        rightSecondIcon = icon
        onRightSecondBtnClick = onClick
    }

    companion object {
        var height = 54.dp
        var titleTextSize = 18f

        @ColorInt
        var titleTextColor = Color.BLACK

        @ColorInt
        var backgroundColor = Color.WHITE

        @DrawableRes
        var leftIcon: Int = R.drawable.ic_nav_return_off
    }
}