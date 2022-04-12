package com.apemans.quickui.superdialog

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.ly.genjidialog.GenjiDialog
import com.ly.genjidialog.other.DialogOptions
import com.ly.genjidialog.other.ViewHolder
import com.apemans.quickui.R

/***********************************************************
 * @Author : caro
 * @Date   : 2020/10/30
 * @Func:
 *
 * TODO 待扩展优化，增加更多属性设置 0底部按钮竖直排列 - 1横向排列
 *
 ***********************************************************/
class SmartDialog : BaseActionSuperDialog() {
    private var dialogStyle = DialogStyle.IOS_STYLE

    override fun inflateLayout(): Int {
        if (dialogStyle == DialogStyle.MATERIAL_STYLE) {
            return R.layout.awesome_dilaog
        }
        if (dialogStyle == DialogStyle.IOS_STYLE) {
            return R.layout.awesome_dilaog_ios_style
        }
        throw RuntimeException("please config dialog ui style")
    }

    /**
     * @param dialogStyle
     * 设置UI Dialog Style
     */
    fun setThemeStyle(@DialogStyle dialogStyle: Int): SmartDialog {
        this.dialogStyle = dialogStyle
        return this
    }

    override fun initView(viewHolder: ViewHolder) {
        super.initView(viewHolder)
        if (dialogStyle == DialogStyle.IOS_STYLE) {
            val line2Middle = viewHolder.getView<View>(R.id.line2Middle)!!
            line2Middle.isVisible = !negativeTextName.isNullOrEmpty() && !positiveTextName.isNullOrEmpty()
        }
    }

    override fun isDarkMode(isDarkMode: Boolean, dialogOptions: DialogOptions) {

    }

    companion object {
        fun build(
            manager: FragmentManager,
            options: (DialogOptions.(dialog: GenjiDialog) -> Unit)? = null
        ): SmartDialog {
            if (options != null) {
                val dialog = newSmartDialog(options)
                dialog.manager = manager
                return dialog
            }
            val dialog = newSmartDialog()
            dialog.manager = manager
            return dialog
        }

        fun build(
            manager: FragmentManager,
            owner: LifecycleOwner,
            options: (DialogOptions.(dialog: GenjiDialog) -> Unit)? = null
        ): SmartDialog {
            if (options != null) {
                val dialog = newSmartDialog(options)
                dialog.manager = manager
                return dialog
            }
            val dialog = newSmartDialog()
            dialog.manager = manager
            dialog.addLifeCycleOwner(owner)
            return dialog
        }
    }
}

private fun newSmartDialog(): SmartDialog {
    return SmartDialog()
}

private inline fun newSmartDialog(options: DialogOptions.(dialog: GenjiDialog) -> Unit): SmartDialog {
    val dialog = SmartDialog()
    dialog.getDialogOptions().options(dialog)
    return dialog
}
