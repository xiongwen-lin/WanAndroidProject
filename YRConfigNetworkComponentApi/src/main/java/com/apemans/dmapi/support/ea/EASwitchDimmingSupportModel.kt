/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.ea

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 电工类调光开关类型支持型号
 *
 * 备注:
 *
 */
object EASwitchDimmingSupportModel {
    const val SR46 = "SR46"

    /**
     * @return 返回支持的电工类开发类型支持型号列表
     */
    private fun getSupportList(): List<String> {
        val list = mutableListOf<String>()
        list.add(SR46)
        return list
    }

    /**
     * 判断model型号是否是电工开关类型
     * @param model 型号
     */
    fun isEASwitchDimmingModel(model: String): Boolean {
        return getSupportList().find {
            it == model
        } != null
    }
}