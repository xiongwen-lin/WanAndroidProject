package com.apemans.custom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isNotEmpty

import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.custom.album.MediaConstant.INTENT_KEY_NICK_NAME
import com.apemans.custom.databinding.CustActivityCustomNameBinding
import com.apemans.userapi.enrty.UserInfo


class CustomNameActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CustActivityCustomNameBinding>() {
    //private var  account = UserInfo?.userAccount.toString()
    private var  account = "767339274@qq.com"
    override fun onViewCreated(savedInstanceState: Bundle?) {

        initView()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.feedback_contact_info)
            leftIcon(R.drawable.ic_back, ::onClickLeft)
        }
        binding.btnDone.setOnClickListener {
            onClickBtn()
        }
        binding.ipvCustomName.getEditText().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                checkBtnEnable()
            }
        })
        binding.ipvCustomName.setText(account)

    }


    private fun checkBtnEnable() {
        if (binding.ipvCustomName.getEditText().text.isNotEmpty()) {
            binding.btnDone.isEnabled = true
            binding.btnDone.setTextColor(resources.getColor(R.color.theme_color))
            binding.btnDone.background = resources.getDrawable(R.drawable.button_sure_radius_22)
        } else {
            binding.btnDone.isEnabled = false
            binding.btnDone.setTextColor(resources.getColor(R.color.unable_clickable_color))
            binding.btnDone.background = resources.getDrawable(R.drawable.button_sure_radius_gray)
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun onClickBtn() {
        val intent  = Intent()
        intent.putExtra(INTENT_KEY_NICK_NAME, binding.ipvCustomName.getEditText().text.toString())
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