package com.apemans.tuya.component.ui.devicesharelist.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.quickui.multitype.getItem
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaItemDeviceShareUsersBinding
import com.tuya.smart.home.sdk.bean.SharedUserInfoBean

class DeviceShareUserDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<SharedUserInfoBean>
) : ItemViewDelegate<SharedUserInfoBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceShareUsersBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceShareUsersBinding>(parent)
            .onItemClick(listener) { getItem(it) }

    override fun onBindViewHolder(holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceShareUsersBinding>, item: SharedUserInfoBean) {
        holder.binding.apply {
            tvShareUserName.text = if (item.remarkName.isNullOrEmpty() && item.remarkName != "注册用户") {
                item.remarkName
            } else {
                item.userName
            }
            ivShareUser.load(item.iconUrl) {
                placeholder(R.drawable.user)
                error(R.drawable.user)
            }
        }
    }
}