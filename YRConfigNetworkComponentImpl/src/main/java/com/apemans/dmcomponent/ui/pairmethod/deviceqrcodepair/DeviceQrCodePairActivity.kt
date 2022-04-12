package com.apemans.dmcomponent.ui.pairmethod.deviceqrcodepair

import androidx.activity.viewModels
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.contants.*
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity
import com.apemans.dmcomponent.ui.pairmethod.fragment.binddevice.BindDeviceFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.binddevice.BindDeviceViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.devicetip.DeviceTipFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.scanqrcode.ScanQrCodeFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.waitingonline.WaitingOnlineFragment

/**
 * 扫描设备二维码配网
 */
class DeviceQrCodePairActivity : BasePairActivity() {

    private val bindDeviceViewModel: BindDeviceViewModel by viewModels()

    override fun initStepFragments() {
        addStepFragment(DeviceTipFragment.newInstance(CONNECT_POWER_ICON, CONNECT_POWER_DESCRIPTION))
        addStepFragment(ScanQrCodeFragment()){
            bindDeviceViewModel.uuid = it.result!!
        }
        addStepFragment(BindDeviceFragment())
    }

    companion object {
        private val CONNECT_POWER_ICON: Int get() = R.drawable.device_illus_connect_power_cam
        private const val CONNECT_POWER_DESCRIPTION = "Please power on the camera. After successful power on, the camera indicator will light up"
    }
}