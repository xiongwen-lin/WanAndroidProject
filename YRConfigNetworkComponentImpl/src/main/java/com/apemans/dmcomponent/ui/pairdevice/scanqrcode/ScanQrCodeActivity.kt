package com.apemans.dmcomponent.ui.pairdevice.scanqrcode

import android.os.Bundle
import com.dylanc.longan.doOnClick
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceFragmentScanQrCodeBinding
import com.apemans.dmcomponent.ui.pairdevice.connecting.ConnectingActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class ScanQrCodeActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentScanQrCodeBinding>() {

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        listOf(binding.ivFlashlight, binding.tvFlashlight).doOnClick {
            startActivity<ConnectingActivity>()
        }
    }
}