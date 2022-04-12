/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.customserviceapi.webapi

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/1
 * @desc:
 */
/**
 * 创建反馈Body类
 */
data class CreateFeedbackBody(
    var type_id : Int,
    var product_id : Int,
    var email : String,
    var content : String,
    var image : String?
)