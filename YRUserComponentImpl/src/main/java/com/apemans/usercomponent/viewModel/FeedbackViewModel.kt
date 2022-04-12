package com.apemans.usercomponent.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dylanc.longan.logError
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.mine.entry.FeedbackResult
import com.apemans.usercomponent.repository.FeedbackRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * feedback viewModel
 */
class FeedbackViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    fun postFeedback(typeId: Int, productId: Int, email: String, content: String, images: String?): LiveData<YRApiResponse<Any>> {
        return FeedbackRepository.postFeedback(typeId, productId, email, content, images)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {}
            .asLiveData()
    }

    fun upLoadPicture(userId: String, userName: String, picPath: String) : LiveData<FeedbackResult>{
        return FeedbackRepository.upLoadPicture(userId, userName, picPath)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {
                logError(it.message)
            }
            .asLiveData()
    }

    fun copyFileToPrivateStorage(srcUri: Uri, targetPath: String) : LiveData<FeedbackResult>{
        return FeedbackRepository.copyFileToPrivateStorage(srcUri, targetPath)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch { }
            .asLiveData()
    }
}