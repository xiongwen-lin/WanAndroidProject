package com.apemans.dmcomponent.ui.pairdevice.connectinghotspot

import android.os.Bundle
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.contants.KEY_SSID
import com.apemans.dmcomponent.databinding.DeviceFragmentConnectingHotspotBinding
import com.apemans.dmcomponent.utils.step
import com.apemans.dmcomponent.utils.wifiSSID
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.*

/**
 * @author Dylan Cai
 */
class ConnectingHotspotActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentConnectingHotspotBinding>() {

    private val ssid: String by safeIntentExtras(KEY_SSID)
    //private val viewModel: IpcControlViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.stepHeader.step = 3
        binding.btnConnectWifi.doOnClick {
            launchWifiSettingsLauncher.launch()
        }
    }

    private val launchWifiSettingsLauncher = launchWifiSettingsLauncher {
        if (ssid == wifiSSID) {
            /*
            viewModel.startNetSpotP2p(ssid, ssid, "HC320") {
                if (it){
                    startActivity<SaveNameActivity>()
                } else{
                    toast("connect failure")
                }
            }

             */
        }
    }

    companion object {
        fun start(ssid: String) =
            startActivity<ConnectingHotspotActivity>(KEY_SSID to ssid)
    }
}