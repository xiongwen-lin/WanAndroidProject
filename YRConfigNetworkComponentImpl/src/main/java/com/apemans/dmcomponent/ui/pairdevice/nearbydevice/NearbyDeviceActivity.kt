package com.apemans.dmcomponent.ui.pairdevice.nearbydevice

import android.os.Bundle
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.databinding.DeviceLayoutRecyclerViewBinding
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.items.ConnectMethodItem
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.items.ConnectMethodViewDelegate
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener

/**
 * @author Dylan Cai
 */
class NearbyDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutRecyclerViewBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<String> {

    private val adapter = MultiTypeAdapter(ConnectMethodViewDelegate(this))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        adapter.items = listOf(
            ConnectMethodItem(R.drawable.device_video_camera,"Victure-0129e1"),
            ConnectMethodItem(R.drawable.device_video_camera,"Victure-0129e1"),
            ConnectMethodItem(R.drawable.device_video_camera,"Victure-0129e1"),
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: String, position: Int) {

    }
}