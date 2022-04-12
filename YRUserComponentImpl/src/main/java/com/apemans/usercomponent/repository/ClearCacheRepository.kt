package com.apemans.usercomponent.repository

import com.apemans.usercomponent.baseinfo.file.FileUtil
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object ClearCacheRepository {

    fun getCacheSize() = flow {
        val cacheSize = FileUtil.getTotalCacheSize(application.context, YRCXSDKDataManager.userAccount)
        emit(cacheSize)
    }.flowOn(Dispatchers.IO)

    fun clearCache() = flow {
        FileUtil.clearAllCache(application.context, YRCXSDKDataManager.userAccount)
        emit(true)
    }.flowOn(Dispatchers.IO)


}