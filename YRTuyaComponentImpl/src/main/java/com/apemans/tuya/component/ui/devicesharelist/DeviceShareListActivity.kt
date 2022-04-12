package com.apemans.tuya.component.ui.devicesharelist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.apemans.base.utils.SpacesItemDecoration
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.dylanc.longan.dp
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_DEVICE_ID
import com.apemans.tuya.component.databinding.TuyaActivityShareListBinding
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.ui.addshareuser.AddShareUserActivity
import com.apemans.tuya.component.ui.devicesharelist.items.DeviceShareUserDelegate
import com.dylanc.longan.doOnClick
import com.tuya.smart.home.sdk.bean.SharedUserInfoBean

class DeviceShareListActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityShareListBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<SharedUserInfoBean> {

    private val adapter = MultiTypeAdapter(DeviceShareUserDelegate(this))
    private val viewModel: DeviceShareListViewModel by viewModels()
    private val deviceId: String by safeIntentExtras(KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.dev_sharing)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this,3)
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(16.dp, 16.dp))
        binding.btnAdd.doOnClick {
            AddShareUserActivity.start(TuyaRepository.selectedHomeId, deviceId)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.observeItemsChanged(this, viewModel.queryDevShareUserList(deviceId)) { oldItem, newItem ->
            oldItem.memeberId == newItem.memeberId
        }
    }

    override fun onItemClick(item: SharedUserInfoBean, position: Int) {
//        FamilyMemberActivity.start(item.memeberId,item.userName,item.remarkName,item.iconUrl,true)
    }

    companion object {
        fun start(deviceId: String) =
            startActivity<DeviceShareListActivity>(KEY_DEVICE_ID to deviceId)
    }
}