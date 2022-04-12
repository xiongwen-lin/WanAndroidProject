/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.ea

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 电工类室外插座类型支持型号
 *
 * 备注:
 *
 */
annotation class EAOutdoorPlugSupportModel {
    companion object {
        const val SS31 = "SS31"
        const val SS32 = "SS32"
        const val SS33 = "SS33"
        const val SS34 = "SS34"
        const val SS36 = "SS36"
        const val SS42 = "SS42"

        /**
         * @return 获取电工类室外插座类型支持型号
         */
        private fun getEAOutdoorPlugSupportList(): List<String> {
            val list = mutableListOf<String>()
            list.add(SS31)
            list.add(SS32)
            list.add(SS33)
            list.add(SS34)
            list.add(SS36)
            list.add(SS42)
            return list
        }

        /**
         * 判断model型号是否是室外插座类型
         * @param model 型号
         */
        fun isEAOutdoorPlugModel(model: String): Boolean {
            return getEAOutdoorPlugSupportList().find {
                it == model
            } != null
        }
    }
}