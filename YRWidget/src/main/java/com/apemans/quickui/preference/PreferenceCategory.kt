package com.apemans.quickui.preference

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.apemans.quickui.R
import com.apemans.quickui.px2sp
import com.apemans.quickui.visible

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
class PreferenceCategory @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val viewTextCategory: TextView


    init {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_preference_category, this, true)
        viewTextCategory = rootView.findViewById(R.id.viewTextCategory)


        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.PreferenceCategory,
            defStyleAttr,
            R.style.Widget_DefaultToolbarStyle
        )

        //Title
        viewTextCategory.text =
            attr.getString(R.styleable.PreferenceCategory_preferenceCategory_title)
        viewTextCategory.textSize = px2sp(
            context,
            attr.getDimension(
                R.styleable.PreferenceCategory_preferenceCategory_titleSize,
                resources.getDimension(R.dimen.preference_default_title_fontsize)
            )
        )
        viewTextCategory.setTextColor(
            attr.getColor(
                R.styleable.PreferenceCategory_preferenceCategory_titleTextColor,
                Color.BLACK
            )
        )

        rootView.setBackgroundColor(
            attr.getColor(
                R.styleable.PreferenceCategory_preferenceCategory_backGroundColor,
                Color.GRAY
            )
        )
        attr.recycle()
    }


    fun setCateGoryName(name: String) {
        viewTextCategory.text = name
        viewTextCategory.visible()
    }


}