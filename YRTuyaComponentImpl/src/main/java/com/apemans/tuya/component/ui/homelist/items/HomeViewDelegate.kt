package com.apemans.tuya.component.ui.homelist.items

import android.content.Context
import android.view.ViewGroup
import com.apemans.quickui.multitype.getItem
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.tuya.component.databinding.TuyaItemHomeBinding
import com.apemans.tuya.component.ui.homemanage.HomeManageActivity
import com.tuya.smart.home.sdk.bean.HomeBean

/**
 * @author Dylan Cai
 */
class HomeViewDelegate : ItemViewDelegate<HomeBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeBinding>(parent)
            .onItemClick {
                val item = getItem<HomeBean>(it)
                HomeManageActivity.start(item.name, item.homeId)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeBinding>,
        item: HomeBean
    ) {
        with(holder.binding) {
            tvHomeName.text = item.name
        }
    }
}