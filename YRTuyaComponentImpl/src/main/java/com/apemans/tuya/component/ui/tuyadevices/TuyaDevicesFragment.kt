package com.apemans.tuya.component.ui.tuyadevices

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseFragment
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.base.utils.SpacesItemDecoration
import com.apemans.tuya.module.api.FRAGMENT_PATH_TUYA_DEVICES
import com.drakeet.multitype.KotlinClassLinker
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.dp
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.tuyadevices.items.GridDeviceViewDelegate
import com.apemans.tuya.component.ui.tuyadevices.items.LearDeviceViewDelegate
import com.tuya.smart.sdk.bean.DeviceBean

/**
 * @author Dylan Cai
 */
@Route(path = FRAGMENT_PATH_TUYA_DEVICES)
class TuyaDevicesFragment : com.apemans.yruibusiness.base.BaseFragment<CommonLayoutRecyclerViewBinding>(), KotlinClassLinker<DeviceBean> {

    private val adapter = MultiTypeAdapter(
        GridDeviceViewDelegate(::onItemSwitchClick), LearDeviceViewDelegate(::onItemSwitchClick), linker = this
    )
    private val viewModel: TuyaDevicesViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()

    override fun onViewCreated(root: View) {
        with(binding) {
            recyclerView.adapter = adapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.layoutManager = gridLayoutManager
            sharedViewModel.isSingleRowType.observe(viewLifecycleOwner) {
                gridLayoutManager.spanCount = if (it) 1 else 2
            }
            recyclerView.addItemDecoration(SpacesItemDecoration(20.dp, 20.dp))
            adapter.observeItemsChanged(viewLifecycleOwner, viewModel.deviceList) { oldItem, newItem ->
                oldItem.devId == newItem.devId
            }
        }
    }

    private fun onItemSwitchClick(deviceBean: DeviceBean, isOpen: Boolean) {
        viewModel.sendCommand(deviceBean, isOpen).observe(viewLifecycleOwner) {
        }
    }

    override fun index(position: Int, item: DeviceBean) =
        if (sharedViewModel.isSingleRowType.value == true) {
            LearDeviceViewDelegate::class
        } else {
            GridDeviceViewDelegate::class
        }
}
