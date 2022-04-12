package com.apemans.tuya.component.ui.creategroup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.base.databinding.CommonLayoutRecyclerViewBinding
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.submitItems
import com.apemans.base.utils.SpacesItemDecoration
import com.dylanc.longan.dp
import com.apemans.tuya.component.ui.creategroup.items.GroupDevice
import com.apemans.tuya.component.ui.creategroup.items.SelectDeviceViewDelegate
import com.tuya.smart.sdk.bean.GroupDeviceBean

/**
 * @author Dylan Cai
 */
abstract class GroupDeviceFragment : com.apemans.yruibusiness.base.BaseComponentFragment<CommonLayoutRecyclerViewBinding>() {

    private val viewDelegate = SelectDeviceViewDelegate()
    private val adapter = MultiTypeAdapter(viewDelegate)

    override fun onViewCreated(root: View) {
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        binding.recyclerView.apply {
            adapter = this@GroupDeviceFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpacesItemDecoration(16.dp, 16.dp))
        }
        queryList()
    }

    protected fun submitList(liveData: LiveData<List<GroupDeviceBean>>) {
        liveData.observe(viewLifecycleOwner) { list ->
            adapter.submitItems(list.map { GroupDevice(it) }) { oldItem, newItem ->
                oldItem.device.deviceBean.devId == newItem.device.deviceBean.devId
            }
        }
    }

    fun getCheckedIds() = viewDelegate.getCheckedItems().map { it.device.deviceBean.devId }

//    protected abstract val list: LiveData<List<GroupDeviceBean>>

    protected abstract fun queryList()

    abstract val productId:String
}