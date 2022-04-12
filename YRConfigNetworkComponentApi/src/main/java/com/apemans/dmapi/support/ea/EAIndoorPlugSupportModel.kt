/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.ea

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 电工类室内插座类型支持型号
 *
 * 备注:
 *
 */
object EAIndoorPlugSupportModel {
    const val SP10 = "SP10"
    const val SP11 = "SP11"
    const val SP20 = "SP20"
    const val SP21 = "SP21"
    const val SP22 = "SP22"
    const val SP23 = "SP23"
    const val SP27 = "SP27"
    const val SP31 = "SP31"
    const val SS30N = "SS30N"
    const val SS60 = "SS60"

    /**
     * @return 获取支持电工类室内插座支持型号
     */
    private fun getEAIndoorPlugSupportList(): List<String> {
        val list = mutableListOf<String>()
        list.add(SP10)
        list.add(SP11)
        list.add(SP20)
        list.add(SP21)
        list.add(SP22)
        list.add(SP23)
        list.add(SP27)
        list.add(SP31)
        list.add(SS30N)
        list.add(SS60)
        return list
    }


    /**
     * 判断model型号是否是室内插座类型
     * @param model 型号
     */
    fun isEAIndoorPlugModel(model: String): Boolean {
        return getEAIndoorPlugSupportList().find {
            it == model
        } != null
    }
}