package com.apemans.dmcomponent.ui.pairmethod.bluetoothpair

import androidx.activity.viewModels
import com.apemans.dmcomponent.contants.pwd
import com.apemans.dmcomponent.contants.ssid
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity
import com.apemans.dmcomponent.ui.pairmethod.fragment.connectbluetooth.ConnectBluetoothFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.connectbluetooth.ConnectBluetoothViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.devicetip.DeviceTipFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.inputwifi.InputWifiFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.savename.SaveNameViewModel
import com.apemans.dmcomponent.ui.pairmethod.fragment.scanbluetooth.ScanBluetoothFragment
import com.apemans.dmcomponent.ui.pairmethod.phoneqrcodepair.PhoneQrCodePairActivity

/**
 * 蓝牙配网
 */
class BluetoothPairActivity : BasePairActivity() {

    private val connectBluetoothViewModel: ConnectBluetoothViewModel by viewModels()
    private val saveNameViewModel: SaveNameViewModel by viewModels()

    override fun initStepFragments() {
        addStepFragment(DeviceTipFragment.newInstance(PhoneQrCodePairActivity.CONNECT_POWER_ICON, PhoneQrCodePairActivity.CONNECT_POWER_DESCRIPTION))
        addStepFragment(InputWifiFragment()) { result ->
            connectBluetoothViewModel.setWifi(result.ssid.orEmpty(), result.pwd.orEmpty())
            saveNameViewModel.name.value = result.ssid
        }
        addStepFragment(ScanBluetoothFragment())
        addStepFragment(ConnectBluetoothFragment())
        addStepFragment(SaveNameFragment())
    }
}