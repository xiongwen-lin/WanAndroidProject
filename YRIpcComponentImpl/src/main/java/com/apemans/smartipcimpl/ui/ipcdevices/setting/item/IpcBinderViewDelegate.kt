package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.application
import com.apemans.quickui.multitype.getItem
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcapi.webapi.DeviceRelation
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceItemIpcBinderBinding
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class IpcBinderViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceRelation>
) :
    ItemViewDelegate<DeviceRelation, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcBinderBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcBinderBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcBinderBinding>,
        item: DeviceRelation
    ) {
        refreshView(holder.binding, item)
    }

    private fun refreshView(view: DeviceItemIpcBinderBinding, relation: DeviceRelation) {
        with(view) {
            deviceItemIpcBinderThumbnail.visibilityEnable { false }
            deviceItemIpcBinderName.text = relation.account
            deviceItemIpcBinderState.text = convertRelationBindingState(relation.type)
        }
    }

    private fun convertRelationBindingState(bindType: Int) : String {
        return when(bindType) {
            DeviceDefine.BIND_TYPE_OWNER -> { application.getString(R.string.camera_share_title) }
            DeviceDefine.BIND_TYPE_SHARER -> { application.getString(R.string.camera_share_title) }
            else -> { "" }
        }
    }
}