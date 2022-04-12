package com.apemans.tuya.component.ui.inputwifi

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityInputWifiBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.scandevice.ScanDeviceActivity
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.doOnClick

class InputWifiActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityInputWifiBinding>() {

    private val sharedViewModel: SharedViewModel by applicationViewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.add_camera_connect_to_wifi)
        binding.apply {
            btnDone.doOnClick {
                ScanDeviceActivity.start("TP-LINK_HyFi_49", "nooie666")
            }
        }
        sharedViewModel.addDeviceEvent.asLiveData().observe(this) {
            finish()
        }
    }
}