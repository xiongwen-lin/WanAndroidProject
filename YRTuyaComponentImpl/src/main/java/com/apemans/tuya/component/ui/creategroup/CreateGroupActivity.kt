package com.apemans.tuya.component.ui.creategroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.module.api.ACTIVITY_PATH_CREATE_GROUP
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_HOME_ID
import com.apemans.tuya.component.databinding.TuyaActivityCreateGroupBinding
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.creategroup.CreateGroupContract.Companion.KEY_CHECK_IDS
import com.apemans.tuya.component.ui.creategroup.CreateGroupContract.Companion.KEY_PRODUCT_ID
import com.apemans.tuya.component.ui.creategroup.lamp.LampGroupFragment
import com.apemans.tuya.component.ui.creategroup.plug.PlugGroupFragment
import com.dylanc.longan.design.FragmentStateAdapter
import com.dylanc.longan.design.setupWithViewPager2

@Route(path = ACTIVITY_PATH_CREATE_GROUP)
class CreateGroupActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityCreateGroupBinding>() {

    private val viewModel: CreateGroupViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()
    private val homeId: Long by intentExtras(KEY_HOME_ID, TuyaRepository.selectedHomeId)
    private val titleList = listOf(R.string.plug, R.string.light)
    private val fragments by lazy {
        listOf(PlugGroupFragment.newInstance(homeId), LampGroupFragment.newInstance(homeId))
            .map { it as GroupDeviceFragment }.toTypedArray()
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            titleRes = R.string.create_group
            rightIcon(R.drawable.device_nav_confirm_on, ::onRightBtnClick)
        }
        with(binding) {
            viewPager2.adapter = FragmentStateAdapter(fragments = fragments)
            tabLayout.setupWithViewPager2(viewPager2) { tab, i ->
                tab.setText(titleList[i])
            }
        }
        sharedViewModel.saveGroupEvent.asLiveData().observe(this) {
            finish()
        }
    }

    private fun onRightBtnClick(v: View) {
        val currentItem = binding.viewPager2.currentItem
        val fragment = fragments[currentItem]
        finishWithResult(
            KEY_CHECK_IDS to fragment.getCheckedIds().toTypedArray(),
            KEY_PRODUCT_ID to fragment.productId
        )
    }

    companion object {
        fun start(homeId: Long) {
            startActivity<CreateGroupActivity>(KEY_HOME_ID to homeId)
        }
    }
}

class CreateGroupContract : ActivityResultContract<Long, CreateGroupInfo?>() {
    override fun createIntent(context: Context, input: Long?) =
        context.intentOf<CreateGroupActivity>(KEY_HOME_ID to input)

    override fun parseResult(resultCode: Int, intent: Intent?): CreateGroupInfo? =
        intent?.takeIf { resultCode == Activity.RESULT_OK }?.let {
            CreateGroupInfo(
                productId = it.getStringExtra(KEY_PRODUCT_ID).orEmpty(),
                deviceIds = it.getStringArrayExtra(KEY_CHECK_IDS)?.toList() ?: emptyList()
            )
        }

    companion object {
        const val KEY_CHECK_IDS = "check_ids"
        const val KEY_PRODUCT_ID = "product_id"
    }
}

data class CreateGroupInfo(
    val productId: String,
    val deviceIds: List<String>
)