package com.apemans.dmcomponent.ui.pairdevice.connectpower

import android.os.Bundle
import coil.load
import com.dylanc.longan.doOnClick
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.databinding.DeviceLayoutPairCommonBinding
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.ConnectWifiActivity
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.DeviceType
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class ConnectPowerActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutPairCommonBinding>() {

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        binding.tvDescription.text = "Please power on the camera. After successful power on, the camera indicator will light up"
        binding.ivImage.load(R.drawable.device_illus_connect_power_cam)
        binding.btnNext.doOnClick {
            ConnectWifiActivity.start(DeviceType.CAMERA)
        }
    }
}
