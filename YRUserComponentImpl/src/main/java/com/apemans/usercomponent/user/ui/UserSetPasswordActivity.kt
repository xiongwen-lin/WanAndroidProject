package com.apemans.usercomponent.user.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.utils.LanguageUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.messagepush.push.um.UmPushClient
import com.apemans.userapi.KEY_ROUTER_PATH
import com.apemans.userapi.loginSuccess
import com.apemans.userapi.paths.ACTIVITY_PATH_UPDATA_PASSWARD
import com.apemans.usercomponent.R
import com.apemans.usercomponent.baseinfo.graphics.DisplayUtil
import com.apemans.usercomponent.databinding.UserActivitySetPasswordBinding
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.user.util.UserHelper
import kotlinx.coroutines.launch
import com.apemans.usercomponent.viewModel.UserSetPasswordViewModel
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.dylanc.longan.*

/**
 * 忘记密码 -- 设置密码
 */
@Route(path = ACTIVITY_PATH_UPDATA_PASSWARD)
class UserSetPasswordActivity : UserBaseActivity<UserActivitySetPasswordBinding>() {
    val account : String by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT, "")
    private val verifyCode : String by intentExtras(ConstantValue.INTENT_KEY_VERIFY_CODE, "")
    val countryCode : String by intentExtras(ConstantValue.INTENT_KEY_COUNTRY_CODE, "")
    private val verifyType : Int by intentExtras(ConstantValue.INTENT_KEY_VERIFY_TYPE, -1)
    private lateinit var viewModel : UserSetPasswordViewModel
    private var btnDoneAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        setBtnDoneAnimator()
    }

    fun initView() {
        binding.btnDone.setOnClickListener { onViewClicked() }
        setToolbar {
            title = resources.getString(R.string.set_psd_reset_title)
            titleTextColor = resources.getColor(R.color.black_010C11)
            binding.btnDone.text = resources.getString(R.string.set_psd_save_and_sign_in)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
            if (verifyType == ConstantValue.USER_REGISTER_VERIFY) {
                title = resources.getString(R.string.set_psd_title)
                binding.btnDone.text = resources.getString(R.string.sign_up)
            } else if (verifyType == -1) { binding.ipvOldPsd.visibility = View.VISIBLE }
        }
        setupInputFrameView()
    }

    private fun initData() {
        viewModel = registerViewModule(UserSetPasswordViewModel::class.java)
    }

    private fun  setupInputFrameView() {
        binding.ipvOldPsd.apply {
            title = resources.getString(R.string.account_old_password)
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        binding.ipvCreatePsd.apply {
            title = resources.getString(R.string.account_new_password)
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        binding.ipvConfirmPsd.apply {
            title = resources.getString(R.string.account_confirm_password)
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
    }

    private fun checkBtnEnable() {
        with(binding) {
            if (verifyType == -1) {
                if (!TextUtils.isEmpty(ipvCreatePsd.getEditText().text.toString()) && !TextUtils.isEmpty(ipvConfirmPsd.getEditText().text.toString())
                    && !TextUtils.isEmpty(ipvOldPsd.getEditText().text.toString())) {
                    btnDone.isEnabled = true
                    btnDone.setTextColor(resources.getColor(R.color.black_010C11))
                } else {
                    btnDone.isEnabled = false
                    btnDone.setTextColor(resources.getColor(R.color.theme_white))
                }
            } else {
                if (!TextUtils.isEmpty(ipvCreatePsd.getEditText().text.toString()) && !TextUtils.isEmpty(ipvConfirmPsd.getEditText().text.toString())) {
                    btnDone.isEnabled = true
                    btnDone.setTextColor(resources.getColor(R.color.black_010C11))
                } else {
                    btnDone.isEnabled = false
                    btnDone.setTextColor(resources.getColor(R.color.theme_white))
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun onViewClicked() {
        with(binding) {
            if (ipvOldPsd.getEditText().text.toString() != ""/*GlobalData.getInstance().password*/ && ipvOldPsd.visibility == View.VISIBLE) {
                toast(resources.getString(R.string.account_old_password_incorrect))
            } else if (ipvCreatePsd.getEditText().text.toString().length < 6 || ipvConfirmPsd.getEditText().text.toString().length < 6) {
                toast(resources.getString(R.string.account_psd_not_less_six))
            } else if (ipvCreatePsd.getEditText().text.toString().length > 12 || ipvConfirmPsd.getEditText().text.toString().length > 12) {
                toast(resources.getString(R.string.account_psd_overflow))
            } else if (ipvCreatePsd.getEditText().text.toString() != ipvConfirmPsd.getEditText().text.toString()) {
                toast(resources.getString(R.string.account_change_psd_new_confirm_not_same))
            } else if (ipvCreatePsd.getEditText().text.toString() == ipvOldPsd.getEditText().text.toString() && ipvOldPsd.visibility == View.VISIBLE) {
                toast(resources.getString(R.string.account_change_psd_old_new_same))
            } else {
                if (verifyType == -1) updateUserInfo()
                else {
                    if (verifyType == 2) resetPasswordBySDK()
                    else signUpBySDK()
                }
            }
        }
    }

    private fun updateUserInfo() {
        showBtnDoneLoading()
        viewModel.updateUserInfo(binding.ipvOldPsd.getEditText().text.toString(), binding.ipvConfirmPsd.getEditText().text.toString())
            .observe(this@UserSetPasswordActivity) {
                if (it == HttpCode.SUCCESS_CODE) {
                    val accountData = Bundle()
                    accountData.putString("password", binding.ipvConfirmPsd.getEditText().text.toString())
                    YRCXSDKDataManager.userPassword = binding.ipvConfirmPsd.getEditText().text.toString()
                }
                notifyUpdataPasswordResult(it)
            }
    }

    private fun resetPasswordBySDK() {
        showBtnDoneLoading()
        lifecycleScope.launch {
            viewModel.resetPasswordBySDK(account, binding.ipvConfirmPsd.getEditText().text.toString(),verifyCode, countryCode)
                .observe(this@UserSetPasswordActivity){
                    if (it == HttpCode.SUCCESS_CODE) {
                        notifyResetPsdResult(ConstantValue.SUCCESS)
                    } else {
                        notifyResetPsdResult("")
                    }
                }
        }
    }

    private fun signUpBySDK() {
        showBtnDoneLoading()
        lifecycleScope.launch {
            viewModel.signUpBySDK(account, binding.ipvConfirmPsd.getEditText().text.toString(), countryCode, verifyCode)
                .observe(this@UserSetPasswordActivity) {
                    if (it == HttpCode.SUCCESS_CODE) {
                        notifySignUpResult(ConstantValue.SUCCESS)
                    }
                }
        }
    }

    private fun onClickLeft(view: View) {
        finish()
    }

    private fun showBtnDoneLoading() {
        binding.btnDone.text = ""
        binding.ivSignUpLoading.visibility = View.VISIBLE
        btnDoneAnimator!!.start()
    }

    private fun hideBtnDoneLoading() {
        binding.btnDone.setText(R.string.submit)
        binding.ivSignUpLoading.visibility = View.GONE
        btnDoneAnimator!!.pause()
    }

    private fun setBtnDoneAnimator() {
        btnDoneAnimator = ObjectAnimator.ofFloat(binding.ivSignUpLoading, "Rotation", 0f, 360f)
        btnDoneAnimator?.duration = 2000
        btnDoneAnimator?.repeatCount = -1
    }

    private fun notifySignUpResult(result: String) {
        hideBtnDoneLoading()
        if (ConstantValue.SUCCESS == result) {
            // 涂鸦
            tuyaOnLogin()
            reportUserInfo()
            // 去首页
            YRCXSDKDataManager.userAccount = account
            YRCXSDKDataManager.firstLogin = false
            toast("Login success")
            loginSuccess() // 登录成功后需要调用该方法才能使登录拦截的逻辑形成闭环，不要再调用 finish() 方法
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

    private fun notifyResetPsdResult(result: String) {
        hideBtnDoneLoading()
        if (ConstantValue.SUCCESS == result) {
            UserSignInActivity.start(this, account, binding.ipvConfirmPsd.getEditText().text.toString(), true)
            finish()
        } else {
        }
    }

    private fun notifyUpdataPasswordResult(code: Int) {
        hideBtnDoneLoading()
        if (code == HttpCode.SUCCESS_CODE) {
            finish()
        } else {
            toast("失败")
        }
    }

    companion object{
        fun start(from : Activity, account : String,
                  verifyCode : String, countryCode : String, verifyType : Int) {
            from.startActivity<UserSetPasswordActivity>(
                ConstantValue.INTENT_KEY_ACCOUNT to account,
                ConstantValue.INTENT_KEY_VERIFY_CODE to verifyCode,
                ConstantValue.INTENT_KEY_COUNTRY_CODE to countryCode,
                ConstantValue.INTENT_KEY_VERIFY_TYPE to verifyType,
                KEY_ROUTER_PATH to from.intent.getStringExtra(KEY_ROUTER_PATH)
            )
        }
    }
}