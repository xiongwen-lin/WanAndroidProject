package com.apemans.dmcomponent.ui.pairdevice.savename.items

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.dmcomponent.databinding.DeviceItemRecommendedNameBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.quickui.multitype.getItem

/**
 * @author Dylan Cai
 */
class RecommendedNameViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<Int>
) : ItemViewDelegate<Int, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameBinding>(parent)
            .onItemClick(listener) { getItem(it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameBinding>,
        item: Int
    ) {
        with(holder.binding) {
            tvLabel.setText(item)
        }
    }
}