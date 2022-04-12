package com.apemans.tdprintercomponentimpl.ui.setting.item

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.application
import com.apemans.quickui.multitype.getItem
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.API_BIND_TYPE_OWNER
import com.apemans.tdprintercomponentimpl.constant.API_BIND_TYPE_SHARER
import com.apemans.tdprintercomponentimpl.databinding.TdItemDeviceBinderBinding
import com.apemans.tdprintercomponentimpl.webapi.DeviceRelation

/**
 * @author Dylan Cai
 */
class TDPrinterBinderViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceRelation>
) :
    ItemViewDelegate<DeviceRelation, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TdItemDeviceBinderBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TdItemDeviceBinderBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TdItemDeviceBinderBinding>,
        item: DeviceRelation
    ) {
        refreshView(holder.binding, item)
    }

    private fun refreshView(view: TdItemDeviceBinderBinding, relation: DeviceRelation) {
        with(view) {
            tdItemDeviceBinderThumbnail.isVisible = false
            tdItemDeviceBinderName.text = relation.account
            tdItemDeviceBinderState.text = convertRelationBindingState(relation.type)
        }
    }

    private fun convertRelationBindingState(bindType: Int) : String {
        return when(bindType) {
            API_BIND_TYPE_OWNER -> { application.getString(R.string.camera_share_title) }
            API_BIND_TYPE_SHARER -> { application.getString(R.string.camera_share_title) }
            else -> { "" }
        }
    }
}