package com.apemans.platformcomponentimpl.repository

import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.platformcomponentimpl.bean.YRPlatformDevice
import com.apemans.platformcomponentimpl.utils.YRPlatformHelper
import com.apemans.platformcomponentimpl.webapi.YRPlatformApiHelper
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

    fun queryDevice(uid: String, category: String) = flow<List<YRPlatformDevice>> {

        var response = remoteDataSource.getRemoteDeviceList(1, 100)
            .catch {
                emit(null)
            }
            .single()

        var responseSuccess = response?.let {
            it.code == HttpCode.SUCCESS_CODE
        } ?: false

        val result = mutableListOf<YRPlatformDevice>()
        if (responseSuccess) {
            val devices = response?.data?.data?.map {
                YRPlatformHelper.convertPlatformDevice(it)
            }.orEmpty()
            result.addAll(devices)
        }

        emit(result)

    }

    fun queryNetSpotDeviceInfo() : YRPlatformDevice {
        return YRPlatformHelper.convertPlatformNetSpotDevice()
    }
}

class YRPlatformLocalDataSource {

    fun getLocalDeviceList(uid: String, category: String) = flow<List<YRPlatformDevice>?> {
        emit(null)
    }

}

class YRPlatformRemoteDataSource {

    fun getRemoteDeviceList(page : Int, pageCount : Int) = YRPlatformApiHelper.platformApi.getBindDevices(page, pageCount)
}