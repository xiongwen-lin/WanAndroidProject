package com.apemans.quickui.preference

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.apemans.quickui.R
import com.apemans.quickui.gone
import com.apemans.quickui.px2sp
import com.apemans.quickui.visible

/***********************************************************
 * @Author : QCoder
 * @Date   : 2020/11/126
 * @Func:
 * 设置项 View
 *
 * @Description:
 *
 *
 ***********************************************************/
class PreferenceBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val root: LinearLayout
    private val viewImage: ImageView
    private val title: TextView
    val seekBar: SeekBar
    private val degree: TextView
    private val unit: TextView

    var min = 0
    var max = 0


    private var paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.preference_item_splitLine)
    }

    init {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_preference_seekbar_item, this, true)
        root = rootView.findViewById(R.id.root)
        title = rootView.findViewById(R.id.textTitle)
        viewImage = rootView.findViewById(R.id.viewImage)
        seekBar = rootView.findViewById(R.id.seek_bar)
        degree = rootView.findViewById(R.id.tv_degree)
        unit = rootView.findViewById(R.id.tv_unit)

        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.PreferenceBar,
            defStyleAttr,
            0
        )

        background = context.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.selectableItemBackground)
        ).getDrawable(0)


        //Flag Item Image
        if (attr.hasValue(R.styleable.PreferenceBar_preferenceBar_rightIcon)) {
            viewImage.setImageDrawable(attr.getDrawable(R.styleable.PreferenceBar_preferenceBar_rightIcon))
            viewImage.visible()
        } else {
            viewImage.gone()
        }
        title.text = attr.getText(R.styleable.PreferenceBar_preferenceBar_title)
        //seekBar
        seekBar.progressDrawable =
            attr.getDrawable(R.styleable.PreferenceBar_preferenceBar_progressDrawable)
        seekBar.thumb = attr.getDrawable(R.styleable.PreferenceBar_preferenceBar_thumbDrawable)


        //degree
        degree.text = attr.getText(R.styleable.PreferenceBar_preferenceBar_degreeText)
        degree.textSize = px2sp(context,
            attr.getDimension(R.styleable.PreferenceBar_preferenceBar_degreeSize,
                resources.getDimension(R.dimen.preference_default_subtitle_fontsize)
            )
        )
        degree.setTextColor(
            attr.getColor(R.styleable.PreferenceBar_preferenceBar_degreeTextColor, Color.GRAY)
        )

        //unit
        unit.text = attr.getText(R.styleable.PreferenceBar_preferenceBar_unit)
        unit.textSize = px2sp(context,
            attr.getDimension(R.styleable.PreferenceBar_preferenceBar_unitSize,
                resources.getDimension(R.dimen.preference_default_title_fontsize)
            )
        )
        unit.setTextColor(attr.getColor(R.styleable.PreferenceBar_preferenceBar_unitColor,
            Color.GRAY))
        val progress = attr.getInteger(R.styleable.PreferenceBar_preferenceBar_progress, 0)

        max = attr.getInteger(R.styleable.PreferenceBar_preferenceBar_max, 100)
        min = attr.getInteger(R.styleable.PreferenceBar_preferenceBar_min, 0)
        seekBar.max = max
        setProgress(progress)

        //Line
        if (attr.hasValue(R.styleable.PreferenceBar_preferenceBar_bottomLineColor)) {
            paint.color = attr.getColor(R.styleable.PreferenceBar_preferenceBar_bottomLineColor,
                ContextCompat.getColor(context, R.color.preference_item_splitLine))
        }
        val showBottomLine =
            attr.getBoolean(R.styleable.PreferenceBar_preferenceBar_showBottomLine, true)
        if (!showBottomLine) {
            paint.color = ContextCompat.getColor(context, android.R.color.transparent)
        }

        isClickable = true

        attr.recycle()

    }


    private val lineWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 0.5f, resources.displayMetrics)


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(paddingLeft.toFloat(),
            height - lineWidth,
            width.toFloat() - paddingRight,
            height.toFloat(),
            paint)

    }

    var View.isVisible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }

    fun View.setEnable(enable: Boolean) {
        val a = if (enable) 1f else 0.2f
        title.alpha = a
        degree.alpha = a

        isEnabled = enable
        isClickable = enable
    }

    fun setPreferenceName(name: String) {
        title.text = name
        title.visible()
    }

    fun setDegree(progress: String) {
        degree.text = progress
    }

    fun setProgress(progress: Int) {
        seekBar.progress = progress
    }

    fun setSeekBarChange(listener: SeekBar.OnSeekBarChangeListener) {
        seekBar.setOnSeekBarChangeListener(listener)
    }

    fun setPreferenceIcon(@DrawableRes iconRes: Int) {
        viewImage.setImageResource(iconRes)
        viewImage.visible()
    }

}