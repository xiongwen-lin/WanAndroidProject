package com.apemans.dmcomponent.ui.pairdevice.connectwifi.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.quickui.multitype.getItem
import com.apemans.dmcomponent.databinding.DeviceItemConnectMethodBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */

data class ConnectMethodItem(
    val icon: Int,
    val name: String
)

class ConnectMethodViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<String>
) : ItemViewDelegate<ConnectMethodItem, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemConnectMethodBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemConnectMethodBinding>(parent)
            .onItemClick(listener) {
                getItem<ConnectMethodItem>(it).name
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemConnectMethodBinding>,
        item: ConnectMethodItem
    ) {
        with(holder.binding) {
            ivIcon.load(item.icon)
            tvName.text = item.name
        }
    }
}