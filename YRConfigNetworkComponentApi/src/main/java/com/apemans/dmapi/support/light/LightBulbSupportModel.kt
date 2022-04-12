/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.light

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 照明类灯泡类型支持型号
 *
 * 备注:
 *
 */
annotation class LightBulbSupportModel {
    companion object {
        const val SB30 = "SB30"
        const val SB50 = "SB50"
        const val SB53 = "SB53"
        const val SB60 = "SB60"
        const val DL46 = "DL46"

        /**
         * @return 返回灯泡类型支持型号列表
         */
        private fun getLightBulbSupportList(): List<String> {
            val list = mutableListOf<String>()
            list.add(SB30)
            list.add(SB50)
            list.add(SB53)
            list.add(SB60)
            list.add(DL46)
            return list
        }

        /**
         * 判断model型号是否是照明类灯泡类型
         * @param model 型号
         */
        fun isLightBulbModel(model: String): Boolean {
            return getLightBulbSupportList().find {
                it == model
            } != null
        }
    }
}