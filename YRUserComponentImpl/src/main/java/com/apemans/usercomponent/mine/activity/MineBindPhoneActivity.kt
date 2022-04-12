package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityBindPhoneBinding

class MineBindPhoneActivity : MineBaseActivity<MineActivityBindPhoneBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initView() {
        setToolbar {
            title = "Bind phone"
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
            rightIcon(R.drawable.confirm_black, ::onClickRight)
        }
        setupInputFrameView()
    }

    private fun setupInputFrameView() {
        binding.ipvPhone.apply {
            setTitle("Phone number")
            addOnTextChanged { _, _ ->
                checkBtnEnable()
            }
        }
        binding.ipvVerifyCode.apply {
            setTitle(resources.getString(R.string.input_verify_code_enter_code))
            // 在此的作用是为了使 获取验证码按钮与输入框高度对齐
            showTips("")
            addOnTextChanged { _, _ ->
                // 在此的作用是为了使 获取验证码按钮与输入框高度对齐
                showTips("")
                checkBtnEnable()
            }
        }
        checkBtnEnable()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkBtnEnable() {
        if (!TextUtils.isEmpty(binding.ipvPhone.getEditText().text.toString()) && TextUtils.isEmpty(binding.ipvVerifyCode.getEditText().text.toString())) {
            binding.btnCode.isEnabled = true
            binding.btnCode.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnCode.isEnabled = false
            binding.btnCode.setTextColor(resources.getColor(R.color.theme_white))
        }
    }

    private fun onClickRight(view : View) {

    }

    private fun onClickLeft(view : View) {
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun start(from : Context) {
            var intent = Intent(from, MineBindPhoneActivity::class.java)
            from.startActivity(intent)
        }
    }
}