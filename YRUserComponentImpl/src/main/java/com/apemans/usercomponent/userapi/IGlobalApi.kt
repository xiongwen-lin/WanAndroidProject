package com.apemans.usercomponent.userapi

import com.apemans.business.apisdk.client.define.*
import com.apemans.userapi.paths.ApiConstant
import com.apemans.userapi.request.*
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 全球服务相关接口
 *
 * 每个接口的@Headers要标明BaseUrlName（对应那一个域名）和ApiSignType（对应那种加密）
 */
interface IGlobalApi {

    /**
     * 获取服务器时间
     */
    @Headers(HEADER_BASE_URL_NAME_OF_GLOBAL, HEADER_API_SIGN_TYPE_NORMAL)
    @GET(ApiConstant.API_PATH_OF_GLOBAL_TIME)
    fun getGlobalTimeAsFlow() : Flow<YRApiResponse<GlobalTimeResult>>

    /**
     * 根据国家码获取全球服务地址，针对没有注册的情况优先使用
     */
    @Headers(HEADER_BASE_URL_NAME_OF_GLOBAL, HEADER_API_SIGN_TYPE_NORMAL)
    @GET(ApiConstant.API_PATH_OF_GLOBAL_URL_WITH_COUNTRY)
    fun getGlobalUrlWithCountryAsFlow(@Query("country") country : String) : Flow<YRApiResponse<GlobalUrlResult>>

    /**
     * 根据国家码或账号获取全球服务地址，针对已注册的情况优先
     */
    @Headers(HEADER_BASE_URL_NAME_OF_GLOBAL, HEADER_API_SIGN_TYPE_NORMAL)
    @GET(ApiConstant.API_PATH_OF_GLOBAL_URL_WITH_COUNTRY_OR_ACCOUNT)
    fun getGlobalUrlWithCountryOrAccountAsFlow(@Query("country") country : String, @Query("account") account : String) : Flow<YRApiResponse<GlobalUrlResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(ApiConstant.API_PATH_OF_FEEDBACK_CREATE)
    fun createFeedback(@Body body: CreateFeedbackBody): Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(ApiConstant.API_PATH_OF_FEEDBACK_CHECK)
    fun checkFeedback(): Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_S3, HEADER_API_SIGN_TYPE_SECRET)
    @GET(ApiConstant.API_PATH_OF_UPLOAD_FEEDBACK_SIGN)
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