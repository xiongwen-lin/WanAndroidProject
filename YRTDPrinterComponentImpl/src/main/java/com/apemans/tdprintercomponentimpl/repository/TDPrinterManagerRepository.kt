package com.apemans.tdprintercomponentimpl.repository

import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.logger.YRLog
import com.apemans.tdprintercomponentimpl.bean.BasePlatformDevice
import com.apemans.tdprintercomponentimpl.utils.YRTDPrinterHelper
import com.apemans.tdprintercomponentimpl.webapi.*
import kotlinx.coroutines.flow.*

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/11 10:20 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object TDPrinterManagerRepository {

    private val remoteDataSource = TDPrinterManagerRemoteDataSource()
    private val localDataSource = TDPrinterManagerLocalDataSource()

   /* fun getDeviceInfo(deviceId: String) = flow<BasePlatformDevice?> {
        var result = remoteDataSource.getRemoteDeviceInfo(deviceId)
            .transform {
                var platformDevice: BasePlatformDevice? = null
                if (it?.code == HttpCode.SUCCESS_CODE) {
                    platformDevice = it?.data?.let { bindDevice ->
                        YRTDPrinterHelper.convertPlatformDevice(bindDevice)
                    }
                }
                emit(platformDevice)
            }
            .catch {
                emit(null)
            }
            .single()
        emit(result)
    }
*/
   fun getDeviceInfo(deviceId: String) = flow<BindDevice?> {
       var result = remoteDataSource.getRemoteDeviceInfo(deviceId)
           .transform {
               var platformDevice: BasePlatformDevice? = null
               if (it?.code == HttpCode.SUCCESS_CODE) {
                   platformDevice = it?.data?.let { bindDevice ->
                       YRTDPrinterHelper.convertPlatformDevice(bindDevice)
                   }
               }
               emit(it?.data)
           }
           .catch {
               emit(null)
           }
           .single()
       emit(result)
   }

    fun shareDevice(deviceId: String, sharers: List<String>): Flow<Map<String, YRApiResponse<ShareDeviceResult?>?>> = remoteDataSource.shareDevice(deviceId, sharers)

    fun deleteSharing(id: Int): Flow<YRApiResponse<Any?>?> = remoteDataSource.deleteSharing(id)

    fun getDeviceRelation(deviceId: String, page : Int,  count : Int): Flow<YRApiResponse<DeviceRelationListResult?>?> = remoteDataSource.getDeviceRelation(deviceId, page, count)


}

class TDPrinterManagerLocalDataSource {}

class TDPrinterManagerRemoteDataSource {

    fun getRemoteDeviceInfo(deviceId: String) = TDPrinterApiHelper.tdPrinterApi.getDeviceInfo(deviceId)

    fun shareDevice(deviceId: String, sharers: List<String>) = flow {
        val responses = mutableMapOf<String, YRApiResponse<ShareDeviceResult?>?>()
        sharers.forEach {
            var response = TDPrinterApiHelper.tdPrinterApi.sendDeviceSharing(SharingDeviceBody(it, deviceId)).single()
            if (it.isNotEmpty()) {
                responses[it] = response
            }
        }
        emit(responses)
    }

    fun deleteSharing(id: Int): Flow<YRApiResponse<Any?>?> {
        return TDPrinterApiHelper.tdPrinterApi.deleteSharingDevice(id)
    }

    fun getDeviceRelation(deviceId: String, page : Int,  count : Int): Flow<YRApiResponse<DeviceRelationListResult?>?> {
        return TDPrinterApiHelper.tdPrinterApi.getDeviceRelationList(deviceId, page, count)
    }

    fun updateDeviceName(deviceId: String, name: String): Flow<YRApiResponse<Any?>?> {
        var body = UpdateDeviceNameBody(deviceId, name)
        return TDPrinterApiHelper.tdPrinterApi.updateDeviceName(body)
    }

}