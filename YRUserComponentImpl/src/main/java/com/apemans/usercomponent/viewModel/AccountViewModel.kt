package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dylanc.longan.logError
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.userapi.request.UserInfoResult
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.repository.AccountRepository
import kotlinx.coroutines.flow.*
import java.io.File

class AccountViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {


    fun loadUserInfo() : LiveData<YRApiResponse<UserInfoResult>>{
        return AccountRepository.loadUserInfo()
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    fun logout() : LiveData<Int>{
        return AccountRepository.logout()
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    fun setPortrait(photo: File) : LiveData<String>{
        return AccountRepository.setPortrait(photo)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch { logError(it.message) }
            .asLiveData()
    }
}