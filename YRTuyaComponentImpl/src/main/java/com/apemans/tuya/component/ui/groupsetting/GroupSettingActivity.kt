package com.apemans.tuya.component.ui.groupsetting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_GROUP_ID
import com.apemans.tuya.component.databinding.TuyaActivityGroupSettingBinding
import com.apemans.tuya.component.ui.groupmanager.GroupManagerActivity
import com.apemans.tuya.component.ui.inputtext.InputTextContract
import com.apemans.tuya.component.ui.inputtext.InputTextRequest
import com.apemans.tuya.component.ui.schedulelist.ScheduleListActivity
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback

class GroupSettingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityGroupSettingBinding>() {

    private val viewModel: GroupSettingViewModel by viewModels()
    private val groupId: Long by safeIntentExtras(KEY_GROUP_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.settings)
        binding.apply {
            val group = TuyaHomeSdk.getDataInstance().getGroupBean(groupId)!!
            labelGroupName.setupConfigure(lifecycleOwner)
            labelManageDevices.setupConfigure(lifecycleOwner)
            labelGroupSchedule.setupConfigure(lifecycleOwner)

            labelGroupName.update {
                text = getString(R.string.group_name)
                subText = group.name
                rightIconVisibility = View.VISIBLE
            }
            labelGroupName.doOnClick {
                inputTextLauncher.launch(InputTextRequest(getString(R.string.group_name)))
            }

            labelManageDevices.update {
                text = getString(R.string.manage_devices)
                subText = group.deviceBeans.orEmpty().size.toString()
                rightIconVisibility = View.VISIBLE
            }
            labelManageDevices.doOnClick {
               GroupManagerActivity.start(groupId)
            }

            labelGroupSchedule.update {
                text = getString(R.string.group_schedule)
                rightIconVisibility = View.VISIBLE
            }
            labelGroupSchedule.doOnClick {
                ScheduleListActivity.start(groupId)
            }

            btnRemove.doOnClick {
                SmartDialog.build(supportFragmentManager, lifecycleOwner)
                    .setTitle(getString(R.string.dismiss_group))
                    .setContentText(String.format(getString(R.string.group_remove_tip), group.name))
                    .setPositiveTextName(getString(R.string.confirm))
                    .setOnPositive {
                        viewModel.dismissGroup(groupId).observe(lifecycleOwner) {
                            finish()
                        }
                        it.dismiss()
                    }
                    .setNegativeTextName(getString(R.string.cancel))
                    .setOnNegative {
                        it.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTimerWithTask(groupId.toString(), groupId.toString())
            .observe(lifecycleOwner) {
                binding.labelGroupSchedule.update {
                    subText = it.timerList.orEmpty().size.toString()
                }
            }
    }

    private val inputTextLauncher = registerForActivityResult(InputTextContract()) {
        if (!it.isNullOrEmpty()) {
            TuyaHomeSdk.newGroupInstance(groupId).renameGroup(it, object : IResultCallback {
                override fun onSuccess() {
                    binding.labelGroupName.update { subText = it }
                }

                override fun onError(code: String?, error: String?) {
                    toast(error)
                }
            })
        }
    }

    companion object {
        fun start(groupId: Long) =
            startActivity<GroupSettingActivity>(KEY_GROUP_ID to groupId)
    }
}