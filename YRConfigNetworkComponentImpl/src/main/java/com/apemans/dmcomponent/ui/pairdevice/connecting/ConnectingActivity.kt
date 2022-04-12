package com.apemans.dmcomponent.ui.pairdevice.connecting

import android.os.Bundle
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceFragmentConnectingBinding
import com.apemans.dmcomponent.ui.pairdevice.savename.SaveNameActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class ConnectingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentConnectingBinding>() {

    private val viewModel: ConnectingViewModel by requestViewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        viewModel.scanPairedDevice()
            .observe(this) {
//                it.forEach {
////                    if ()
//                }
                startActivity<SaveNameActivity>()
//                NetworkUtil.isWifi()
            }
    }
}