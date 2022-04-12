package com.apemans.usercomponent.user.view

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.widget.AppCompatEditText

class ClearEditText @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle) : AppCompatEditText(context!!, attrs, defStyle), View.OnFocusChangeListener, TextWatcher {
    //删除按钮的引用
    private var mClearDrawable: Drawable? = null
    private var clearEditTextListener: ClearEditTextListener? = null
    private fun init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,2是获得右边的图片  顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables().get(2)
        if (mClearDrawable == null) {
            //mClearDrawable = getResources().getDrawable(R.drawable.emotionstore_progresscancelbtn);//过时
            //mClearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.emotionstore_progresscancelbtn);
            //mClearDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.btn_minus,null)
        }
        mClearDrawable?.getIntrinsicWidth()?.let {
            mClearDrawable?.setBounds(0, 0, it, mClearDrawable?.getIntrinsicHeight()!!)
        }

        // 默认设置隐藏图标
        setClearIconVisible(false)
        // 设置焦点改变的监听
        setOnFocusChangeListener(this)
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置在EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (getCompoundDrawables().get(2) != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                val touchable =
                    (event.getX() > getWidth() - getPaddingRight() - (mClearDrawable?.getIntrinsicWidth()
                        ?: 0)
                            && event.getX() < getWidth() - getPaddingRight())
                if (touchable) {
                    this.setText("")
                    if (clearEditTextListener != null) {
                        clearEditTextListener!!.onClear()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        setClearIconVisible(text.length > 0)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            val length = (getText()?.length ?: 0) > 0
            setClearIconVisible(length)
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected fun setClearIconVisible(visible: Boolean) {
        val right: Drawable? = if (visible) mClearDrawable else null
        setCompoundDrawables(
            getCompoundDrawables().get(0),
            getCompoundDrawables().get(1),
            right,
            getCompoundDrawables().get(3)
        )
    }

    /**
     * 设置晃动动画
     */
    fun setShakeAnimation() {
        this.setAnimation(shakeAnimation(5))
    }

    /**
     * 外部回调接口，可根据情况选择使用
     */
    interface ClearEditTextListener {
        fun onClear()
    }

    fun setClearEditTextListener(clearEditTextListener: ClearEditTextListener?) {
        this.clearEditTextListener = clearEditTextListener
    }

    companion object {
        /**
         * 晃动动画
         *
         * @param counts 1秒钟晃动多少下
         * @return
         */
        fun shakeAnimation(counts: Int): Animation {
            val translateAnimation: Animation = TranslateAnimation(0F, 10F, 0F, 0F)
            translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
            translateAnimation.duration = 1000
            return translateAnimation
        }
    }

    init {
        init()
    }
}