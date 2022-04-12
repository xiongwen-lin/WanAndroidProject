package com.apemans.userapi.request

/**
 * YR平台API接口返回数据model类
 */
data class YRApiResponse<T>(var code : Int, var msg : String, var data : T)