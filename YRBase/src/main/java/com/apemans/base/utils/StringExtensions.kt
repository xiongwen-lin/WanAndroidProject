/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.base.utils

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/17 09:03
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

/**
 * 查找字符串中包含flag的个数
 * @param flag 要查找的 flag
 * @return 包含flag的个数
 */
fun String.findFlagCount(flag: String): Int {
    var counter = 0
    fun findFlag(origin: String, flag: String): Int {
        val index = origin.indexOf(flag)
        if (index == -1) {
            return counter
        }
        //找到，则++ 一次
        counter++
        findFlag(origin.substring(origin.indexOf(flag) + flag.length), flag)
        return counter
    }
    return findFlag(this, flag)
}
