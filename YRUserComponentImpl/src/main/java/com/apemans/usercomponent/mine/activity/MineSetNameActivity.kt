package com.apemans.usercomponent.mine.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivitySetNameBinding
import com.apemans.usercomponent.mine.util.ConstantValue

class MineSetNameActivity : MineBaseActivity<MineActivitySetNameBinding>() {

    private val userNickName : String by intentExtras(ConstantValue.INTENT_KEY_NICK_NAME, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.btnDone.setOnClickListener {
            updataNickName()
        }
        setToolbar {
            title = "账号设置"
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        setupInputView()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setupInputView() {
        binding.ipvSetName.apply {
            setTitle("姓名")
            addOnTextChanged { _, _ ->
                checkBtnEnable()
            }
            setText(userNickName)
        }
        checkBtnEnable()
    }

    private fun checkBtnEnable() {
        if (binding.ipvSetName.getEditText().text.toString().isNotEmpty()) {
            binding.btnDone.isEnabled = true
            binding.btnDone.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnDone.isEnabled = false
            binding.btnDone.setTextColor(resources.getColor(R.color.theme_white))
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun updataNickName() {
        var intent = Intent()
        intent.putExtra(ConstantValue.INTENT_KEY_NICK_NAME, binding.ipvSetName.getEditText().text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun start(from : Activity, userNickName : String, requestCode : Int) {
            var intent = Intent(from, MineSetNameActivity::class.java)
            intent.putExtra(ConstantValue.INTENT_KEY_NICK_NAME, userNickName)
            from.startActivityForResult(intent, requestCode)
        }
    }
}