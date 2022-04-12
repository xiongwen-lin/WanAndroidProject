package com.apemans.smartipcimpl.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class SwitchImageButton(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    var isOn = false
        private set
    var toggleEnable = true
    private var mOnStateResId = 0
    private var mOffStateResId = 0
    private var mOnStateDrawable: Drawable? = null
    private var mOffStateDrawable: Drawable? = null
    private var mListener: OnStateChangeListener? = null
    private var mClickListener: OnClickListener? = null

    override fun setOnClickListener(listener: OnClickListener?) {
        mClickListener = listener
    }

    fun initBtn(onResId: Int, offResId: Int) {
        setOnStateRes(onResId)
        setOffStateRes(offResId)
        refreshOnState()
    }

    fun initBtn(onRes: Drawable?, offRes: Drawable?) {
        setOnStateRes(onRes)
        setOffStateRes(offRes)
        refreshOnState()
    }

    fun toggleNoCallback() {
        toggle()
        refreshOnState()
    }

    fun setListener(listener: OnStateChangeListener?) {
        mListener = listener
    }

    private fun init() {
        super.setOnClickListener { v ->
            toggleCallback()
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }
    }

    private fun toggle() {
        isOn = !isOn
    }

    private fun refreshOnState() {
        if (isOn) {
            if (mOnStateDrawable != null) {
                setImageDrawable(mOnStateDrawable)
            } else {
                setImageResource(mOnStateResId)
            }
        } else {
            if (mOffStateDrawable != null) {
                setImageDrawable(mOffStateDrawable)
            } else {
                setImageResource(mOffStateResId)
            }
        }
    }

    private fun notifyOnStateChange(on: Boolean) {
        if (mListener != null) {
            mListener!!.onStateChange(on)
        }
    }

    private fun setOnStateRes(resId: Int) {
        mOnStateResId = resId
    }

    private fun setOffStateRes(resId: Int) {
        mOffStateResId = resId
    }

    private fun setOnStateRes(drawable: Drawable?) {
        mOnStateDrawable = drawable
    }

    private fun setOffStateRes(drawable: Drawable?) {
        mOffStateDrawable = drawable
    }

    private fun toggleCallback() {
        if (!toggleEnable) {
            return
        }
        toggle()
        refreshOnState()
        notifyOnStateChange(isOn)
    }

    interface OnStateChangeListener {
        fun onStateChange(on: Boolean)
    }

    init {
        init()
    }
}