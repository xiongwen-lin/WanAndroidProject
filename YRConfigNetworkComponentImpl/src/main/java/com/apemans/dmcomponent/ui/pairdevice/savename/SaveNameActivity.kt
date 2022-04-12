package com.apemans.dmcomponent.ui.pairdevice.savename

import android.os.Bundle
import com.dylanc.longan.startActivity
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.databinding.DeviceFragmentSaveNameBinding
import com.apemans.dmcomponent.db.KEY_DEVICE_ID
import com.apemans.dmcomponent.ui.pairdevice.savename.items.RecommendedNameViewDelegate
import com.apemans.yruibusiness.ui.toolbar.setToolbar

/**
 * @author Dylan Cai
 */
class SaveNameActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentSaveNameBinding>() {

//    private val adapter = MultiTypeAdapter(RecommendedNameViewDelegate(this))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
//        adapter.items = listOf(
//            R.string.add_camera_front_door,
//            R.string.add_camera_office,
//            R.string.add_camera_living_room,
//            R.string.add_camera_garage,
//            R.string.add_camera_baby_room,
//            R.string.add_camera_kitchen,
//        )
//        binding.recyclerView.adapter = adapter
    }

    companion object {
        fun start(deviceId: String) =
            startActivity<SaveNameActivity>(KEY_DEVICE_ID to deviceId)
    }
}