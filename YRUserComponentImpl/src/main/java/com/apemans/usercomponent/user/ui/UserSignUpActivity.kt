package com.apemans.usercomponent.user.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.utils.LanguageUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.dylanc.longan.intentExtras
import com.dylanc.longan.toast
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.datastore.db.entity.UserEntity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.messagepush.push.um.UmPushClient
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.router.startRouterActivity
import com.apemans.usercomponent.R
import com.apemans.usercomponent.baseinfo.graphics.DisplayUtil
import com.apemans.usercomponent.databinding.UserActivitySetPasswordBinding
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.user.util.UserHelper
import com.apemans.usercomponent.viewModel.UserSetPasswordViewModel
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.dylanc.longan.appVersionCode
import com.dylanc.longan.appVersionName
import kotlinx.coroutines.launch

class UserSignUpActivity : UserBaseActivity<UserActivitySetPasswordBinding>() {

    val account : String by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT, "")
    private val countryCodeString : String by intentExtras(ConstantValue.INTENT_KEY_COUNTRY_CODE, "")
    private val verifyCode : String by intentExtras(ConstantValue.INTENT_KEY_VERIFY_CODE, "")
    private lateinit var viewModel : UserSetPasswordViewModel
    private var btnSignUpAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.set_psd_title)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        setupInputFrameView()
        onClickEvent()
        checkBtnEnabled()
    }

    private fun initData() {
        setBtnSignUpAnimator()
        viewModel = registerViewModule(UserSetPasswordViewModel::class.java)
    }

    private fun setupInputFrameView() {
        binding.ipvCreatePsd.apply {
            setTitle(resources.getString(R.string.key_mine_NewPassword))
            setHint("input password")
            addOnTextChanged { _, _ -> checkBtnEnabled() }
        }
    }

    private fun onClickEvent() {
        binding.btnDone.setOnClickListener {
            lifecycleScope.launch { onClickSunmit() }
        }
    }

    @SuppressLint("WrongConstant")
    private suspend fun onClickSunmit() {
        if (binding.ipvCreatePsd.getEditText().text.toString().length < 6 || binding.ipvConfirmPsd.getEditText().text.toString().length < 6) {
            toast(resources.getString(R.string.account_psd_not_less_six))
        } else if (binding.ipvCreatePsd.getEditText().text.toString().length > 12 || binding.ipvConfirmPsd.getEditText().text.toString().length > 12) {
            toast(resources.getString(R.string.account_psd_overflow))
        } else if (binding.ipvCreatePsd.getEditText().text.toString() != binding.ipvConfirmPsd.getEditText().text.toString()) {
            toast(resources.getString(R.string.account_change_psd_new_confirm_not_same))
        } else {
            signUpBySDK()
        }
    }

    private suspend fun signUpBySDK() {
        showBtnSignUpLoading()
        viewModel.signUpBySDK(account, binding.ipvCreatePsd.getEditText().text.toString(), countryCodeString, verifyCode)
            .observe(this) {
                if (it == HttpCode.SUCCESS_CODE) {
                    // 保存注册账号信息到本地数据库
                    val userEntity = UserEntity().apply {
                        this.userAccount = account
                        this.countryCode = countryCodeString
                    }
                    lifecycleScope.launch {
                        SmartDatabase.getDatabase().userDao().insertUser(userEntity)
                    }
                    notifySignUpResult(ConstantValue.SUCCESS)
                }
            }
    }

    private fun notifySignUpResult(result: String) {
        if (isDestroyed) {
            return
        }
        hideBtnSignUpLoading()
        if (ConstantValue.SUCCESS == result) {
            // 涂鸦
            tuyaOnLogin()
            reportUserInfo()
            // 去首页
            YRCXSDKDataManager.userAccount = account // 模拟保存登录数据到本地
            YRCXSDKDataManager.firstLogin = false
            toast("Login success")
            var hashMap = HashMap<String,Any?>()
            hashMap["login"] = this
            YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
            finish()
//            startRouterActivity(ACTIVITY_PATH_MAIN) { finish() }
            //loginSuccess() // 登录成功后需要调用该方法才能使登录拦截的逻辑形成闭环，不要再调用 finish() 方法
        } else {
            // 标记账号登出
        }
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

    private fun checkBtnEnabled() {
        binding.btnDone.isEnabled = true
        binding.btnDone.setTextColor(resources.getColor(R.color.black_010C11));
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun showBtnSignUpLoading() {
        binding.btnDone.text = ""
        binding.ivSignUpLoading.visibility = View.VISIBLE
        btnSignUpAnimator!!.start()
    }

    private fun hideBtnSignUpLoading() {
        binding.btnDone.setText(R.string.submit)
        binding.ivSignUpLoading.visibility = View.GONE
        btnSignUpAnimator!!.pause()
    }

    private fun setBtnSignUpAnimator() {
        btnSignUpAnimator = ObjectAnimator.ofFloat(binding.ivSignUpLoading, "Rotation", 0f, 360f)
        btnSignUpAnimator?.duration = 2000
        btnSignUpAnimator?.repeatCount = -1
    }

    companion object {
        fun start(from : Context, account : String, countryCode : String,registerCode : String) {
            var intent = Intent(from, UserSignUpActivity::class.java)
            intent.putExtra(ConstantValue.INTENT_KEY_ACCOUNT, account)
            intent.putExtra(ConstantValue.INTENT_KEY_COUNTRY_CODE, countryCode)
            intent.putExtra(ConstantValue.INTENT_KEY_VERIFY_CODE, registerCode)
            from.startActivity(intent)
        }
    }
}