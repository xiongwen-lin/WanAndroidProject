package com.apemans.tdprintercomponentimpl.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import com.apemans.tdprintercomponentimpl.R

/**
 * @Auther: 蛮羊
 * @datetime: 2022/2/28
 * @desc:
 */
class MediaPopupWindows(context: Activity, myOnClick: OnClickMediaListener) : PopupWindow(context) {
    var mMenuView: View? = null
    val context: Context
    private val myOnClick: OnClickMediaListener

    interface OnClickMediaListener {
        fun onFaceBookClick()
        fun onTiktokClick()
        fun onInstagramClick()
        fun onEmailClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        // TODO Auto-generated method stub
        // PopupWindow 导入
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mMenuView = inflater.inflate(R.layout.td_pop_media, null)
        val ivFacebook = mMenuView?.findViewById<ImageView>(R.id.ivFacebook)
        val ivYoutube = mMenuView?.findViewById<ImageView>(R.id.ivYoutube)
        val ivInstagram = mMenuView?.findViewById<ImageView>(R.id.ivInstagram)
        val ivEmail = mMenuView?.findViewById<ImageView>(R.id.ivEmail)
        val btn_cancel = mMenuView?.findViewById<Button>(R.id.btnCancel)
        ivFacebook?.setOnClickListener {
            if (myOnClick != null) myOnClick.onFaceBookClick()
            dismiss()
        }
        ivYoutube?.setOnClickListener {
            if (myOnClick != null) myOnClick.onTiktokClick()
            dismiss()
        }
        ivInstagram?.setOnClickListener {
            if (myOnClick != null) myOnClick.onInstagramClick()
            dismiss()
        }
        ivEmail?.setOnClickListener {
            if (myOnClick != null) myOnClick.onEmailClick()
            dismiss()
        }
        btn_cancel?.setOnClickListener {
            dismiss()
        }

        // 导入布局
        this.contentView = mMenuView
        // 设置动画效果
        this.animationStyle = R.style.td_from_bottom_anim
        this.width = ViewGroup.LayoutParams.FILL_PARENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置可触
        this.isFocusable = true
        val dw = ColorDrawable(-0x33fef3ef)
        this.setBackgroundDrawable(dw)
        // 单击弹出窗以外处 关闭弹出窗
        mMenuView!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            val height = mMenuView!!.findViewById<View>(R.id.containerBtn).top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        }
    }

    init {
        this.context = context
        this.myOnClick = myOnClick
        init()
    }
}
