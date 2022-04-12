package com.apemans.tuya.component.ui.homemanage.items

import android.content.Context
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.apemans.quickui.multitype.getItem
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.tuya.component.R
import com.drakeet.multitype.ItemViewDelegate
import com.apemans.tuya.component.databinding.TuyaItemHomeOwnerBinding
import com.apemans.tuya.component.ui.familymember.FamilyMemberActivity
import com.tuya.smart.home.sdk.bean.SharedUserInfoBean

/**
 * @author Dylan Cai
 */
class HomeGuestsViewDelegate :
    ItemViewDelegate<SharedUserInfoBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>(parent)
            .onItemClick {
                val item: SharedUserInfoBean = getItem(it)
                FamilyMemberActivity.start(item.memeberId, item.userName, item.userName, item.iconUrl,false)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>,
        item: SharedUserInfoBean
    ) {
        with(holder.binding) {
            tvName.text = item.userName
            ivIcon.load(item.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
                transformations(CircleCropTransformation())
            }
        }
    }
}