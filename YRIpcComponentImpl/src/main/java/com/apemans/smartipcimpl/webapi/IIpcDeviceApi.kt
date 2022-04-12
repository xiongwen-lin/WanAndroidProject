package com.apemans.smartipcimpl.webapi

import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HEADER_API_SIGN_TYPE_SECRET
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_S3
import com.apemans.business.apisdk.client.define.HEADER_BASE_URL_NAME_OF_WEB
import com.apemans.smartipcapi.webapi.AwsFileListResult
import com.apemans.smartipcapi.webapi.FirmwareVersion
import com.apemans.smartipcapi.webapi.GatewayAndChildFirmwareVersion
import com.apemans.smartipcapi.webapi.PackageInfoResult
import com.apemans.smartipcapi.webapi.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

/**
 * 设备接口
 */
interface IIpcDeviceApi {

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_BIND_STATUS)
    fun getDeviceBindStatus() : Flow<YRApiResponse<DeviceBindStatusResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_RECENT_BIND)
    fun getRecentBindDevices() : Flow<YRApiResponse<MutableList<BindDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_NAME)
    fun updateDeviceName(@Body body: UpdateDeviceNameBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_LIST)
    fun getBindDevices(@Query("page") page : Int, @Query("per_page") per_page : Int) : Flow<YRApiResponse<BindDeviceListResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_INFO)
    fun getDeviceInfo(@Query("uuid") deviceId : String) : Flow<YRApiResponse<BindDevice>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_BIND_LIST)
    fun getDeviceRelationList(@Query("uuid") deviceId : String, @Query("page") page : Int, @Query("per_page") per_page : Int) : Flow<YRApiResponse<DeviceRelationListResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_DELETE_SHARING)
    fun deleteSharingDevice(@Path("id") id : Int) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_SEND_SHARING)
    fun sendDeviceSharing(@Body body : SharingDeviceBody) : Flow<YRApiResponse<ShareDeviceResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_FEEDBACK_SHARING)
    fun feedbackDeviceSharing(@Body body : SharingFeedbackBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_UPGRADE_STATUS)
    fun getDeviceUpgradeStatus(@Query("uuid") deviceId : String) : Flow<YRApiResponse<DeviceUpgradeStatusResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_SORT)
    fun sortDevice(@Body body : DeviceSortBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_BINDING)
    fun bindDevice(@Body body : DeviceBindingBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_CONFIGURE)
    fun updateDeviceConfigure(@Body body : UpdateDeviceConfigureBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_UPDATE_NOTICE)
    fun updateDeviceNotice(@Body body : UpdateDeviceNoticeBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_LEVEL_LIST)
    fun getGatewayDeviceList(@Query("type") type : String) : Flow<YRApiResponse<MutableList<GatewayDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_DEVICE_CHILD_LIST)
    fun getSubDeviceList(@Query("uuid") deviceId : String) : Flow<YRApiResponse<MutableList<BindDevice>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_DELETE_GATEWAY)
    fun deleteGatewayDevice(@Body body : DeleteDeviceBindingBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @POST(IpcDeviceApiConstant.API_PATH_OF_DEVICE_DELETE_DEVICE)
    fun deleteDevice(@Body body : DeleteDeviceBindingBody) : Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_PACK_INFO)
    fun getDevicePageInfo(@Query("uuid") uuid: String): Flow<YRApiResponse<PackageInfoResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_PACK_SHARING_INFO)
    fun getSharingDevicePageInfo(@Query("uuid") uuid: String): Flow<YRApiResponse<PackageInfoResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_PACK_ALL_INFO)
    fun getAllDevicePageInfo(): Flow<YRApiResponse<List<PackageInfoResult>>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_PACK_ORDER_CANCEL)
    fun cancelOrder(@Query("uuid") uuid: String): Flow<YRApiResponse<Any>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_VERSION_HARD)
    fun getFirmwareVersion(@Query("model") model: String): Flow<YRApiResponse<FirmwareVersion>>

    @Headers(HEADER_BASE_URL_NAME_OF_WEB, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_VERSION_GATEWAY_CHILD)
    fun getGatewayAndChildFirmwareVersion(@Query("gateway") gateway: String, @Query("child") child: String): Flow<YRApiResponse<GatewayAndChildFirmwareVersion>>

    @Headers(HEADER_BASE_URL_NAME_OF_S3, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_FETCH_CLOUD_VIDEO_LIST)
    fun getCloudVideoFileList(
        @Query("deviceid") deviceid: String,
        @Query("userid") userId: String,
        @Query("filedate") fileDate: String,
        @Query("bindtype") bindType: Int
    ): Flow<YRApiResponse<AwsFileListResult>>

    @Headers(HEADER_BASE_URL_NAME_OF_S3, HEADER_API_SIGN_TYPE_SECRET)
    @GET(IpcDeviceApiConstant.API_PATH_OF_FETCH_DOORBELL_CLOUD_VIDEO_LIST)
    fun getDoorbellCloudVideoFileList(
        @Query("deviceid") deviceid: String,
        @Query("userid") userId: String,
        @Query("filedate") fileDate: String,
        @Query("bindtype") bindType: Int
    ): Flow<YRApiResponse<AwsFileListResult>>

}