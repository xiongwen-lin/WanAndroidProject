package com.apemans.quickui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/***********************************************************
 * @Author : caro
 * @Date   : 2020/11/2
 * @Func:
 *
 *
 * @Description:
 *
 * TODO
 ***********************************************************/
class SmartRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr) {
    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        //控件的宽度
        val screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        //  int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        //控件的高度
        val screenHeight = MeasureSpec.getSize(heightMeasureSpec)
        //测量子控件大小，必须调用，否则不显示页面内容
        // 计算出所有的childView的宽和高，调用后，它所有的childView的宽和高的值就被确定，也即getMeasuredWidth（）有值了。
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = measuredWidth
        val height = measuredHeight
        val childLeft = paddingLeft
        val childTop = paddingTop
        //由于这里child只有一个所以将整个长宽都设置给child
        val child: View = getChildAt(0)
        child.layout(childLeft, childTop, width - paddingRight, height - paddingBottom)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}