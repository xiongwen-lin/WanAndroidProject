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
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.apemans.quickui.*
import com.apemans.quickui.switchwidget.SwitchButton

/***********************************************************
 * @Author : caro
 * @Date   : 2020/11/11
 * @Func:
 * 设置项View
 *
 * @Description:
 *
 *
 ***********************************************************/
class PreferenceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val root: LinearLayout
    val viewImage: ImageView
    private val textTitle: TextView
    private val textSubTitle: TextView
    private val tvValue: TextView
    val viewSwitch: SwitchButton
    private val viewArrowRight: ImageView
    private val viewSelected: ImageView


    private var paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.preference_item_splitLine)
    }

    init {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_preference_item, this, true)
        root = rootView.findViewById(R.id.root)
        /*
        background = context.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.selectableItemBackground)
        ).getDrawable(0)
         */
        PreferenceGlobalConfig.itemBackground?.let {
            setBackgroundResource(it)
        }
        viewImage = rootView.findViewById(R.id.viewImage)
        textTitle = rootView.findViewById(R.id.textTitle)
        textSubTitle = rootView.findViewById(R.id.txt_subTitle)
        tvValue = rootView.findViewById(R.id.tv_value)
        viewSwitch = rootView.findViewById(R.id.viewSwitch)
        viewArrowRight = rootView.findViewById(R.id.viewArrowRight)
        viewSelected = rootView.findViewById(R.id.viewSelected)
        isClickable = true
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.PreferenceView,
            defStyleAttr,
            R.style.Widget_DefaultToolbarStyle
        )


        //Flag Item Image
        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_itemIcon)) {
            viewImage.setImageDrawable(attr.getDrawable(R.styleable.PreferenceView_preferenceView_itemIcon))
            viewImage.visible()
        } else {
            viewImage.gone()
        }
        //Title
        textTitle.text = attr.getString(R.styleable.PreferenceView_preferenceView_title)
        textTitle.textSize = px2sp(
            context,
            attr.getDimension(
                R.styleable.PreferenceView_preferenceView_titleSize,
                resources.getDimension(R.dimen.preference_default_title_fontsize)
            )
        )
        textTitle.setTextColor(
            attr.getColor(R.styleable.PreferenceView_preferenceView_titleTextColor, Color.BLACK)
        )

        //subTitle
        val subTitleValue = attr.getString(R.styleable.PreferenceView_preferenceView_subTitle)
        if (!subTitleValue.isNullOrEmpty()) {
            textSubTitle.text = subTitleValue
            textSubTitle.textSize = px2sp(
                context,
                attr.getDimension(
                    R.styleable.PreferenceView_preferenceView_subTitleSize,
                    resources.getDimension(R.dimen.preference_default_subtitle_fontsize)
                )
            )
            textSubTitle.setTextColor(
                attr.getColor(R.styleable.PreferenceView_preferenceView_subTitleTextColor, Color.GRAY)
            )
            textSubTitle.visible()
        } else {
            textSubTitle.gone()
        }


        //value-right
        tvValue.text = attr.getString(R.styleable.PreferenceView_preferenceView_rightValueTitle)
        tvValue.textSize = px2sp(
            context,
            attr.getDimension(
                R.styleable.PreferenceView_preferenceView_rightValueSize,
                resources.getDimension(R.dimen.preference_default_subtitle_fontsize)
            )
        )
        tvValue.setTextColor(
            attr.getColor(R.styleable.PreferenceView_preferenceView_rightValueTextColor, Color.GRAY)
        )
        //Switch
        /*
        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_switchThumb)) {
            viewSwitch.thumbDrawable =
                attr.getDrawable(R.styleable.PreferenceView_preferenceView_switchThumb)
        }
        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_switchTruck)) {
            viewSwitch.trackDrawable =
                attr.getDrawable(R.styleable.PreferenceView_preferenceView_switchTruck)
        }*/

        val showSwitch =
            attr.getBoolean(R.styleable.PreferenceView_preferenceView_showSwitch, false)
        if (showSwitch) {
            viewSwitch.visible()
        } else {
            viewSwitch.gone()
        }

        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_arrowRightIcon)) {
            viewArrowRight.setImageDrawable((attr.getDrawable(R.styleable.PreferenceView_preferenceView_arrowRightIcon)))
        }


        //arrow
        val showArrowRight =
            attr.getBoolean(R.styleable.PreferenceView_preferenceView_showArrowRight, true)
        if (showArrowRight) {
            viewArrowRight.visible()
        } else {
            viewArrowRight.gone()
        }

        //select
        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_selected)) {
            viewSelected.setImageDrawable((attr.getDrawable(R.styleable.PreferenceView_preferenceView_selected)))
            viewSelected.visible()
        } else {
            viewSelected.gone()
        }

        //Line
        if (attr.hasValue(R.styleable.PreferenceView_preferenceView_bottomLineColor)) {
            paint.color = attr.getColor(
                R.styleable.PreferenceView_preferenceView_bottomLineColor,
                ContextCompat.getColor(context, R.color.preference_item_splitLine)
            )
        }
        val showBottomLine =
            attr.getBoolean(R.styleable.PreferenceView_preferenceView_showBottomLine, true)
        if (!showBottomLine) {
            paint.color = ContextCompat.getColor(context, android.R.color.transparent)
        }

        viewSwitch.setOnClickChangeListener { view, isChecked ->
            viewSwitch.isChecked = !isChecked
            if (isChecked){
                viewSwitch.tag=0
            }else{
                viewSwitch.tag=1
            }
            if (switchListener != null) {
                if (isChecked) {
                    switchListener?.onSwitchOff()
                } else {
                    switchListener?.onSwitchTurn()
                }
            }
        }
        attr.recycle()

    }


    private var switchListener: SwitchListener? = null
    fun setOnSwitchListener(switchListener: SwitchListener) {
        this.switchListener = switchListener
    }

    interface SwitchListener {
        fun onSwitchTurn()
        fun onSwitchOff()
    }

    private val lineWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 0.5f, resources.displayMetrics
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            paddingLeft.toFloat(),
            height - lineWidth,
            width.toFloat() - paddingRight,
            height.toFloat(),
            paint
        )
    }

    var View.isVisible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }

    fun View.setEnable(enable: Boolean) {
        val a = if (enable) 1f else 0.2f
        textTitle.alpha = a
        textSubTitle.alpha = a
        tvValue.alpha = a
        viewSwitch.alpha = a
        viewArrowRight.alpha = a

        isEnabled = enable
        isClickable = enable
    }

    fun setPreferenceName(name: String) {
        textTitle.text = name
        textTitle.visible()
    }

    fun setPreferenceValue(value: String) {
        tvValue.text = value
        tvValue.visible()
    }

    fun setSwitchValue(value: String) {
        if (value =="1"){
            viewSwitch.isChecked=true
            viewSwitch.tag=1
        }

        if (value =="0"){
            viewSwitch.isChecked=false
            viewSwitch.tag=0
        }
    }

    fun showSwitch(show: Boolean) = if (show) {
        viewSwitch.visible()
    } else {
        viewSwitch.gone()
    }

    fun enableSwitch(enable: Boolean) {
        viewSwitch.enable = enable
    }

    fun showSelected(selected: Boolean) = if (selected) {
        viewSelected.visible()
    } else {
        viewSelected.gone()
    }

    fun setOnCheckedChanged(onCheckedChanged: SwitchButton.OnCheckedChangeListener) {
        viewSwitch.setOnCheckedChangeListener(onCheckedChanged)
    }

    fun showTextSummary(show: Boolean) = if (show) {
        tvValue.visible()
    } else {
        tvValue.gone()
    }

    fun setTextTitleColor(@ColorInt color: Int) {
        textTitle.setTextColor(color)
    }

    fun setRightValueTextTitleColor(@ColorInt color: Int) {
        tvValue.setTextColor(color)
    }

    fun setPreferenceIcon(@DrawableRes iconRes: Int) {
        viewImage.setImageResource(iconRes)
        viewImage.visible()
    }

    fun setArrowRightIcon(@DrawableRes iconRes: Int) {
        viewArrowRight.setImageResource(iconRes)
    }

    fun showArrowRightIcon(show: Boolean) = if (show) {
        viewArrowRight.visible()
    } else {
        viewArrowRight.gone()
    }

    fun setPaddingEnd(endPx: Int) {
        // root.layoutParams.
    }

}