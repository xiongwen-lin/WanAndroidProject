package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.repository.UserForgotPasswordRepository
import com.apemans.usercomponent.repository.UserSigninRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class UserForgotPasswordViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    suspend fun sendVerifyCode(account : String, countryCode : String, verifyType : Int) : LiveData<Int> {
        return UserForgotPasswordRepository.sendVerifyCode(account, countryCode, verifyType)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    suspend fun checkVerifyCode(account: String, verifycode : String, countryCode : String, type : Int) : LiveData<Int> {
        return UserForgotPasswordRepository.checkVerifyCode(account, verifycode, countryCode, type)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    fun checkAccountSourceForgetPassword(account : String) : LiveData<YRApiResponse<GlobalUrlResult>> {
        return UserForgotPasswordRepository.checkAccountSourceForgetPassword(account)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }
}