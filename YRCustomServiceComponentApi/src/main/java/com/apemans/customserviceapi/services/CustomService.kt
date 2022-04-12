/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.customserviceapi.services

import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.business.apisdk.client.bean.YRApiResponse

import com.apemans.customserviceapi.webapi.AwsFilePreSign
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.customserviceapi.webapi.MessageData
import com.apemans.customserviceapi.webapi.RequestMessageBody
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/1
 * @desc:
 */
interface CustomService : IProvider {
    /**
     * 客服回复反馈消息
     */
    fun getMessageList(rows :Int,time :Int,type: Int): Flow<YRApiResponse<List<MessageData>>>

    /**
     * 创建反馈
     */
    fun createFeedback(body: CreateFeedbackBody): Flow<YRApiResponse<Any>>

    /**
     * 检查反馈消息是否频发
     */
    fun checkFeedback(): Flow<YRApiResponse<Any>>

    fun getUploadFeedbackFilePreSign(userId: String, userName: String, fileName: String, fileSize: Int): Flow<YRApiResponse<AwsFilePreSign>>

    /**
     * 上传媒体文件
     */
    fun upLoadFileToCloud(url: String, contentType: String, usenameParam: String, uploadFile: RequestBody) : Call<ResponseBody?>?

}