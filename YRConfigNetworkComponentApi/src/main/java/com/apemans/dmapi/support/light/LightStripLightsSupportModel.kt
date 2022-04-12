/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.light

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 照明类灯带类型支持型号
 *
 * 备注:
 *
 */
annotation class LightStripLightsSupportModel {
    companion object {
        const val SL02 = "SL02"
        const val SL07 = "SL07"
        const val SL08 = "SL08"
        const val SL12 = "SL12"

        /**
         * @return 返回照明类灯带支持型号列表
         */
        private fun getStripLightsSupportList(): List<String> {
            val list = mutableListOf<String>()
            list.add(SL02)
            list.add(SL07)
            list.add(SL08)
            list.add(SL12)
            return list
        }

        /**
         * 判断model型号是否是照明类灯带类型
         * @param model 型号
         */
        fun isStripLightsLampModel(model: String): Boolean {
            return getStripLightsSupportList().find {
                it == model
            } != null
        }

    }
}