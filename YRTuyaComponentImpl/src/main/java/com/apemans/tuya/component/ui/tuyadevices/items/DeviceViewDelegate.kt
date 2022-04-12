package com.apemans.tuya.component.ui.tuyadevices.items

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
import com.dylanc.longan.topActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.isOpen
import com.tuya.smart.api.MicroContext
import com.tuya.smart.panelcaller.api.AbsPanelCallerService
import com.tuya.smart.sdk.bean.DeviceBean

/**
 * @author Dylan Cai
 */
class LearDeviceViewDelegate(onOpen: (DeviceBean, Boolean) -> Unit) :
    BaseDeviceViewDelegate(R.layout.tuya_item_linear_device, onOpen)

class GridDeviceViewDelegate(onOpen: (DeviceBean, Boolean) -> Unit) :
    BaseDeviceViewDelegate(R.layout.tuya_item_grid_device, onOpen)

abstract class BaseDeviceViewDelegate(
    private val layoutId: Int,
    private val onOpen: (DeviceBean, Boolean) -> Unit
) : ItemViewDelegate<DeviceBean, BaseDeviceViewDelegate.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false)).apply {
            itemView.setOnClickListener {
                val deviceBean: DeviceBean = getItem(bindingAdapterPosition)
                val service = MicroContext.getServiceManager().findServiceByInterface<AbsPanelCallerService>(AbsPanelCallerService::class.java.name)
                service.goPanelWithCheckAndTip(topActivity, deviceBean.devId)
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, item: DeviceBean) {
        with(holder) {
            tvDeviceName.text = item.name
            ivDeviceIcon.load(item.iconUrl) {
                placeholder(R.drawable.device_list_placeholder)
                error(R.drawable.device_list_placeholder)
                crossfade(true)
            }
            if (item.isOnline) {
                tvOnlineState.setText(R.string.online)
                ivOnlineStatus.setBackgroundColor(Color.parseColor("#5ee884"))
            } else {
                tvOnlineState.setText(R.string.offline)
                ivOnlineStatus.setBackgroundColor(Color.parseColor("#FF414245"))
            }
            btnSwitch.isVisible = item.isOnline
            if (item.isOnline) {
                btnSwitch.isSelected = item.isOpen
                btnSwitch.doOnClick {
                    onOpen(item, !item.isOpen)
                }
            }
            backgroundOffline.isVisible = !item.isOnline
        }
    }

    data class Schema(
        val code: String,
        val id: Int
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDeviceName: TextView = itemView.findViewById(R.id.tv_device_name)
        val tvOnlineState: TextView = itemView.findViewById(R.id.tv_online_state)
        val ivOnlineStatus: ImageView = itemView.findViewById(R.id.iv_online_status)
        val ivDeviceIcon: ImageView = itemView.findViewById(R.id.iv_device_icon)
        val btnSwitch: View = itemView.findViewById(R.id.btn_switch)
        val backgroundOffline: View = itemView.findViewById(R.id.background_offline)
    }
}