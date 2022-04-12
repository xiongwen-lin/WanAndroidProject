package com.apemans.dmcomponent.ui.pairdevice.waitflashing

import android.os.Bundle
import com.dylanc.longan.doOnClick
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.contants.KEY_PWD
import com.apemans.dmcomponent.contants.KEY_SSID
import com.apemans.dmcomponent.databinding.DeviceFragmentWaitFlashingBinding
import com.apemans.dmcomponent.ui.pairdevice.scanedqrcode.ScanedQrCodeActivity
import com.apemans.dmcomponent.utils.step
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class WaitFlashingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentWaitFlashingBinding>() {

    private val ssid: String by safeIntentExtras(KEY_SSID)
    private val psd: String by safeIntentExtras(KEY_PWD)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.stepHeader.step = 2
        binding.btnNext.doOnClick {
            ScanedQrCodeActivity.start(ssid, psd)
        }
    }

    companion object {
        fun start(ssid: String, psd: String) =
            startActivity<WaitFlashingActivity>(KEY_SSID to ssid, KEY_PWD to psd,)
    }
}