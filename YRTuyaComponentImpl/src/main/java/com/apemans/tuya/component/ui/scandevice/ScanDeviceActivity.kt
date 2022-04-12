package com.apemans.tuya.component.ui.scandevice

import android.os.Bundle
import androidx.activity.viewModels
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_PSD
import com.apemans.tuya.component.constants.KEY_SSID
import com.apemans.tuya.component.databinding.TuyaActivityScanDeviceBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity

class ScanDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityScanDeviceBinding>() {

    private val viewModel: ScanDeviceViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()
    private val ssid: String by safeIntentExtras(KEY_SSID)
    private val pwd: String by safeIntentExtras(KEY_PSD)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.pairing_please_wait)
        viewModel.ezPair(ssid, pwd).observe(this) {
            sharedViewModel.addDeviceEvent.tryEmit(Unit)
        }
    }

    companion object {
        fun start(ssid: String, pwd: String) =
            startActivity<ScanDeviceActivity>(KEY_SSID to ssid, KEY_PSD to pwd)
    }
}