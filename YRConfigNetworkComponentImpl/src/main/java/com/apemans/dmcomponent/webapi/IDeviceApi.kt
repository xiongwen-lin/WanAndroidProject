package com.apemans.dmcomponent.webapi

import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_SECRET
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_WEB
import com.apemans.dmapi.webapi.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

/**
 * 设备接口
 */
interface IDeviceApi {

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_BIND_STATUS)
    fun getDeviceBindStatus() : Flow<YRApiResponse<DeviceBindStatusResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_RECENT_BIND)
    fun getRecentBindDevices() : Flow<YRApiResponse<MutableList<BindDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_NAME)
    fun updateDeviceName(@Body body: UpdateDeviceNameBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_LIST)
    fun getBindDevices(@Query("page") page : Int, @Query("per_page") per_page : Int) : Flow<YRApiResponse<BindDeviceListResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_INFO)
    fun getDeviceInfo(@Query("uuid") deviceId : String) : Flow<YRApiResponse<BindDevice>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_BIND_LIST)
    fun getDeviceRelationList(@Query("uuid") deviceId : String, @Query("page") page : Int, @Query("per_page") per_page : Int) : Flow<YRApiResponse<DeviceRelationListResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_DELETE_SHARING)
    fun deleteSharingDevice(@Path("id") id : Int) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_SEND_SHARING)
    fun sendDeviceSharing(@Body body : SharingDeviceBody) : Flow<YRApiResponse<ShareDeviceResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_FEEDBACK_SHARING)
    fun feedbackDeviceSharing(@Body body : SharingFeedbackBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_UPGRADE_STATUS)
    fun getDeviceUpgradeStatus(@Query("uuid") deviceId : String) : Flow<YRApiResponse<DeviceUpgradeStatusResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_SORT)
    fun sortDevice(@Body body : DeviceSortBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_BINDING)
    fun bindDevice(@Body body : DeviceBindingBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_CONFIGURE)
    fun updateDeviceConfigure(@Body body : UpdateDeviceConfigureBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_NOTICE)
    fun updateDeviceNotice(@Body body : UpdateDeviceNoticeBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_LEVEL_LIST)
    fun getGatewayDeviceList(@Query("type") type : String) : Flow<YRApiResponse<MutableList<GatewayDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(DeviceApiConstant.API_PATH_OF_DEVICE_CHILD_LIST)
    fun getSubDeviceList(@Query("uuid") deviceId : String) : Flow<YRApiResponse<MutableList<BindDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_DELETE_GATEWAY)
    fun deleteGatewayDevice(@Body body : DeleteDeviceBindingBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(DeviceApiConstant.API_PATH_OF_DEVICE_DELETE_DEVICE)
    fun deleteDevice(@Body body : DeleteDeviceBindingBody) : Flow<YRApiResponse<Any>>

}