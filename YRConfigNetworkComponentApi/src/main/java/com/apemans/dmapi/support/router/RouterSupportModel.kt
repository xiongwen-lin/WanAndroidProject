/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */
package com.apemans.dmapi.support.router

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:29
 * 说明: 路由器类型支持型号
 *
 * 备注:
 *
 */
annotation class RouterSupportModel {
    companion object {
        const val R2 = "R2"

        /**
         * @return 返回路由器类支持型号列表
         */
        fun getRouterSupportList(): List<String> {
            val list = mutableListOf<String>()
            list.add(R2)
            return list
        }

        /**
         * 判断model型号是否是Router类型
         * @param model 型号
         */
        fun isRouterModel(model: String): Boolean {
            return getRouterSupportList().find {
                it == model
            } != null
        }

    }
}