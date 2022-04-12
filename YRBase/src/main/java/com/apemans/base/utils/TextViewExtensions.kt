package com.apemans.base.utils

import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged

/**
 * 设置Text Color
 */
fun TextView.setTexColor(@ColorRes colorId: Int) = setTextColor(context.getCompatColor(colorId))

/**
 * 设置Text txt中sub text的颜色
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        Log.d(
            "ViewExtensions",
            "exception in setColorOfSubstring, text=$text, substring=$substring",
            e
        )
    }
}

/**
 * 设置Drawable Left
 * @param drawable 要设置的Drawable
 */
fun TextView.setDrawableLeft(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun TextView.setDrawableTop(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}


fun TextView.setDrawableRight(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun TextView.setDrawableBottom(@DrawableRes drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
}

private val beforeTextChangedDefault: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit =
    { _, _, _, _ -> }
private val onTextChangedDefault: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit =
    { _, _, _, _ -> }
private val afterTextChangedDefault: (s: Editable?) -> Unit = {}

/**
 * 简写addTextChangedListener
 */
fun TextView.addTextChangedListener(
    beforeTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = beforeTextChangedDefault,
    onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = onTextChangedDefault,
    afterTextChanged: (s: Editable?) -> Unit = afterTextChangedDefault
)  {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s, start, before, count)
        }
    })

    doAfterTextChanged {

    }
}
