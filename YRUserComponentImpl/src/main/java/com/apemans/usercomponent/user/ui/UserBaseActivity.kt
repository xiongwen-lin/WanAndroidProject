package com.apemans.usercomponent.user.ui

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.apemans.yruibusiness.base.BaseActivity

open class UserBaseActivity<VB : ViewBinding> : com.apemans.yruibusiness.base.BaseActivity<VB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    fun initGlobalData () {

    }

    override fun onRestart() {
        super.onRestart()
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

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }
}