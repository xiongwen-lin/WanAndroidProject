package com.apemans.tuya.component.ui.tuyagroups.items

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apemans.quickui.multitype.getItem
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.doOnClick
import com.apemans.tuya.component.R
import com.apemans.tuya.component.ui.group.GroupActivity
import com.tuya.smart.sdk.bean.GroupBean

/**
 * @author Dylan Cai
 */

class LearGroupViewDelegate : BaseGroupViewDelegate(R.layout.tuya_item_linear_group)

class GridGroupViewDelegate : BaseGroupViewDelegate(R.layout.tuya_item_grid_group)

abstract class BaseGroupViewDelegate(
    private val layoutId: Int
) : ItemViewDelegate<GroupBean, BaseGroupViewDelegate.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false)).apply {
            itemView.doOnClick {
                val group: GroupBean = getItem(bindingAdapterPosition)
                GroupActivity.start(group.id)
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, item: GroupBean) {
        with(holder) {
            tvDeviceName.text = item.name
            ivDeviceIcon.load(item.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
                crossfade(true)
            }
            if (item.isOnline) {
                ivOnlineStatus.setBackgroundColor(Color.parseColor("#5ee884"))
            } else {
                ivOnlineStatus.setBackgroundColor(Color.parseColor("#FF414245"))
            }
            btnSwitch.isVisible = item.isOnline
            backgroundOffline.isVisible = !item.isOnline
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDeviceName: TextView = itemView.findViewById(R.id.tv_device_name)
        val ivOnlineStatus: ImageView = itemView.findViewById(R.id.iv_online_status)
        val ivDeviceIcon: ImageView = itemView.findViewById(R.id.iv_device_icon)
        val btnSwitch: View = itemView.findViewById(R.id.btn_switch)
        val backgroundOffline: View = itemView.findViewById(R.id.background_offline)
    }
}