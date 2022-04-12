/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.custom.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.business.apisdk.client.bean.YRApiResponse

import com.apemans.custom.webapi.CustomApiHelper
import com.apemans.customserviceapi.ACTIVITY_PATH_CUSTOMSERVICE
import com.apemans.customserviceapi.services.CustomService
import com.apemans.customserviceapi.webapi.AwsFilePreSign
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.customserviceapi.webapi.MessageData

import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/1
 * @desc:
 */
@Route(path = ACTIVITY_PATH_CUSTOMSERVICE)
class CustomServiceProvider : CustomService {

    override fun getMessageList(rows :Int,time :Int,type: Int): Flow<YRApiResponse<List<MessageData>>> {
        return CustomApiHelper.customApi.getMessageList(rows,time,type)
    }
    override fun createFeedback(body: CreateFeedbackBody): Flow<YRApiResponse<Any>> {
        return CustomApiHelper.customApi.createFeedback(body)
    }

    override fun checkFeedback(): Flow<YRApiResponse<Any>> {
        return CustomApiHelper.customApi.checkFeedback()
    }

    override fun getUploadFeedbackFilePreSign(userId: String, userName: String, fileName: String, fileSize: Int): Flow<YRApiResponse<AwsFilePreSign>> {
        return CustomApiHelper.customApi.getUploadFeedbackFilePreSign(userId, userName, fileName, fileSize)
    }

    override fun upLoadFileToCloud(url: String, contentType: String, usenameParam: String, uploadFile: RequestBody): Call<ResponseBody?>? {
        return CustomApiHelper.customApi.upLoadFileToCloud(url, contentType, usenameParam, uploadFile)
    }

    override fun init(context: Context?) {

    }
}