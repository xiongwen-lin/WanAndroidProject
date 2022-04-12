package com.apemans.datamanager.repository

import com.apemans.datamanager.bean.YRPlatformDevice
import com.apemans.datamanager.utils.YRPlatformHelper
import com.apemans.datamanager.webapi.YRPlatformApiHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/1/27 5:03 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object YRPlatformRepository {

    private val localDataSource = YRPlatformLocalDataSource()
    private val remoteDataSource = YRPlatformRemoteDataSource()

    suspend fun queryDevice() =
        remoteDataSource.getRemoteDeviceList(1, 100)
            .catch {
                emit(null)
            }
            .single()


//    fun queryDevice(uid: String, category: String) = flow<List<YRPlatformDevice>> {
//
//        val response = remoteDataSource.getRemoteDeviceList(1, 100)
//            .catch {
//                emit(null)
//            }
//            .single()
//
//        val responseSuccess = response?.code == HttpCode.SUCCESS_CODE
//
//        val result = mutableListOf<YRPlatformDevice>()
//        if (responseSuccess) {
//            val devices = response?.data?.data?.map {
//                YRPlatformHelper.convertPlatformDevice(it)
//            }.orEmpty()
//            result.addAll(devices)
//        }
//
//        emit(result)
//
//    }

    fun queryNetSpotDeviceInfo(): YRPlatformDevice {
        return YRPlatformHelper.convertPlatformNetSpotDevice()
    }
}

class YRPlatformLocalDataSource {

    fun getLocalDeviceList(uid: String, category: String) = flow<List<YRPlatformDevice>?> {
        emit(null)
    }

}

class YRPlatformRemoteDataSource {

    fun getRemoteDeviceList(page: Int, pageCount: Int) = YRPlatformApiHelper.platformApi.getBindDevices(page, pageCount)
}