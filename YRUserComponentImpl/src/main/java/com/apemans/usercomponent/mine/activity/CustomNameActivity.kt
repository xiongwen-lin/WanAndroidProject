package com.apemans.usercomponent.mine.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityCustomNameBinding
import com.apemans.usercomponent.mine.util.ConstantValue
import com.apemans.usercomponent.user.spinner.InputFrameView
import com.apemans.yrcxsdk.data.YRCXSDKDataManager

class CustomNameActivity : MineBaseActivity<MineActivityCustomNameBinding>() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.feedback_contact_info)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        binding.btnDone.setOnClickListener {
            onClickBtn()
        }
        setupCountryInput()
    }

    private fun setupCountryInput() {
        binding.ipvCustomName.setTheme(InputFrameView.INPUT_FRAME_THEME_TYPE_DEFAULT)
            .setInputTitle(resources.getString(R.string.feedback_email))
            .setInputBtn(R.drawable.close_white_icon_state_list)
            .setOnClickListener(object : InputFrameView.OnInputFrameClickListener {
                override fun onInputBtnClick() {
                    binding.ipvCustomName.setEtInputText("")
                }

                override fun onEditorAction() {
                }

                override fun onEtInputClick() {
                }

            })
            .setInputTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    checkBtnEnable()
                }

            })
        binding.ipvCustomName.setEtInputText(YRCXSDKDataManager.userAccount)
        checkBtnEnable()
    }

    private fun checkBtnEnable() {
        if (binding.ipvCustomName.inputText.isNotEmpty()) {
            binding.btnDone.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnDone.setTextColor(resources.getColor(R.color.theme_white))
        }
        binding.btnDone.isEnabled = binding.ipvCustomName.inputTextNoTrim.isNotEmpty()
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun onClickBtn() {
        val intent  = Intent()
        intent.putExtra(ConstantValue.INTENT_KEY_NICK_NAME, binding.ipvCustomName.inputText)
        setResult(RESULT_OK, intent)
        finish()
    }
    companion object {
        fun start(from : Activity, requestCode : Int) {
            val intent  = Intent(from, CustomNameActivity::class.java)
            from.startActivityForResult(intent, requestCode)
        }
    }
}