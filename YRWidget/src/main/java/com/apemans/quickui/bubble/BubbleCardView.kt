package com.apemans.quickui.bubble

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.apemans.quickui.R
import com.apemans.quickui.dp
import kotlin.math.max
import kotlin.math.min

/***********************************************************
 * @Author : caro
 * @Date   : 2020/10/30
 * @Func:
 * 气泡View
 *
 * @Description:
 *
 *
 ***********************************************************/
const val OR_TOP = 0
const val OR_BOTTOM = 1
const val OR_LEFT = 2
const val OR_RIGHT = 3
class BubbleCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var a = context.obtainStyledAttributes(attrs, R.styleable.BubbleCardView, defStyleAttr, 0)
    private val backgroundColor = a.getColor(
        R.styleable.BubbleCardView_backgroundColor,Color.WHITE
    )
    // 0 1 2 3 = t b l r
    var orientation = a.getInteger(R.styleable.BubbleCardView_triangleOrientation,OR_BOTTOM)
    private val paint = Paint().apply {
        color = backgroundColor
        style = Paint.Style.FILL
        pathEffect = CornerPathEffect(5f)
    }

    private val path = Path()
    private val round = dp(10f)
    private val subscriptHeight = dp(12f)
    private val subscriptWidth = dp(12f) //这宽度其实是一半的宽度
    private val shadowColor = Color.parseColor("#10000000")
    private val shadowRadius =5f
    private val sizeRF = RectF()
    private val sizeRFSource = RectF()

    private var first = true


    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)

        sizeRF.set(round,round,(r-l)-round,(b - t)-round )
        sizeRFSource.set(sizeRF)
        updateSizeRF()
        if (first) {
            first = false
            if(orientation in 0 ..1){
                setSubscriptCenterX(sizeRF.centerX())
            }else{
                setSubscriptCenterY(sizeRF.centerY())
            }
        }

    }

    private fun updateSizeRF(){
        sizeRF.set(sizeRFSource)
        var dx = 0F
        var dy = 0F
        when (orientation) {
            OR_TOP -> {
                dy = -5f
                sizeRF.top = sizeRF.top + subscriptHeight
            }
            OR_BOTTOM -> {
                dy = 5f
                sizeRF.bottom = sizeRF.bottom - subscriptHeight
            }
            OR_LEFT -> {
                dx = -5f
                sizeRF.left = sizeRF.left + subscriptHeight
            }
            OR_RIGHT -> {
                dx = 5f
                sizeRF.right = sizeRF.right -subscriptHeight
            }
            else -> {}
        }
        paint.setShadowLayer(shadowRadius,dx,dy,shadowColor)
    }
    fun setSubscriptCenterX(x:Float){
        val sx = min(max(x,round + subscriptWidth + round ),sizeRFSource.width() - round)
        updateSizeRF()
        path.reset()
        val y = if(orientation == OR_TOP) sizeRF.top  else sizeRF.bottom
        val y2 = if(orientation == OR_TOP) y - subscriptHeight else y + subscriptHeight
        path.moveTo(sx -subscriptWidth,y)
        path.lineTo(sx,y2)
        path.lineTo(sx+subscriptWidth,y)
        path.close()
    }

    private fun setSubscriptCenterY(y:Float){
        val sy = min(max(y,round + subscriptWidth + round ),sizeRFSource.height() - round)
        updateSizeRF()
        path.reset()
        val x = if(orientation == OR_LEFT) sizeRF.left else sizeRF.right
        val x2 = if(orientation == OR_LEFT) x - subscriptHeight else x + subscriptHeight
        path.moveTo(x,sy - subscriptWidth)
        path.lineTo(x2,sy)
        path.lineTo(x,sy+subscriptWidth)
        path.close()
    }
    fun setSubscriptCenterX(x:Int){
        setSubscriptCenterX(x.toFloat())
        invalidate()
    }

    fun setBubbleOutset(v:Int){
        if(orientation in 0 ..1){
            setSubscriptCenterX(v + round)
        }else{
            setSubscriptCenterY(v.toFloat() + round)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(sizeRF,round,round,paint)
//        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawPath(path,paint)
    }
}