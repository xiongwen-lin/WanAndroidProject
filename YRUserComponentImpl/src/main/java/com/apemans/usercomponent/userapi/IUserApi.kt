package com.apemans.usercomponent.userapi

import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_NORMAL
import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_SECRET
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_WEB
import com.apemans.userapi.paths.ApiConstant
import com.apemans.userapi.request.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

/**
 * 平台用户接口
 */
interface IUserApi {

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_REGISTER_SEND)
    fun registerSendCode(@Body body : RegisterSendBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_REGISTER_VERIFY)
    fun registerVerifyCode(@Body body : RegisterVerifyBody) : Flow<YRApiResponse<Any>>

    // API_PATH_OF_USER_LOGIN_VERIFY
    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_LOGIN_VERIFY)
    fun loginVerifyCode(@Body body : RegisterVerifyBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_REGISTER)
    fun register(@Body body : RegisterBody) : Flow<YRApiResponse<RegisterResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_LOGIN)
    fun login(@Body body : LoginBody) : Flow<YRApiResponse<LoginResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_LOGIN_SEND)
    fun forgotPwSendCode(@Body body : RegisterSendBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_LOGIN_RESET)
    fun resetPassword(@Body body : ResetPwBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(ApiConstant.API_PATH_OF_USER_LOGOUT)
    fun logout() : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(ApiConstant.API_PATH_OF_USER_GET_INFO)
    fun getUserInfo() : Flow<YRApiResponse<UserInfoResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(ApiConstant.API_PATH_OF_USER_UPDATE)
    fun updateUserInfo(@Body body : ResetUserPasswordBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(ApiConstant.API_PATH_OF_USER_PUT)
    fun uploadUserInfo(@Body body: UploadUserBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_TWO_AUTH_MAIL)
    fun sendTwoAutoCode(@Body body: TwoAuthMailBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @POST(ApiConstant.API_PATH_OF_USER_TWO_AUTH_AUTH)
    fun twoAuthLogin(@Body body: TwoAutoLoginBody) : Flow<YRApiResponse<LoginResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_NORMAL)
    @GET(ApiConstant.API_PATH_OF_USER_TUYA)
    fun getUidByAccount(@Query("account") account : String) : Flow<YRApiResponse<TuyaUidResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(ApiConstant.API_PATH_OF_USER_TUYA_ACCOUNT)
    fun getAccountByUid(@Query("uid") uid : String) : Flow<YRApiResponse<TuyaAccountResult>>

}