package com.apemans.dmcomponent.ui.pairdevice.scanedqrcode

import android.os.Bundle
import coil.load
import com.dylanc.longan.*
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.contants.KEY_PWD
import com.apemans.dmcomponent.contants.KEY_SSID
import com.apemans.dmcomponent.databinding.DeviceFragmentScanedQrcodeBinding
import com.apemans.dmcomponent.ui.pairdevice.connecting.ConnectingActivity
import com.apemans.dmcomponent.utils.toQRCodeBitmap
import com.apemans.dmcomponent.utils.wifiEncodeStringOf
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class ScanedQrCodeActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentScanedQrcodeBinding>() {

    private val ssid: String by safeIntentExtras(KEY_SSID)
    private val psd: String by safeIntentExtras(KEY_PWD)
//    private val model: String by safeIntentExtras(KEY_MODEL)
//    private val connectionMode: String by safeIntentExtras(KEY_MODE)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        val info = wifiEncodeStringOf(ssid, psd, "us")
        val bitmap = info.toQRCodeBitmap(300.dp.toInt(), 300.dp.toInt())
        binding.ivQrcode.load(bitmap)
        binding.btnNext.doOnClick {
            startActivity<ConnectingActivity>()
        }
    }


    companion object {
//        const val KEY_MODEL = "model"
//        const val KEY_MODE = "mode"

        //        fun start(ssid: String, psd: String, model: String, connectionMode: Int) {
        fun start(ssid: String, psd: String) {
            startActivity<ScanedQrCodeActivity>(
                KEY_SSID to ssid, KEY_PWD to psd,
//                KEY_MODEL to model, KEY_MODE to connectionMode
            )
        }
    }
}