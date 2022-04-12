package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.usercomponent.repository.UserSigninRepository
import com.apemans.usercomponent.repository.UserTwoAuthRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class UserTwoAuthViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    suspend fun sendTwoAuthCode (account : String, countryCode : String) : LiveData<Int> {
        return UserTwoAuthRepository.sendTwoAuthCode(account, countryCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {  }
            .asLiveData()
    }

    suspend fun checkAndLogin(account : String, password : String, countryCode : String, verifyCode : String) : LiveData<Int> {
        return UserTwoAuthRepository.checkAndLogin(account, password, countryCode, verifyCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch { }
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