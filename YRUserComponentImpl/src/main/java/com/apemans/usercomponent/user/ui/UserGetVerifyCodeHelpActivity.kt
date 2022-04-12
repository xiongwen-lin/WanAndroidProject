package com.apemans.usercomponent.user.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.UserActivityGetVerifyCodeHelpBinding

class UserGetVerifyCodeHelpActivity : UserBaseActivity<UserActivityGetVerifyCodeHelpBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.help)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    companion object {
        fun start(from : Context) {
            var intent = Intent(from, UserGetVerifyCodeHelpActivity::class.java)
            from.startActivity(intent)
        }
    }
}