package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityHelpBinding

class MineHelpActivity : MineBaseActivity<MineActivityHelpBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.help)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        binding.containerUserManual.setOnClickListener {

        }
        binding.containerFAQ.setOnClickListener {
            MineFAQActivity.start(this)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun onClickLeft(view : View) {
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
        fun start(from : Context) {
            from.startActivity<MineHelpActivity>()
        }
    }
}