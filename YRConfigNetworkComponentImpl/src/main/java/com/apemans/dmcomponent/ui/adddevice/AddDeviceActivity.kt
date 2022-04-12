package com.apemans.dmcomponent.ui.adddevice

import android.os.Bundle
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.model.DeviceModel
import com.apemans.dmcomponent.databinding.DeviceActivityAddDeviceBinding
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener

/**
 * @author Dylan Cai
 */
class AddDeviceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityAddDeviceBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceModel> {
    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

    override fun onItemClick(item: DeviceModel, position: Int) {

    }
//
//    private val viewModel: AddDeviceViewModel by viewModels()
//    private val categoryTitleViewDelegate = CategoryTitleViewDelegate(::onChecked)
//    private val categoryAdapter = MultiTypeAdapter(categoryTitleViewDelegate)
//    private val deviceAdapter = multiTypeAdapter {
//        register(SortingDeviceViewDelegate(::onItemClick))
//        register(DeviceGroupViewDelegate())
//    }
//
//    override fun onViewCreated(savedInstanceState: Bundle?) {
//        setToolbar("Add device")
//        // TODO: 2021/11/30
//        with(binding) {
//            rvCategory.adapter = categoryAdapter
//            rvDevices.adapter = deviceAdapter
//            val layoutManager = GridLayoutManager(context, 3)
//            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int) =
//                    if (deviceAdapter.items[position] is DeviceGroup) 3 else 1
//            }
//            rvDevices.layoutManager = layoutManager
//
//            deviceAdapter.observeItemsChanged(lifecycleOwner, viewModel.supportDevice) { oldItem, newItem ->
//                (oldItem is DeviceGroup && newItem is DeviceGroup && oldItem.groupingTitle == newItem.groupingTitle)
//                        || (oldItem is DeviceModel && newItem is DeviceModel && oldItem.deviceInfo.name == newItem.deviceInfo.name)
//            }
//            categoryAdapter.items = viewModel.supportCategories.map { CategoryTitle(it) }
//            categoryTitleViewDelegate.checkItem(0)
//        }
//        viewModel.getSupportDevice(getString(R.string.electrical))
//    }
//
//    override fun onItemClick(item: DeviceModel, position: Int) {
//        PairDevGuideActivity.openPairDevGuideActivity(context, item)
//    }
//
//    private fun onChecked(title: CategoryTitle) {
////        viewModel.getSupportDevice(title.name)
//    }
}