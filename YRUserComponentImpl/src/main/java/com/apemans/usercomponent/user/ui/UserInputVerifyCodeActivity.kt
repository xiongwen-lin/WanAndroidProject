package com.apemans.usercomponent.user.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.apemans.business.apisdk.client.define.HttpCode
import com.dylanc.longan.intentExtras
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.UserActivityInputVerifyCodeBinding
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.viewModel.UserForgotPasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * 忘记密码 -- 输入验证码
 */
class UserInputVerifyCodeActivity : UserBaseActivity<UserActivityInputVerifyCodeBinding>() {

    val account : String by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT, "")
    val countryCode : String by intentExtras(ConstantValue.INTENT_KEY_COUNTRY_CODE, "")
    val type : Int by intentExtras(ConstantValue.INTENT_KEY_VERIFY_TYPE, -1)
    private lateinit var viewModel : UserForgotPasswordViewModel
    private var btnDoneAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        setBtnDoneAnimator()
    }

    fun initView() {
        setToolbar {
            title = resources.getString(R.string.sign_in_forget_pwd)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        with(binding) {
            tvDestination.text = account
            btnDone.setOnClickListener { clickBtn() }
            ipvVerifyCode.apply {
                addOnRightTextClick { _, _ -> trySendVerifyCode() }
                addOnTextChanged { _, _ -> checkBtnEnable() }
            }
        }
        checkBtnEnable()
    }

    private fun initData() {
        viewModel = registerViewModule(UserForgotPasswordViewModel::class.java)
        lifecycleScope.launch { showVerifyCodeCounter() } // 开始倒计时
    }

    private fun trySendVerifyCode() {
        lifecycleScope.launch {
            viewModel.sendVerifyCode(account, countryCode, type)
                .observe(this@UserInputVerifyCodeActivity){
                    lifecycleScope.launch { showVerifyCodeCounter() } // 开始倒计时
                }
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun clickBtn() {
        if (binding.ipvVerifyCode.getEditText().text.isEmpty()) {
            toast(resources.getString(R.string.input_verify_please_enter_code))
            return
        }
        showBtnDoneLoading()
        lifecycleScope.launch {
            viewModel.checkVerifyCode(account, binding.ipvVerifyCode.getEditText().text.toString(), countryCode, type)
                .observe(this@UserInputVerifyCodeActivity) {
                    notifyCheckVerifyCodeResult(it)
                }
        }
    }

    private fun checkBtnEnable() {
        if (!TextUtils.isEmpty(binding.ipvVerifyCode.getEditText().text.toString())) {
            binding.btnDone.isEnabled = true
            binding.btnDone.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnDone.isEnabled = false
            binding.btnDone.setTextColor(resources.getColor(R.color.theme_white))
        }
    }

    private fun showBtnDoneLoading() {
        binding.btnDone.text = ""
        binding.ivBtnDoneLoading.visibility = View.VISIBLE
        btnDoneAnimator!!.start()
    }

    private fun hideBtnDoneLoading() {
        binding.btnDone.setText(R.string.submit)
        binding.ivBtnDoneLoading.visibility = View.GONE
        btnDoneAnimator!!.pause()
    }

    private fun setBtnDoneAnimator() {
        btnDoneAnimator = ObjectAnimator.ofFloat(binding.ivBtnDoneLoading, "Rotation", 0f, 360f)
        btnDoneAnimator?.duration = 2000
        btnDoneAnimator?.repeatCount = -1
    }

    private fun notifyCheckVerifyCodeResult(code: Int) {
        if (isDestroyed) {
            return
        }
        hideBtnDoneLoading()
        if (code == HttpCode.SUCCESS_CODE) {
            UserSetPasswordActivity.start(this, account, binding.ipvVerifyCode.getEditText().text.toString(), countryCode, type)
        } else if (code == HttpCode.CODE_1054) {
            toast(resources.getString(R.string.sign_up_account_exist))
        } else {
            toast(resources.getString(R.string.sign_in_verify_code_incorrect))
        }
    }

    private suspend fun showVerifyCodeCounter() {
        startVerifyCodeCounter()
            .collect {
                if (ConstantValue.SUCCESS == it) {
                    binding.ipvVerifyCode.setRightText(resources.getString(R.string.sign_in_resend))
                } else {
                    binding.ipvVerifyCode.setRightText("${it}s")
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
        fun start(from: Context, account: String, countryCode: String, type: Int) {
            from.startActivity<UserInputVerifyCodeActivity>(
                ConstantValue.INTENT_KEY_ACCOUNT to account,
                ConstantValue.INTENT_KEY_COUNTRY_CODE to countryCode,
                ConstantValue.INTENT_KEY_VERIFY_TYPE to type
            )
        }
    }
}