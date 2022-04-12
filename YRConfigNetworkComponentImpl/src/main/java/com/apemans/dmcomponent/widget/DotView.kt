package com.apemans.dmcomponent.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.dylanc.longan.withStyledAttrs
import com.apemans.dmcomponent.R
import kotlin.math.min

/**
 * @author Dylan Cai
 */
class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var dotColor = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        withStyledAttrs(attrs, R.styleable.DotView) {
            dotColor = getColor(R.styleable.DotView_dotColor, Color.WHITE)
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        val radius = min(measuredWidth, measuredHeight) / 2f
        paint.color = dotColor
        canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, paint)
    }
}