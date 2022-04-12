package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SHOOTING_VIDEO_IMAGE
import com.apemans.quickui.multitype.getItem
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.databinding.DeviceItemIpcConfigureSelectorBinding
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.widget.BaseCheckableItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.dylanc.longan.application

/**
 * @author Dylan Cai
 */

class IpcShootingModeViewDelegate(
    private var owner: LifecycleOwner,
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<Int>>,
) :
    BaseCheckableItemViewDelegate<MultiViewDelegateItem<Int>, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcConfigureSelectorBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcConfigureSelectorBinding>(parent)
            .onItemClick {
                listener.onItemClick(getItem(it), it)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcConfigureSelectorBinding>,
        item: MultiViewDelegateItem<Int>
    ) {
        refreshView(holder.binding, item)
    }

    private fun refreshView(view: DeviceItemIpcConfigureSelectorBinding, data: MultiViewDelegateItem<Int>) {
        val mediaMode = data?.data ?: IPC_SCHEME_SHOOTING_VIDEO_IMAGE
        val isSelected = data?.isSelected ?: false
        with(view) {
            deviceIpcConfigureSelectorItem.setupConfigure(owner)
            deviceIpcConfigureSelectorItem.updateUiState {
                it?.apply {
                    text = DeviceControlHelper.convertMediaModeTitleByMode(application, mediaMode)
                    rightIconVisibility = if (isSelected) View.VISIBLE else View.GONE
                }
            }
        }
    }

}