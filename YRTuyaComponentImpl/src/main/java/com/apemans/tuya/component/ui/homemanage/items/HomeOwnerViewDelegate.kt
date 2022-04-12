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
import com.tuya.smart.home.sdk.bean.MemberBean

/**
 * @author Dylan Cai
 */
class HomeOwnerViewDelegate :
    ItemViewDelegate<MemberBean, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>(parent)
            .onItemClick {
                val item: MemberBean = getItem(it)
                FamilyMemberActivity.start(item.memberId, item.nickName, item.account, item.headPic, true)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemHomeOwnerBinding>,
        item: MemberBean
    ) {
        with(holder.binding) {
            tvName.text = item.nickName
            ivIcon.load(item.headPic) {
                placeholder(R.drawable.user)
                error(R.drawable.user)
                transformations(CircleCropTransformation())
            }
        }
    }
}