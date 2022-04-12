package com.apemans.dmcomponent.ui.pairmethod

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.ACTIVITY_PATH_PAIR_DEVICE
import com.apemans.dmcomponent.ui.pairdevice.adddevice.items.DeviceType
import com.apemans.dmcomponent.ui.pairdevice.adddevice.items.DeviceTypeViewDelegate
import com.apemans.dmcomponent.ui.pairmethod.bluetoothpair.BluetoothPairActivity
import com.apemans.dmcomponent.ui.pairmethod.deviceqrcodepair.DeviceQrCodePairActivity
import com.apemans.dmcomponent.ui.pairmethod.hotspotpair.HotspotPairActivity
import com.apemans.dmcomponent.ui.pairmethod.phoneqrcodepair.PhoneQrCodePairActivity
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.finishWithResult
import com.dylanc.longan.intentOf

@Route(path = ACTIVITY_PATH_PAIR_DEVICE)
class PairMethodActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CommonLayoutRecyclerViewBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceType> {

    private val adapter = MultiTypeAdapter(DeviceTypeViewDelegate(this))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar("配对设备")
        adapter.items = listOf(
            DeviceType(0, "扫描手机二维码"),
            DeviceType(0, "扫描设备二维码"),
            DeviceType(0, "热点配网"),
            DeviceType(0, "蓝牙配网"),
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: DeviceType, position: Int) {
        when (item.name) {
            "扫描手机二维码" -> {
                startActivityLauncher.launch(intentOf<PhoneQrCodePairActivity>())
            }
            "扫描设备二维码" -> {
                startActivityLauncher.launch(intentOf<DeviceQrCodePairActivity>())
            }
            "热点配网" -> {
                startActivityLauncher.launch(intentOf<HotspotPairActivity>())
            }
            "蓝牙配网" -> {
                startActivityLauncher.launch(intentOf<BluetoothPairActivity>())
            }
        }
    }

    private val startActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK)
            finishWithResult()
    }
}