package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.logger.YRLog
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.repository.UserSigninRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class UserSigninViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    val limitTime = UserSigninRepository.mVerifyCodeLimitTime

    fun signIn(account : String, psd : String, countryCode : String) : LiveData<Int> {
        return UserSigninRepository.signIn(account, psd, countryCode)
            .onStart { showLoading() }
            .onCompletion { dismissLoading() }
            .catch {
                YRLog.d {
                    "#####################====: ${it.message}"
                }
            }
            .asLiveData()
    }

    fun sendRegisterVerifyCode(account : String, countryCode : String) : LiveData<Int> {
        return UserSigninRepository.sendRegisterVerifyCode(account, countryCode)
            .onStart { showLoading() }
            .onCompletion {
                dismissLoading()
            }
            .catch { dismissLoading() }
            .asLiveData()
    }

    fun checkRegisterVerifyCodeBySDK(account : String, verifyCode : String, countryCode : String) : LiveData<Int> {
        return UserSigninRepository.checkRegisterVerifyCodeBySDK(account, verifyCode, countryCode)
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

    suspend fun checkAccountSourceForSignIn(account : String, psd : String) : LiveData<YRApiResponse<GlobalUrlResult>> {
        return UserSigninRepository.checkAccountSourceForSignIn(account, psd)
            .onStart { /*showLoading()*/ }
            .onCompletion { /*dismissLoading()*/ }
            .catch {  }
            .asLiveData()
    }

    fun showLoadingView () {
        showLoading()
    }

    fun dismissLoadingView() {
        dismissLoading()
    }
}