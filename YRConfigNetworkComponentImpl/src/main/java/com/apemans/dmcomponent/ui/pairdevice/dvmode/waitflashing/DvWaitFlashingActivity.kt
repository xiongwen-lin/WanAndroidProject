package com.apemans.dmcomponent.ui.pairdevice.dvmode.waitflashing

import android.os.Bundle
import com.dylanc.longan.doOnClick
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceFragmentWaitFlashingBinding
import com.apemans.dmcomponent.ui.pairdevice.connecthotspot.ConnectHotspotActivity
import com.apemans.dmcomponent.utils.step
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class DvWaitFlashingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentWaitFlashingBinding>() {

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.stepHeader.step = 2
        binding.btnNext.doOnClick {
            startActivity<ConnectHotspotActivity>()
        }
    }
}