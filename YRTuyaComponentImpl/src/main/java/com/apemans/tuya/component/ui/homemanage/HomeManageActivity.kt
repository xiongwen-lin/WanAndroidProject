package com.apemans.tuya.component.ui.homemanage

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.ui.toolbar.updateToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityHomeManageBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.addguests.AddGuestActivity
import com.apemans.tuya.component.ui.addowner.AddOwnerActivity
import com.apemans.tuya.component.ui.homemanage.items.HomeDeviceViewDelegate
import com.apemans.tuya.component.ui.homemanage.items.HomeGuestsViewDelegate
import com.apemans.tuya.component.ui.homemanage.items.HomeOwnerViewDelegate
import com.apemans.tuya.component.ui.homesetting.HomeSettingActivity

class HomeManageActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityHomeManageBinding>() {

    private val title: String by safeIntentExtras(KEY_TITLE)
    private val homeId: Long by safeIntentExtras(KEY_HOME_ID)
    private val ownerAdapter = MultiTypeAdapter(HomeOwnerViewDelegate())
    private val guestsAdapter = MultiTypeAdapter(HomeGuestsViewDelegate())
    private val devicesAdapter = MultiTypeAdapter(HomeDeviceViewDelegate())
    private val viewModel: HomeManageViewModel by requestViewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            title = this@HomeManageActivity.title
            rightIcon(R.drawable.menu_setting_icon_state_list) {
                HomeSettingActivity.start(homeId)
            }
        }
        with(binding) {
            rvOwner.adapter = ownerAdapter
            rvGuests.adapter = guestsAdapter
            rvDevices.adapter = devicesAdapter
            btnAddOwner.doOnClick { AddOwnerActivity.start(homeId) }
            btnAddGuests.doOnClick { AddGuestActivity.start(homeId) }
        }
        ownerAdapter.observeItemsChanged(this, viewModel.queryMemberList(homeId)) { old, new ->
            old.memberId == new.memberId
        }
        guestsAdapter.observeItemsChanged(this, viewModel.queryUserShareList(homeId)) { old, new ->
            old.memeberId == new.memeberId
        }
        devicesAdapter.observeItemsChanged(this, viewModel.queryHomeDeviceList(homeId)) { old, new ->
            old.devId == new.devId
        }
        sharedViewModel.removeHomeEvent.asLiveData().observe(this) {
            finish()
        }
        sharedViewModel.renameHomeEvent.asLiveData().observe(this) {
            updateToolbar { title = it }
        }
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_HOME_ID = "home_id"

        fun start(title: String, homeId: Long) {
            startActivity<HomeManageActivity>(
                KEY_TITLE to title,
                KEY_HOME_ID to homeId,
            )
        }
    }
}