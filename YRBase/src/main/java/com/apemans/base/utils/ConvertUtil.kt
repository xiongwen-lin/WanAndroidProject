package com.apemans.base.utils

import java.lang.StringBuilder

/**
 * @Auther: Administrator
 * @datetime: 2021/9/6
 * @desc:
 */
object ConvertUtil {
    fun convertListToString(list: List<String?>): String {
        return if (com.apemans.base.utils.CollectionUtil.isEmpty(list)) {
            String()
        } else {
            val result = StringBuilder()
            val iterator: Iterator<*> = list.iterator()
            while (iterator.hasNext()) {
                result.append(iterator.next() as String?)
                if (iterator.hasNext()) {
                    result.append(",")
                }
            }
            result.toString()
        }
    }
}
