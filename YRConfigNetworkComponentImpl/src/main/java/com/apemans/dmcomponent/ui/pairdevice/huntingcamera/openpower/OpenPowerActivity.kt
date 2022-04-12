package com.apemans.dmcomponent.ui.pairdevice.huntingcamera.openpower

import android.os.Bundle
import com.dylanc.longan.doOnClick
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceLayoutPairCommonBinding
import com.apemans.dmcomponent.ui.pairdevice.huntingcamera.scanbluetooth.ScanBluetoothActivity
import com.apemans.dmcomponent.utils.step
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class OpenPowerActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutPairCommonBinding>() {

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.stepHeader.step = 1
        binding.tvDescription.text = "Turn on the camera. Make sure to turn on the power of the camera when the battery is charged"
        binding.btnNext.doOnClick {
            startActivity<ScanBluetoothActivity>()
        }
    }
}