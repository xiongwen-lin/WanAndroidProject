package com.apemans.usercomponent.repository

import android.net.Uri
import com.apemans.business.apisdk.client.configure.YRApiConfigure
import com.dylanc.longan.application
import com.dylanc.longan.context
import com.apemans.userapi.request.CreateFeedbackBody
import com.apemans.usercomponent.baseinfo.file.FileUtil
import com.apemans.usercomponent.baseinfo.graphics.BitmapUtil
import com.apemans.usercomponent.baseinfo.time.DateTimeUtil
import com.apemans.usercomponent.mine.entry.FeedbackResult
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.userapi.UserApiHelper
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

import java.io.File
import java.lang.StringBuilder

object FeedbackRepository {

     fun postFeedback(typeId: Int, productId: Int, email: String, content: String, images: String?) =
         UserApiHelper.userGlobalApi.checkFeedback()
            .transform {
                val createFeedbackBody  = CreateFeedbackBody(typeId, productId, email, content, images)
                emit(
                    UserApiHelper.userGlobalApi.createFeedback(createFeedbackBody).single()
                )
            }.flowOn(Dispatchers.IO)

    fun upLoadPicture(userId: String, userName: String, picPath: String) : Flow<FeedbackResult>{
        val fileNameSb = StringBuilder()
        val fileName = DateTimeUtil.getOnlyTimeId() + ".jpg"
        fileNameSb.append(YRCXSDKDataManager.getAppid())
        fileNameSb.append("/")
        fileNameSb.append(userId)
        fileNameSb.append("/")
        fileNameSb.append(fileName)
        val compressPicPathSb = StringBuilder()

        val compressPicPath = BitmapUtil.compressImage(picPath,
            FileUtil.getStorageTempFolder(application.context),
            "", 480, 800)
        compressPicPathSb.append(compressPicPath)
        val file = File(compressPicPath)
        return UserApiHelper.userGlobalApi.getUploadFeedbackFilePreSign(userId, userName, fileName, file.length().toInt())
            .map {
                val userNameParam = "username=$userName"
                val uploadFile = File(compressPicPathSb.toString())
                if (uploadFile.exists()) {
                    val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), uploadFile)
                    UserApiHelper.userGlobalApi.upLoadFileToCloud(it.data.url, "image/jpeg", userNameParam, requestBody)
                        ?.execute()
                    FileUtil.deleteFile(uploadFile)
                }
                var result = FeedbackResult()
                result.resultStr = ConstantValue.SUCCESS
                result.picPath = picPath
                result.fileNameSb = fileNameSb.toString()
                result
            }
            .flowOn(Dispatchers.IO)
    }

    fun copyFileToPrivateStorage(srcUri: Uri, targetPath: String) = flow {
        FileUtil.copyFile(application.context, srcUri, targetPath)
        var result = FeedbackResult()
        result.resultStr = ConstantValue.SUCCESS
        result.picPath = targetPath
        result
        emit(result)
    }
}