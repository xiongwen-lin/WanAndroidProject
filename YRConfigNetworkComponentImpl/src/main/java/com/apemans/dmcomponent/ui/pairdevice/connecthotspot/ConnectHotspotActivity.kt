package com.apemans.dmcomponent.ui.pairdevice.connecthotspot

import android.os.Bundle
import androidx.activity.result.launch
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceLayoutPairCommonBinding
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.doOnClick
import com.dylanc.longan.launchWifiSettingsLauncher

/**
 * @author Dylan Cai
 */
class ConnectHotspotActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutPairCommonBinding>() {

    //private val viewModel: IpcControlViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.tvDescription.text =
            "1. Connect to Wi-Fi starting with the brand name \n2. Return to Osaio APP after successful connection"
        binding.btnNext.text = "To connect"
        binding.btnNext.doOnClick {
            launchWifiSettingsLauncher.launch()
        }
    }

    private val launchWifiSettingsLauncher = launchWifiSettingsLauncher {
        /**
        viewModel.startNetSpotTcp(wifiSSID) {
            if (it) {
                startActivity<SaveNameActivity>()
            } else {
                toast("connect failure")
            }
        }
        */
    }
}