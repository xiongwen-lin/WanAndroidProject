package com.apemans.dmcomponent.ui.pairmethod.fragment.inputwifi

import android.Manifest
import android.os.Bundle
import android.view.View
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.contants.pwd
import com.apemans.dmcomponent.contants.ssid
import com.apemans.dmcomponent.databinding.DeviceFragmentInputWifiBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.utils.wifiSSID
import com.apemans.base.MMKVOwner
import com.dylanc.longan.*

class InputWifiFragment : com.apemans.yruibusiness.base.BaseComponentFragment<DeviceFragmentInputWifiBinding>(),MMKVOwner {
    private val networkAvailableLiveData = NetworkAvailableLiveData()

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        binding.btnNext.doOnClick {
            val result = Bundle().apply {
                ssid = "TP-LINK_HyFi_49"
                pwd = "nooie666"
//                ssid = binding.edtWifiName.textString
//                pwd = binding.edtPwd.textString
            }
            nextStep(result)
        }
        binding.btnSwitchWifi.doOnClick {
            launchWifiSettingsLauncher.launch(Unit)
        }
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            binding.edtWifiName.setText(wifiSSID)
        },
        onDenied = {

        },
        onShowRequestRationale = {

        }
    )

    private val launchWifiSettingsLauncher = launchWifiSettingsLauncher {
        binding.edtWifiName.setText(wifiSSID)
    }
}