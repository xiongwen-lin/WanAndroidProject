/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.light

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 照明类落地灯类型支持型号
 *
 * 备注:
 *
 */
annotation class LightFloorLampSupportModel {
    companion object {
        const val FL41 = "FL41"

        /**
         * @return 返回支持的照明类落地登支持型号列表
         */
        private fun getLightFloorLampSupportList(): List<String> {
            val list = mutableListOf<String>()
            list.add(FL41)
            return list
        }


        /**
         * 判断model型号是否是照明类落地灯类型
         * @param model 型号
         */
        fun isLightFloorLampModel(model: String): Boolean {
            return getLightFloorLampSupportList().find {
                it == model
            } != null
        }
    }
}