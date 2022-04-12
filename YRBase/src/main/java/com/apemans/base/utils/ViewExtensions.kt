package com.apemans.base.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import java.util.*

//inline var View.isVisible: Boolean
//    get() = visibility == View.VISIBLE
//    set(value) {
//        visibility = if (value) View.VISIBLE else View.GONE
//    }

inline var View.enable: Boolean
    get() = isEnabled
    set(value) {
        isEnabled = value
        alpha = if (value) 1f else 0.5f
    }

fun View.dp(value: Int) = dp(value.toFloat()).toInt()

fun View.dp(value: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
)

fun View.sp(value: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, value.toFloat(), resources.displayMetrics
).toInt()

fun View.getLInWindow(): IntArray {
    val locations = IntArray(2)
    getLocationInWindow(locations)
    return locations
}

fun View.getLocationXInWindow(): Int {
    return getLInWindow()[0]
}

fun View.getLocationYInWindow(): Int {
    return getLInWindow()[1]
}

fun View.getLOnScreen(): IntArray {
    val locations = IntArray(2)
    getLocationOnScreen(locations)
    return locations
}

fun View.getLocationXOnScreen(): Int {
    return getLOnScreen()[0]
}

/**获取在屏幕上的坐标**/
fun View.getLocationYOnScreen(): Int {
    return getLOnScreen()[1]
}

fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}

fun <T : View> T.singleClick(block: (T) -> Unit) =
    setOnClickListener(object : SingleClickListener() {
        override fun onSingleClick(v: View) {
            block(v as T)
        }
    })

fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener {
    block(it as T)
}

//获取子View集合
val ViewGroup.children: List<View> get() = (0 until childCount).map { getChildAt(it) }

/**改变大小**/
fun View.resize(width: Int, height: Int) {
    if (width < 0 || height < 0) return
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}

/**
 * View 可见设置
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * View 不可见设置
 */
fun View.gone() {
    visibility = View.GONE
}

fun View.setStatusVisible(isVisible: Boolean) {
    if (isVisible) {
        visible()
    } else {
        gone()
    }
}

/******设置padding******/
fun View.updatePadding(
    paddingStart: Int = getPaddingStart(),
    paddingTop: Int = getPaddingTop(),
    paddingEnd: Int = getPaddingEnd(),
    paddingBottom: Int = getPaddingBottom()
) {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
}


fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

fun View.setPaddingTop(value: Int) =
    setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

fun View.setPaddingBottom(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)


fun View.updateHeight(value: Int) {
    if (value < 0) return
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.updateWidth(value: Int) {
    if (value < 0) return
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * 私有扩展属性，允许2次点击的间隔时间
 */
private var <T : View> T.delayTime: Long
    get() = getTag(0x7FFF0001) as? Long ?: 0
    set(value) {
        setTag(0x7FFF0001, value)
    }

/**
 * 私有扩展属性，记录点击时的时间戳
 */
private var <T : View> T.lastClickTime: Long
    get() = getTag(0x7FFF0002) as? Long ?: 0
    set(value) {
        setTag(0x7FFF0002, value)
    }

/***私有扩展方法，判断能否触发点击事件***/
private fun <T : View> T.canClick(): Boolean {
    var flag = false
    val now = System.currentTimeMillis()
    if (now - this.lastClickTime >= this.delayTime) {
        flag = true
        this.lastClickTime = now
    }
    return flag
}

/**
 * 扩展点击事件，默认 500ms 内不能触发 2 次点击
 */
fun <T : View> T.clickWithDuration(time: Long = 500, block: (T) -> Unit) {
    delayTime = time
    setOnClickListener {
        if (canClick()) {
            block(this)
        }
    }
}

/**
 * 扩展点击事件，默认 500ms 内不能触发 2 次点击
 */
fun <T : View> T.setOnClickListener(time: Long = 500, block: (T) -> Unit) {
    delayTime = time
    setOnClickListener {
        if (canClick()) {
            block(this)
        }
    }
}

/**
 * 扩展点击事件，默认 500ms 内不能触发 2 次点击
 */
fun <T : View> T.setOnClickListenerHH(time: Long = 500, onClickMe: OnClickMe) {
    delayTime = time
    setOnClickListener {
        if (canClick()) {
            onClickMe.onClickHH(this)
        }
    }
}

interface OnClickMe {
    fun onClickHH(v: View)
}


/**
 * 扩展点击事件，默认 500ms 内不能触发 2 次点击
 */
fun <T : View> T.clickWithDurationCallback(time: Long = 500, callback: ClickDurationCallback) {
    delayTime = time
    setOnClickListener {
        if (canClick()) {
            callback.onDelayClick(this)
        }
    }
}

interface ClickDurationCallback {
    fun onDelayClick(T: View)
}


/**
 * 单击
 */
abstract class SingleClickListener : View.OnClickListener {

    companion object {
        const val MIN_CLICK_DELAY_TIME = 500
    }

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onSingleClick(v);
        }
    }

    abstract fun onSingleClick(v: View)
}


/**
 * 在view显示之前读取宽高的扩展方法
 */
fun View.unDisplayViewSize(): IntArray {
    val size = IntArray(2)
    val width = View.MeasureSpec.makeMeasureSpec(
        0,
        View.MeasureSpec.UNSPECIFIED
    )
    val height = View.MeasureSpec.makeMeasureSpec(
        0,
        View.MeasureSpec.UNSPECIFIED
    )
    measure(width, height)
    size[0] = measuredWidth
    size[1] = measuredHeight
    return size
}