/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.base.utils

import com.google.gson.reflect.TypeToken

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/18 2:50 下午
 * 说明:
 *
 * 备注: Json转化工具类
 *
 ***********************************************************/
object JsonConvertUtil {

    fun <T> convertData(json : String?, clazz: Class<T>) : T? {
        if (json == null || json.isEmpty()) {
            return null
        }
        try {
            return com.apemans.base.utils.GsonInstance.getInstance().gson.fromJson(json, clazz)
        } catch (e : Exception) {
        }
        return null
    }

    fun <T> convertData(json : String?, typeToken: TypeToken<T>) : T? {
        if (json == null || json.isEmpty()) {
            return null
        }
        try {
            return com.apemans.base.utils.GsonInstance.getInstance().gson.fromJson(json, typeToken.type)
        } catch (e : Exception) {
        }
        return null
    }

    fun <T> convertToJson(target : T?) : String? {
        if (target == null) {
            return null
        }
        try {
            return com.apemans.base.utils.GsonInstance.getInstance().gson.toJson(target)
        } catch (e : Exception) {
        }
        return null
    }

}