package com.apemans.tuya.component.ui.groupmanager

import android.os.Bundle
import androidx.fragment.app.commit
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.GroupType
import com.apemans.tuya.component.constants.KEY_GROUP_ID
import com.apemans.tuya.component.constants.KEY_HOME_ID
import com.apemans.tuya.component.constants.deviceType
import com.apemans.tuya.component.databinding.TuyaActivityGroupManagerBinding
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.ui.creategroup.GroupDeviceFragment
import com.apemans.tuya.component.ui.creategroup.lamp.LampGroupFragment
import com.apemans.tuya.component.ui.creategroup.plug.PlugGroupFragment
import com.tuya.smart.home.sdk.TuyaHomeSdk

class GroupManagerActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityGroupManagerBinding>() {

    private val homeId: Long by intentExtras(KEY_HOME_ID, TuyaRepository.selectedHomeId)
    private val viewModel: GroupManagerViewModel by requestViewModels()
    private val groupId: Long by safeIntentExtras(KEY_GROUP_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        val group = TuyaHomeSdk.getDataInstance().getGroupBean(groupId)!!
        val fragment: GroupDeviceFragment =
            if (group.deviceType == GroupType.PLUG) {
                PlugGroupFragment.newInstance(homeId)
            } else {
                LampGroupFragment.newInstance(homeId)
            }
        setToolbar(R.string.manage_devices) {
            rightIcon(R.drawable.define_black) {
                viewModel.updateDeviceListForGroup(groupId, fragment.getCheckedIds())
                    .observe(lifecycleOwner) {
                        finish()
                    }
            }
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container, fragment)
        }
    }

    companion object {
        fun start(groupId: Long) =
            startActivity<GroupManagerActivity>(KEY_GROUP_ID to groupId)

    }
}