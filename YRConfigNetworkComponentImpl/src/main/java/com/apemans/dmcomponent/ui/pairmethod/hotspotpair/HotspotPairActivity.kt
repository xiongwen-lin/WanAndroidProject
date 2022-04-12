package com.apemans.dmcomponent.ui.pairmethod.hotspotpair

import androidx.activity.viewModels
import com.apemans.dmcomponent.contants.deviceId
import com.apemans.dmcomponent.contants.name
import com.apemans.dmcomponent.contants.pwd
import com.apemans.dmcomponent.contants.ssid
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity
import com.apemans.dmcomponent.ui.pairmethod.fragment.checkssid.CheckSsidFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.connecthotspot.ConnectHotspotFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.connecthotspot.ConnectHotspotViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.devicetip.DeviceTipFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.inputwifi.InputWifiFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.waitingonline.WaitingOnlineFragment
import com.apemans.dmcomponent.ui.pairmethod.phoneqrcodepair.PhoneQrCodePairActivity

/**
 * 热点配网
 */
class HotspotPairActivity : BasePairActivity() {

    private val saveNameViewModel: SaveNameViewModel by viewModels()
    private val connectHotspotViewModel:ConnectHotspotViewModel by viewModels()

    override fun initStepFragments() {
        addStepFragment(DeviceTipFragment.newInstance(PhoneQrCodePairActivity.CONNECT_POWER_ICON, PhoneQrCodePairActivity.CONNECT_POWER_DESCRIPTION))
        addStepFragment(InputWifiFragment()) { result ->
//            saveNameViewModel.name.value = result.ssid
            connectHotspotViewModel.ssid = result.ssid!!
            connectHotspotViewModel.pwd = result.pwd!!
        }
        addStepFragment(CheckSsidFragment())
        addStepFragment(ConnectHotspotFragment()){ result ->
            saveNameViewModel.name.value = result.name
            saveNameViewModel.deviceId = result.deviceId!!
        }
        addStepFragment(SaveNameFragment())
    }
}