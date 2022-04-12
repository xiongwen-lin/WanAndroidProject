package com.apemans.router

import android.os.Bundle
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import java.io.Serializable

/**
 * @author Dylan Cai
 */
fun Postcard.with(vararg pairs: Pair<String, Any?>) = apply {
    for ((key, value) in pairs) {
        when (value) {
            null -> withString(key, null)

            is Boolean -> withBoolean(key, value)
            is Byte -> withByte(key, value)
            is Char -> withChar(key, value)
            is Double -> withDouble(key, value)
            is Float -> withFloat(key, value)
            is Int -> withInt(key, value)
            is Long -> withLong(key, value)
            is Short -> withShort(key, value)

            is Bundle -> withBundle(key, value)
            is CharSequence -> withCharSequence(key, value)
            is Parcelable -> withParcelable(key, value)

            is ByteArray -> withByteArray(key, value)
            is CharArray -> withCharArray(key, value)
            is FloatArray -> withFloatArray(key, value)


            is ShortArray -> withShortArray(key, value)

            is Array<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST")
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        withParcelableArray(key, value as Array<Parcelable>)
                    }
                    String::class.java.isAssignableFrom(componentType) -> {
                        extras.putStringArray(key, value as Array<out String>)
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\""
                        )
                    }
                }
            }
            is Serializable -> withSerializable(key, value)

            else -> {
                val valueType = value.javaClass.canonicalName
                throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
            }
        }
    }
}