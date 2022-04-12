/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.custom.webapi

import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_SECRET
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_S3
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_WEB

import com.apemans.apisdk.define.ApiConstant
import com.apemans.customserviceapi.webapi.AwsFilePreSign
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.customserviceapi.webapi.MessageData
import com.apemans.customserviceapi.webapi.RequestMessageBody
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/1
 * @desc:
 */
interface ICustomApi {

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(CustomApiConstant.API_PATH_OF_FEEDBACK_GET_MESSAGE)
    fun getMessageList( @Query("rows") rows: Int,@Query("time") time: Int,
                        @Query("type") type: Int): Flow<YRApiResponse<List<MessageData>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(CustomApiConstant.API_PATH_OF_FEEDBACK_CREATE)
    fun createFeedback(@Body body: CreateFeedbackBody): Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(CustomApiConstant.API_PATH_OF_FEEDBACK_CHECK)
    fun checkFeedback(): Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_S3, HEADER_API_SIGN_TYPE_SECRET)
    @GET(CustomApiConstant.API_PATH_OF_UPLOAD_FEEDBACK_SIGN)
    fun getUploadFeedbackFilePreSign(
        @Query("userid") userId: String,
        @Query("username") userName: String,
        @Query("filename") fileName: String,
        @Query("filesize") fileSize: Int
    ): Flow<YRApiResponse<AwsFilePreSign>>

    @Headers("x-amz-server-side-encryption:AES256")
    @PUT
    fun upLoadFileToCloud(
        @Url var1: String?,
        @Header("Content-Type") var2: String?,
        @Header("x-amz-tagging") var3: String?,
        @Body var4: RequestBody?
    ): Call<ResponseBody?>?
}