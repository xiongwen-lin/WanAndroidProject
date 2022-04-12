package com.apemans.usercomponent.user.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.utils.LanguageUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.dylanc.longan.intentExtras
import com.dylanc.longan.toast
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.messagepush.push.um.UmPushClient
import com.apemans.userapi.KEY_ROUTER_PATH
import com.apemans.userapi.loginSuccess
import com.apemans.usercomponent.R
import com.apemans.usercomponent.baseinfo.graphics.DisplayUtil
import com.apemans.usercomponent.databinding.UserActivityTwoAuthLoginBinding
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.user.util.UserHelper
import com.apemans.usercomponent.viewModel.UserTwoAuthViewModel
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.dylanc.longan.appVersionCode
import com.dylanc.longan.appVersionName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class UserTwoAuthLoginActivity : UserBaseActivity<UserActivityTwoAuthLoginBinding>() {

    val account : String by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT, "")
    val password : String by intentExtras(ConstantValue.INTENT_KEY_PSD, "")
    val countryCode : String by intentExtras(ConstantValue.INTENT_KEY_COUNTRY_CODE, "")
    val isSendCode : Boolean by intentExtras(ConstantValue.INTENT_KEY_DATA_PARAM, false)
    val isClearTask : Boolean by intentExtras(ConstantValue.INTENT_KEY_DATA_PARAM_1, false)
    val path : String by intentExtras(KEY_ROUTER_PATH, "")

    private lateinit var viewModel : UserTwoAuthViewModel
    private var mCheckingLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        viewModel = registerViewModule(UserTwoAuthViewModel::class.java)
        mCheckingLogin = false
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.two_auth_login_title)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        binding.btnTwoAuthLoginCheck.setOnClickListener {
            onClickTwoAuthLoginCheck()
        }
        binding.tvTwoAuthLoginAccount.text = account
        setInputView()
        lifecycleScope.launch { onStartCount() }
    }

    private fun setInputView() {
        binding.etTwoAuthLoginCode.apply {
            setTitle(resources.getString(R.string.input_verify_code_enter_code))
            setHint("input code")
            addOnTextChanged { _, _ -> checkBtnEnable() }
            addOnRightTextClick { _, _ ->
                lifecycleScope.launch { onStartCount() }
            }
        }
        checkBtnEnable()
    }

    private fun checkBtnEnable() {
        if (!TextUtils.isEmpty(binding.etTwoAuthLoginCode.getEditText().text)) {
            binding.btnTwoAuthLoginCheck.isEnabled = true
            binding.btnTwoAuthLoginCheck.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnTwoAuthLoginCheck.isEnabled = false
            binding.btnTwoAuthLoginCheck.setTextColor(resources.getColor(R.color.theme_white))
        }
    }

    private fun onClickTwoAuthLoginCheck() {
        if (mCheckingLogin) {
            return
        }
        val code = binding.etTwoAuthLoginCode.getEditText().text.toString()
        if (!TextUtils.isEmpty(account) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(countryCode) &&
            !TextUtils.isEmpty(code)) {
            mCheckingLogin = true
            lifecycleScope.launch {
                viewModel.checkAndLogin(account, password, countryCode, code)
                    .observe(this@UserTwoAuthLoginActivity){
                        onCheckAndLoginResult(it)
                    }
            }
        }
    }

    private fun setLoginSuccessResult() {
        val data = Intent()
        data.putExtra(ConstantValue.INTENT_KEY_DATA_PARAM, true)
        setResult(RESULT_OK, data)
    }

    private fun goBackToHome() {
        YRCXSDKDataManager.userAccount = account // 模拟保存登录数据到本地
        YRCXSDKDataManager.firstLogin = false
        // 记录其他模块需要的信息
        initGlobalData()
        tuyaOnLogin()
        reportUserInfo()
        //登陆成功,去首页
        toast("Login success")
        loginSuccess() // 登录成功后需要调用该方法才能使登录拦截的逻辑形成闭环，不要再调用 finish() 方法
        var hashMap = HashMap<String,Any?>()
        hashMap["login"] = this
        YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
        finish()
//        startRouterActivity(ACTIVITY_PATH_MAIN)
        //finish()
    }

    private fun tuyaOnLogin() {
        YRMiddleServiceManager.requestAsync("yrcx://yrtuya/onlogin", emptyMap()) {
        }
    }

    private fun reportUserInfo() {
        val nickname = ""
        val photoUrl = ""
        val appVersion = "$appVersionName($appVersionCode)"
        val screenSize = "${DisplayUtil.SCREEN_WIDTH_PX.toString()}*${DisplayUtil.SCREEN_HIGHT_PX}"
        val language = LanguageUtil.getLocal(this).language
        YRCXSDKDataManager.userAccount?.let {
            YRCXSDKDataManager.userCountryCode?.let { it1 ->
                viewModel.reportUserInfo(
                    it, YRCXSDKDataManager.userPassword.toString(),
                    UserHelper.getCurrentTimeZone().toFloat(), it1, 1,
                    nickname, photoUrl, UserHelper.createPhoneId(this),1,
                    UmPushClient.getPushType(), UmPushClient.getToken(),
                    appVersion, appVersionCode.toString(), Build.MODEL, Build.BRAND,
                    Build.VERSION.SDK_INT.toString(), screenSize,
                    language, packageName).observe(this){
                }
            }
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun onCheckAndLoginResult(response : Int) {
        if (isDestroyed) {
            return
        }
        mCheckingLogin = false
        if (response == HttpCode.SUCCESS_CODE) {
            setLoginSuccessResult()
            goBackToHome()
        } else {
            var errorMsg = getString(R.string.get_fail)
            if (response == HttpCode.CODE_1051) {
                errorMsg = getString(R.string.sign_in_verify_code_incorrect)
            }
            //MyAccountHelper.getInstance().logout()
            toast(errorMsg)
            dismissLoading()
        }
    }

    private suspend fun onStartCount() {
        startVerifyCodeCounter()
            .collect {
                if (ConstantValue.SUCCESS == it) {
                    binding.etTwoAuthLoginCode.setRightText(resources.getString(R.string.sign_in_resend))
                } else {
                    binding.etTwoAuthLoginCode.setRightText("${it}s")
                }
            }
    }

    private fun startVerifyCodeCounter() = flow {
        var limitTime = 180
        while(limitTime > 0) {
            emit(limitTime.toString())
            delay(1000)
            limitTime--
        }
        if (limitTime <= 0) {
            emit(ConstantValue.SUCCESS)
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        fun start(from : Activity, requestCode : Int, account : String, password : String,
                  countryCode : String, isSendCode : Boolean, isClearTask : Boolean) {
            val intent = Intent(from, UserTwoAuthLoginActivity::class.java)
            intent.putExtra(ConstantValue.INTENT_KEY_ACCOUNT, account)
            intent.putExtra(ConstantValue.INTENT_KEY_PSD, password)
            intent.putExtra(ConstantValue.INTENT_KEY_COUNTRY_CODE, countryCode)
            intent.putExtra(ConstantValue.INTENT_KEY_DATA_PARAM, isSendCode)
            intent.putExtra(ConstantValue.INTENT_KEY_DATA_PARAM_1, isClearTask)
            intent.putExtra(KEY_ROUTER_PATH, from.intent.getStringExtra(KEY_ROUTER_PATH))
            from.startActivityForResult(intent, requestCode)
        }
    }
}