package com.apemans.usercomponent.mine.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.apemans.usercomponent.R

class SquareImageView : AppCompatImageView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    public override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height
        //圆角剪切
        val connerRadius = (resources.getDimension(R.dimen.dp_3) + 0.5).toInt()
        val path = Path()
        path.moveTo(0f, connerRadius.toFloat())
        path.arcTo(RectF(0F, 0F, (connerRadius * 2).toFloat(), (connerRadius * 2).toFloat()), -180f, 90f)
        path.lineTo((width - connerRadius).toFloat(), 0f)
        path.arcTo(RectF((width - 2 * connerRadius).toFloat(), 0F, width.toFloat(), (connerRadius * 2).toFloat()), -90f, 90f)
        path.lineTo(width.toFloat(), (height - connerRadius).toFloat())
        path.arcTo(
            RectF(
                (width - 2 * connerRadius).toFloat(), (height - 2 * connerRadius).toFloat(),
                width.toFloat(), height.toFloat()
            ), 0f, 90f
        )
        path.lineTo(connerRadius.toFloat(), height.toFloat())
        path.arcTo(RectF(0F, (height - 2 * connerRadius).toFloat(), (connerRadius * 2).toFloat(), height.toFloat()), 90f, 90f)
        path.close()
        canvas.clipPath(path)
        //灰色背景
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = resources.getColor(R.color.gray_d8dde6)
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        //内容
        super.onDraw(canvas)
    }
}