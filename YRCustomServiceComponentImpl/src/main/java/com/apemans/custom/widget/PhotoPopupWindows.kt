package com.apemans.custom.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import com.apemans.custom.R


class PhotoPopupWindows(context: Activity, myOnClick: OnClickSelectPhotoListener?) :
    PopupWindow(context) {
    private var mMenuView: View? = null
    private val context: Context
    private val myOnClick: OnClickSelectPhotoListener?

    interface OnClickSelectPhotoListener {
        fun onClick(takePhoto: Boolean)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        // TODO Auto-generated method stub
        // PopupWindow 导入
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mMenuView = inflater.inflate(R.layout.cust_pop_select_photo, null)
        mMenuView?.findViewById<Button>(R.id.btnTakePhoto)?.setOnClickListener {
            myOnClick?.onClick(true)
            dismiss()
        }
        mMenuView?.findViewById<Button>(R.id.btnAlbum)?.setOnClickListener {
            myOnClick?.onClick(false)
            dismiss()
        }
        mMenuView?.findViewById<Button>(R.id.btnCancel)?.setOnClickListener {
            dismiss()
        }

        // 导入布局
        this.contentView = mMenuView
        // 设置动画效果
        this.animationStyle = R.style.from_bottom_anim
        this.width = ViewGroup.LayoutParams.FILL_PARENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置可触
        this.isFocusable = true
        val dw = ColorDrawable(-0x33fef3ef)
        setBackgroundDrawable(dw)
        // 单击弹出窗以外处 关闭弹出窗
        mMenuView!!.setOnTouchListener { _, event ->
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