package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.usercomponent.repository.UserSetPasswordRepository
import com.apemans.usercomponent.repository.UserSigninRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class UserSetPasswordViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    fun updateUserInfo(oldPassword: String, newPassword: String) : LiveData<Int> {
        return UserSetPasswordRepository.updateUserInfo(oldPassword, newPassword)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    suspend fun signUpBySDK(account: String, psd: String, countryCode : String, verifyCode: String) : LiveData<Int> {
        return UserSetPasswordRepository.signUpBySDK(account, psd, countryCode, verifyCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {}
            .asLiveData()
    }

    suspend fun resetPasswordBySDK(account : String, password : String, verifyCode : String, countryCode : String) : LiveData<Int> {
        return UserSetPasswordRepository.resetPasswordBySDK(account, password, verifyCode, countryCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    suspend fun sendRegisterVerifyCode(sendCodeType : Int, account : String, countryCode : String) : LiveData<Int> {
        return UserSigninRepository.sendRegisterVerifyCode(account, countryCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    fun reportUserInfo(account : String , password : String , zone : Float, country : String ,
                       type : Int , nickname : String , photo : String , phoneCode : String ,
                       deviceType :Int, pushType:Int, pushToken: String, appVersion : String,
                       appVersionCode : String, phoneModel : String, phoneBrand : String,
                       phoneVersion : String, phoneScreen : String, language : String, packageName : String) : LiveData<Int>{
        return UserSigninRepository.reportUserInfo(account, password, zone, country, type,
            nickname, photo, phoneCode, deviceType, pushType, pushToken, appVersion, appVersionCode,
            phoneModel, phoneBrand, phoneVersion, phoneScreen, language, packageName)
            .onStart {  }
            .onCompletion {  }
            .catch {  }
            .asLiveData()
    }
}