package com.apemans.dmcomponent.ui.pairmethod.phoneqrcodepair

import androidx.activity.viewModels
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.contants.deviceId
import com.apemans.dmcomponent.contants.name
import com.apemans.dmcomponent.contants.pwd
import com.apemans.dmcomponent.contants.ssid
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity
import com.apemans.dmcomponent.ui.pairmethod.fragment.createqrcode.CreateQrCodeFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.createqrcode.CreateQrCodeViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.devicetip.DeviceTipFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.inputwifi.InputWifiFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.waitingonline.WaitingOnlineFragment

/**
 * 扫描手机二维码配网
 */
class PhoneQrCodePairActivity : BasePairActivity() {

    private val createQrCodeViewModel: CreateQrCodeViewModel by viewModels()
    private val saveNameViewModel: SaveNameViewModel by viewModels()

    override fun initStepFragments() {
        addStepFragment(DeviceTipFragment.newInstance(CONNECT_POWER_ICON, CONNECT_POWER_DESCRIPTION))
        addStepFragment(InputWifiFragment()) { result ->
            createQrCodeViewModel.createQrCode(result.ssid.orEmpty(), result.pwd.orEmpty())
        }
        addStepFragment(CreateQrCodeFragment())
        addStepFragment(WaitingOnlineFragment()) { result ->
            saveNameViewModel.name.value = result.name
            saveNameViewModel.deviceId = result.deviceId!!
        }
        addStepFragment(SaveNameFragment())
    }

    companion object {
        val CONNECT_POWER_ICON: Int get() = R.drawable.device_illus_connect_power_cam
        const val CONNECT_POWER_DESCRIPTION = "Please power on the camera. After successful power on, the camera indicator will light up"
    }
}