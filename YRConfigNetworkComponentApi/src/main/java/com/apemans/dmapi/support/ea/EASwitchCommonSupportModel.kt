/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.ea

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 电工类通用/普通开关类型支持型号
 *
 * 备注:
 *
 */
object EASwitchCommonSupportModel {
    const val SR40 = "SR40"
    const val SR41 = "SR41"
    const val SR42 = "SR42"
    const val SR43 = "SR43"

    /**
     * @return 返回支持的电工类开发类型支持型号列表
     */
    private fun getEASwitchSupportList(): List<String> {
        val list = mutableListOf<String>()
        list.add(SR40)
        list.add(SR41)
        list.add(SR42)
        list.add(SR43)
        return list
    }

    /**
     * 判断model型号是否是电工开关类型
     * @param model 型号
     */
    fun isEASwitchCommonModel(model: String): Boolean {
        return getEASwitchSupportList().find {
            it == model
        } != null
    }
}