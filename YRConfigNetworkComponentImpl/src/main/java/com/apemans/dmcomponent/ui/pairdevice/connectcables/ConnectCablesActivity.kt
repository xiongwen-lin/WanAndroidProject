package com.apemans.dmcomponent.ui.pairdevice.connectcables

import android.os.Bundle
import com.dylanc.longan.doOnClick
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceLayoutPairCommonBinding
import com.apemans.dmcomponent.ui.pairdevice.scanqrcode.ScanQrCodeActivity
import com.apemans.dmcomponent.utils.step
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class ConnectCablesActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutPairCommonBinding>() {

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.stepHeader.step = 2
        binding.tvDescription.text = "Please use a network cable to connect the device and the router"
        binding.btnNext.doOnClick {
            startActivity<ScanQrCodeActivity>()
        }
    }
}