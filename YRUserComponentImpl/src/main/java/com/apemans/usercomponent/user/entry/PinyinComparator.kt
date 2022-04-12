package com.apemans.usercomponent.user.entry

import java.util.*

/**
 * 比较器类，主要就是根据ASCII码来对数据进行比较排序
 */
class PinyinComparator : Comparator<SortModel> {
    override fun compare(o1: SortModel, o2: SortModel): Int {
        return if (o1.letters == "@" || o2.letters == "#") {
            -1
        } else if (o1.letters == "#" || o2.letters == "@") {
            1
        } else {
            o1.letters!!.compareTo(o2.letters!!)
        }
    }
}