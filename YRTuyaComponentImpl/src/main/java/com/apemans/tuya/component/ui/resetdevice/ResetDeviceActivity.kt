package com.apemans.tuya.component.ui.resetdevice

import android.os.Bundle
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityResetDeviceBinding
import com.dylanc.longan.doOnClick

class ResetDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityResetDeviceBinding>() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.reset_device)
        binding.btnReset.doOnClick {
            finish()
        }
    }
}