package com.apemans.tdprintercomponentimpl.webapi

import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_SECRET
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_WEB
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/10 6:44 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
interface ITDPrinterApi {

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(TDPrinterApiConstant.API_PATH_OF_DEVICE_UPDATE_NAME)
    fun updateDeviceName(@Body body: UpdateDeviceNameBody) : Flow<YRApiResponse<Any?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(TDPrinterApiConstant.API_PATH_OF_DEVICE_INFO)
    fun getDeviceInfo(@Query("uuid") deviceId : String) : Flow<YRApiResponse<BindDevice?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(TDPrinterApiConstant.API_PATH_OF_DEVICE_BIND_LIST)
    fun getDeviceRelationList(@Query("uuid") deviceId : String, @Query("page") page : Int, @Query("per_page") per_page : Int) : Flow<YRApiResponse<DeviceRelationListResult?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(TDPrinterApiConstant.API_PATH_OF_DEVICE_DELETE_SHARING)
    fun deleteSharingDevice(@Path("id") id : Int) : Flow<YRApiResponse<Any?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(TDPrinterApiConstant.API_PATH_OF_DEVICE_SEND_SHARING)
    fun sendDeviceSharing(@Body body : SharingDeviceBody) : Flow<YRApiResponse<ShareDeviceResult?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(TDPrinterApiConstant.API_PATH_OF_DEVICE_UPGRADE_STATUS)
    fun getDeviceUpgradeStatus(@Query("uuid") deviceId : String) : Flow<YRApiResponse<DeviceUpgradeStatusResult?>?>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(TDPrinterApiConstant.API_PATH_OF_DEVICE_UPDATE_NOTICE)
    fun updateDeviceNotice(@Body body : UpdateDeviceNoticeBody) : Flow<YRApiResponse<Any?>?>

    @POST(TDPrinterApiConstant.API_PATH_OF_DEVICE_DELETE_DEVICE)
    fun deleteDevice(@Body body : DeleteDeviceBindingBody) : Flow<YRApiResponse<Any?>?>

}