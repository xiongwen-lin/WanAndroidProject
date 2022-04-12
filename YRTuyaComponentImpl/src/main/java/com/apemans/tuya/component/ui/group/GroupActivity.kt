package com.apemans.tuya.component.ui.group

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.base.utils.SpacesItemDecoration
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.dylanc.longan.dp
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_GROUP_ID
import com.apemans.tuya.component.ui.group.items.GroupViewDelegate
import com.apemans.tuya.component.ui.groupsetting.GroupSettingActivity
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.bean.DeviceBean

class GroupActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CommonLayoutRecyclerViewBinding>() {

    private val adapter = MultiTypeAdapter(GroupViewDelegate(::onItemSwitchClick))
    private val groupId: Long by safeIntentExtras(KEY_GROUP_ID)
    private val viewModel: GroupViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        val groupBean = TuyaHomeSdk.getDataInstance().getGroupBean(groupId)!!
        setToolbar(groupBean.name) {
            rightIcon(R.drawable.menu_setting_icon_state_list) {
                GroupSettingActivity.start(groupId)
            }
        }

        binding.recyclerView.apply {
            adapter = this@GroupActivity.adapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(SpacesItemDecoration(16.dp, 16.dp))
        }
        adapter.items = groupBean.deviceBeans
    }

    private fun onItemSwitchClick(deviceBean: DeviceBean, isOpen: Boolean) {
        viewModel.sendCommand(deviceBean, isOpen)
    }

    companion object {
        fun start(groupId: Long) =
            startActivity<GroupActivity>(KEY_GROUP_ID to groupId)
    }
}