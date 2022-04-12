package com.apemans.base.utils

import com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceFunctionName
import com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceSubFunctionName
import com.apemans.base.middleservice.YRMiddleServiceListener

class YRMiddleEventUtils {

    private var listenerMap : HashMap<String, HashMap<String, YRMiddleServiceListener?>> = HashMap<String, HashMap<String, YRMiddleServiceListener?>>()

    fun listenerList(protocol: MutableMap<String, String>?, lifeCycle: Any?,
                             parameters: MutableMap<String, Any>?, listener: YRMiddleServiceListener?): HashMap<String, HashMap<String, YRMiddleServiceListener?>> {
        if (protocol?.get(YRMiddleServiceFunctionName) == "add") {
            var hashMap = HashMap<String, YRMiddleServiceListener?>()
            hashMap[lifeCycle.toString()] = listener
            listenerMap[YRMiddleServiceSubFunctionName] = hashMap
        } else if (protocol?.get(YRMiddleServiceFunctionName) == "remove") {
            listenerMap[YRMiddleServiceSubFunctionName]?.remove(lifeCycle.toString())
        }
        return listenerMap
    }

    fun getListenerMap() : HashMap<String, HashMap<String, YRMiddleServiceListener?>> {
        return listenerMap
    }
}