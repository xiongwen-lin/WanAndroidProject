package com.apemans.quickui.superdialog

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import com.ly.genjidialog.other.DialogGravity
import com.ly.genjidialog.other.DialogOptions
import com.ly.genjidialog.other.ViewHolder
import com.apemans.quickui.R

/***********************************************************
 * @Author : caro
 * @Date   : 2020/10/30
 * @Func:
 * 通用提示框
 *
 * @Description:
 *
 *
 ***********************************************************/
class TipsDialog : BaseActionSuperDialog() {
    private var btn_action: AppCompatButton? = null
    private var img_text_left: ImageView? = null
    private var textLeftImageRes: Int? = null
    private val handler: Handler = Handler(Looper.getMainLooper())

    companion object {
        fun build(
            manager: FragmentManager,
        ): TipsDialog {
            val tipsDialog = TipsDialog()
            tipsDialog.manager = manager
            return tipsDialog
        }
    }

    override fun inflateLayout(): Int {
        return R.layout.tips_dilaog
    }

    override fun initView(viewHolder: ViewHolder) {
        super.initView(viewHolder)
        btn_action = viewHolder.getView(R.id.btn_action)
        img_text_left = viewHolder.getView(R.id.img_text_left)
        img_text_left?.apply {
            textLeftImageRes?.let {
                setImageResource(it)
                visibility = View.VISIBLE
            }
        }
    }

    override fun isDarkMode(isDarkMode: Boolean, dialogOptions: DialogOptions) {
        Log.i("isDarkMode", "---------isDarkMode:$isDarkMode")
        //当状态栏字体是白色时，需要设置一个verticalMargin等于状态栏高度
        if (isDarkMode) {
            dialogOptions.verticalMargin = requireContext().statusBarHeight.toFloat()
        }
    }

    fun setLeftImageRes(@DrawableRes textLeftImageRes: Int): BaseActionSuperDialog {
        this.textLeftImageRes = textLeftImageRes
        return this
    }

    fun showTips(delayMills: Long = 2000, @StyleRes newAnim: Int = R.style.TopTransAlphaADAnimation) {
        manager?.let {
            showOnWindow(it, DialogGravity.CENTER_TOP, newAnim)
            handler.postDelayed({
                dismissAllowingStateLoss()
            }, delayMills)
        }

    }
}



