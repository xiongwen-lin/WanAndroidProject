package com.apemans.dmcomponent.ui.adddevice.items

import android.content.Context
import android.view.ViewGroup
import com.apemans.quickui.multitype.CheckType
import com.apemans.quickui.multitype.CheckableItemViewDelegate
import com.apemans.quickui.multitype.getItem
import com.apemans.dmcomponent.databinding.DeviceItemDeviceTitleBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class CategoryTitleViewDelegate(
    private val onChecked: (CategoryTitle) -> Unit
) : CheckableItemViewDelegate<CategoryTitle, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTitleBinding>>(CheckType.SINGLE) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTitleBinding>(parent)
            .onItemClick {
                checkItem(it)
                onChecked(getItem(it))
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemDeviceTitleBinding>,
        item: CategoryTitle
    ) {
        holder.binding.apply {
            tvDeviceTitle.text = item.name
            root.isSelected = item.isChecked
        }
    }
}