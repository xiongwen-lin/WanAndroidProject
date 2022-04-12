package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityPhotoBinding

class PhotoActivity : MineBaseActivity<MineActivityPhotoBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun initView() {
        setToolbar {
            title = "相册"
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
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
            from.startActivity<PhotoActivity>()
        }
    }
}