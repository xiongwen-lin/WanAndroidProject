package com.apemans.tuya.component.ui.schedulelist

import android.os.Bundle
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.databinding.CommonLayoutRefreshListBinding
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.submitItems
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.*
import com.apemans.tuya.component.ui.scheduleaction.ScheduleActionActivity
import com.apemans.tuya.component.ui.schedulelist.items.DeviceScheduleViewDelegate
import com.tuya.smart.home.sdk.TuyaHomeSdk

class ScheduleListActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CommonLayoutRefreshListBinding>() {

    private val adapter = MultiTypeAdapter(DeviceScheduleViewDelegate())
    private val viewModel: ScheduleListViewModel by requestViewModels()
    private val groupId: Long by safeIntentExtras(KEY_GROUP_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.group_schedule) {
            rightIcon(R.drawable.scene_add) {
                val group = TuyaHomeSdk.getDataInstance().getGroupBean(groupId)!!
                ScheduleActionActivity.start(groupId, group.deviceType)
            }
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTimerWithTask(groupId.toString(), groupId.toString())
            .observe(this) {
                adapter.submitItems(it.timerList) { oldItem, newItem ->
                    oldItem.timerId == newItem.timerId
                }
            }
    }

    companion object {
        fun start(groupId: Long) =
            startActivity<ScheduleListActivity>(KEY_GROUP_ID to groupId)
    }
}