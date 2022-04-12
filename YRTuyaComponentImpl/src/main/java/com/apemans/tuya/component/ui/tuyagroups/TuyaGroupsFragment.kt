package com.apemans.tuya.component.ui.tuyagroups

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseFragment
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.base.utils.SpacesItemDecoration
import com.apemans.tuya.module.api.FRAGMENT_PATH_TUYA_GROUPS
import com.drakeet.multitype.KotlinClassLinker
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.dp
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.tuyagroups.items.GridGroupViewDelegate
import com.apemans.tuya.component.ui.tuyagroups.items.LearGroupViewDelegate
import com.tuya.smart.sdk.bean.GroupBean

/**
 * @author Dylan Cai
 */
@Route(path = FRAGMENT_PATH_TUYA_GROUPS)
class TuyaGroupsFragment : com.apemans.yruibusiness.base.BaseFragment<CommonLayoutRecyclerViewBinding>(), KotlinClassLinker<GroupBean> {

    private val viewModel: TuyaGroupsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()
    private val adapter = MultiTypeAdapter(GridGroupViewDelegate(), LearGroupViewDelegate(), linker = this)

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
                oldItem.id == newItem.id
            }
        }
    }

    override fun index(position: Int, item: GroupBean) =
        if (sharedViewModel.isSingleRowType.value == true) {
            LearGroupViewDelegate::class
        } else {
            GridGroupViewDelegate::class
        }

}
