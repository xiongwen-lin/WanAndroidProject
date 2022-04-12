package com.apemans.custom.repository

import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.*
import com.apemans.custom.util.QTimeUtils
import com.apemans.custom.webapi.CustomApiHelper
import com.apemans.customserviceapi.services.CustomService
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.customserviceapi.webapi.MessageData
import com.apemans.logger.YRLog
import com.apemans.router.routerServices
import com.apemans.base.utils.CollectionUtil
import com.apemans.base.utils.DateTimeUtil

import com.apemans.custom.util.BitmapUtil
import com.apemans.custom.util.FileUtil
import com.apemans.userapi.enrty.UserInfo
import com.dylanc.longan.application
import com.dylanc.longan.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

object CustomerRepository {
    private val customService: CustomService by routerServices()

    /**
     * 获取客服回复信息
     */
    fun getMessageList(rows: Int, time: Int, type: Int) =
        CustomApiHelper.customApi.getMessageList(rows, time, type)
            .catch { e -> YRLog.e { "getMessageLists()--error $e" } }.map {
            val backStateSysList = mutableListOf<RecordDataBean>()
            for (sysMsg in CollectionUtil.safeFor(it.data)) {
                backStateSysList.add(messageDataToRecordData(sysMsg))
            }
            backStateSysList
        }.flowOn(Dispatchers.IO)

    private fun messageDataToRecordData(messageData: MessageData): RecordDataBean {
        return RecordDataBean(
            0,
            "",
            UserType.SERVICE,
            messageData.msg.toString(),
            ContentType.TXT,
            QTimeUtils.getToday(),
            Status.SUCCESS
        )
    }

    /**
     * 检查是否频发消息
     */
    fun checkFeedback() =
        customService.checkFeedback()
            .map {
                it
            }
            .flowOn(Dispatchers.IO)

    /**
     * 发送消息
     */
    fun createFeedback(body: CreateFeedbackBody) = customService.createFeedback(body)
        .map { it }
        .catch { e -> YRLog.e { "createFeedback()--error $e" } }
        .flowOn(Dispatchers.IO)



    fun upLoadPicture(userId: String, userName: String, picPath: String): Flow<FeedbackResult> {
        val fileNameSb = StringBuilder()
        val fileName = DateTimeUtil.getOnlyTimeId() + ".jpg"
        fileNameSb.append("")
        fileNameSb.append("/")
        fileNameSb.append(userId)
        fileNameSb.append("/")
        fileNameSb.append(fileName)
        val compressPicPathSb = StringBuilder()

        val compressPicPath = BitmapUtil.compressImage(
            picPath,
            FileUtil.getStorageTempFolder(application.context),
            "", 480, 800
        )
        compressPicPathSb.append(compressPicPath)
        val file = File(compressPicPath)
        return customService
            .getUploadFeedbackFilePreSign(userId, userName, fileName, file.length().toInt())
            .map {
                val userNameParam = "username=$userName"
                val uploadFile = File(compressPicPathSb.toString())
                if (uploadFile.exists()) {
                    val requestBody =
                        RequestBody.create("image/jpeg".toMediaTypeOrNull(), uploadFile)
                    customService.upLoadFileToCloud(
                        it.data.url,
                        "image/jpeg",
                        userNameParam,
                        requestBody
                    )
                        ?.execute()
                    FileUtil.deleteFile(uploadFile)
                }
                var result = FeedbackResult()
                result.resultStr = MediaConstant.SUCCESS
                result.picPath = picPath
                result.fileNameSb = fileNameSb.toString()
                result
            }
            .flowOn(Dispatchers.IO)
    }

}