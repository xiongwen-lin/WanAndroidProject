package com.apemans.custom.viewModel

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.dylanc.longan.logError
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.FeedbackResult
import com.apemans.custom.repository.CustomerRepository
import com.apemans.custom.util.FileUtil
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.logger.YRLog
import com.dylanc.longan.application
import com.dylanc.longan.context


import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * feedback viewModel
 */
class FeedbackViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    fun postFeedback(typeId: Int, productId: Int, email: String, content: String, images: String?): LiveData<YRApiResponse<Any>> {
        val createFeedback = CreateFeedbackBody(
            typeId,
            productId,
            email,
            content,
            images
        )
        return CustomerRepository.createFeedback(createFeedback)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch { e ->
                YRLog.e { "feedbackCheck--error $e" }
            }
            .asLiveData()

    }

    fun upLoadPicture(userId: String, userName: String, picPath: String) : LiveData<FeedbackResult>{
        return CustomerRepository.upLoadPicture(userId, userName, picPath)
            .catch {
                logError(it.message)
            }
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .asLiveData()
    }

    fun copyFileToPrivateStorage(srcUri: Uri, targetPath: String) : FeedbackResult{
        val  copyFileResult= FileUtil.copyFile(application.context, srcUri, targetPath)
        var result = FeedbackResult()
        result.resultStr = if (!TextUtils.isEmpty(copyFileResult)) MediaConstant.SUCCESS else MediaConstant.ERROR
        result.picPath = targetPath
        return  result

    }
}