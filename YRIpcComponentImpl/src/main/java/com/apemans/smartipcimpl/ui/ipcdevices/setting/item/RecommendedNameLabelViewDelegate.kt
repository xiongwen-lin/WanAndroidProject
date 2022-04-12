package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.ViewGroup
import com.apemans.quickui.multitype.getItem
import com.apemans.smartipcimpl.databinding.DeviceItemRecommendedNameLabelBinding
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class RecommendedNameLabelViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<String>
) :
    ItemViewDelegate<String, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameLabelBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameLabelBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemRecommendedNameLabelBinding>,
        item: String
    ) {
        with(holder.binding) {
            deviceRecommendedNameLabel.setText(item)
        }
    }
}