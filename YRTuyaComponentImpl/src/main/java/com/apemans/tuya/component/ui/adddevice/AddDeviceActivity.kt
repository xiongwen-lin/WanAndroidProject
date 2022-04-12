package com.apemans.tuya.component.ui.adddevice

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.databinding.TuyaActivityAddDeviceBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.inputwifi.InputWifiActivity
import com.apemans.tuya.component.ui.resetdevice.ResetDeviceActivity
import com.apemans.tuya.module.api.ACTIVITY_PATH_ADD_TUYA_DEVICE
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.doOnClick
import com.dylanc.longan.startActivity

@Route(path = ACTIVITY_PATH_ADD_TUYA_DEVICE)
class AddDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityAddDeviceBinding>() {

    private val sharedViewModel: SharedViewModel by applicationViewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.btnDone.doOnClick {
            startActivity<InputWifiActivity>()
        }
        binding.tvLightNoRight.doOnClick {
            startActivity<ResetDeviceActivity>()
        }
        sharedViewModel.addDeviceEvent.asLiveData().observe(this) {
            finish()
        }
    }
}