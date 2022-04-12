package com.apemans.tuya.component.ui.addhome

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityAddHomeBinding
import com.dylanc.longan.textString

class AddHomeActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityAddHomeBinding>() {

    private val viewModel: AddHomeViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            title = "Add home"
            rightIcon(R.drawable.device_nav_confirm_on, ::onRightBtnClick)
        }
    }

    private fun onRightBtnClick(v: View) {
        viewModel.addHome(binding.edtName.textString).observe(this) {
            finish()
        }
    }
}