package com.apemans.dmcomponent.ui.pairdevice.adddevice

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dylanc.longan.startActivity
import com.apemans.quickui.multitype.multiTypeAdapter
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.ACTIVITY_PATH_ADD_IPC_DEVICE
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.databinding.DeviceLayoutRecyclerViewBinding
import com.apemans.dmcomponent.ui.pairdevice.adddevice.items.DeviceType
import com.apemans.dmcomponent.ui.pairdevice.adddevice.items.DeviceTypeViewDelegate
import com.apemans.dmcomponent.ui.pairdevice.adddevice.items.TitleViewDelegate
import com.apemans.dmcomponent.ui.pairdevice.connectpower.ConnectPowerActivity
import com.apemans.dmcomponent.ui.pairdevice.huntingcamera.openpower.OpenPowerActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener

/**
 * @author Dylan Cai
 */
@Route(path = ACTIVITY_PATH_ADD_IPC_DEVICE)
class AddDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutRecyclerViewBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceType> {

    private val adapter = multiTypeAdapter {
        register(DeviceTypeViewDelegate(::onItemClick))
        register(TitleViewDelegate())
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.add_device)
        adapter.items = listOf(
            "Security",
            DeviceType(R.drawable.device_video_camera, "Video camera"),
            DeviceType(R.drawable.device_gateway_camera, "Gateway + camera"),
            DeviceType(R.drawable.device_hunting_camera, "Hunting camera"),
            "Smart home",
            DeviceType(R.drawable.device_smart_socket, "Smart socket"),
            DeviceType(R.drawable.device_smart_switch, "Smart switch")
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: DeviceType, position: Int) {
        when (item.name) {
            "Video camera" -> startActivity<ConnectPowerActivity>()
            "Hunting camera" -> startActivity<OpenPowerActivity>()
//            "Gateway + camera" -> startRouterActivity(ACTIVITY_PATH_ROUTER_CFG)
        }
    }
}