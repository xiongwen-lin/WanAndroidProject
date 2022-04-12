package com.apemans.tuya.component.ui.homelist

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.tuya.module.api.ACTIVITY_PATH_HOME_LIST
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.ui.addhome.AddHomeActivity
import com.apemans.tuya.component.ui.homelist.items.HomeViewDelegate

@Route(path = ACTIVITY_PATH_HOME_LIST)
class HomeListActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CommonLayoutRecyclerViewBinding>() {

    private val adapter = MultiTypeAdapter(HomeViewDelegate())
    private val viewModel: HomeListViewModel by requestViewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            title = "Home manage"
            rightIcon(R.drawable.device_home_add) { startActivity<AddHomeActivity>() }
        }
        binding.recyclerView.adapter = adapter
        adapter.observeItemsChanged(this, viewModel.homeList) { oldItem, newItem ->
            oldItem.homeId == newItem.homeId
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.queryHomeList()
    }
}