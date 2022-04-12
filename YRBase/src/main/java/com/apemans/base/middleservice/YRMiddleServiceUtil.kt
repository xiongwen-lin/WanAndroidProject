package com.apemans.base.middleservice

import com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceFunctionName
import com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceSubFunctionName

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/12/9 11:29 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRMiddleServiceUtil {

    fun checkServiceFunctionNameAssignable(functionName: String?, protocol: Map<String, String>?) : Boolean {
        if (functionName.isNullOrEmpty() || protocol == null) {
            return false
        } else {
            return functionName.equals(protocol[YRMiddleServiceFunctionName], true)
        }
    }

    fun checkServiceAllFunctionNameAssignable(functionName: String?, protocol: Map<String, String>?) : Boolean {
        if (functionName.isNullOrEmpty() || protocol == null) {
            return false
        } else {
            val protocolFunctionName = protocol[YRMiddleServiceFunctionName].orEmpty().plus("/").plus(protocol[YRMiddleServiceSubFunctionName].orEmpty())
            return functionName.equals(protocolFunctionName, true)
        }
    }

    fun checkIsAssignableMiddleService(className: String?, prefix: String?) : Boolean {
        if (className.isNullOrEmpty() || prefix.isNullOrEmpty()) {
            return false
        }
        try {
            return className?.lowercase()?.let {
                val startIndex = it.lastIndexOf(".")
                if (startIndex < 0) {
                    false
                } else {
                    it.contains(prefix) && it.substring(startIndex + 1, it.length).orEmpty().startsWith(prefix)
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun checkIsAssignableMiddleServiceInRegisterList(className: String?, registerList: List<String>?) : Boolean {
        return registerList?.firstNotNullOfOrNull {
            checkIsAssignableMiddleService(className, it)
        } != null
    }
}