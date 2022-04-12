package com.apemans.dmcomponent.utils

import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.dylanc.longan.isTextEmpty

/**
 * @author Dylan Cai
 */


fun TextView.enableWhenOtherTextNotEmpty(vararg textViews: TextView) {
    isEnabled = textViews.all { it.isTextEmpty() }
    textViews.forEach {
        it.doAfterTextChanged {
            isEnabled = !textViews.any { textView -> textView.isTextEmpty() }
        }
    }
}