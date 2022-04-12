package com.apemans.usercomponent.user.view

import android.text.method.PasswordTransformationMethod
import android.view.View

class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source,0)
    }

    private inner class PasswordCharSequence(private val mSource: CharSequence,
                                             override val length: Int) : CharSequence {
        @JvmName("charAt1")
        fun charAt(index: Int): Char {
            return '•' // This is the important part
        }

        @JvmName("length1")
        fun length(): Int {
            return mSource.length // Return default
        }

        override fun get(index: Int): Char {
            return mSource[index]
        }

        override fun subSequence(start: Int, end: Int): CharSequence {
            return mSource.subSequence(start, end) // Return default
        }
    }
}