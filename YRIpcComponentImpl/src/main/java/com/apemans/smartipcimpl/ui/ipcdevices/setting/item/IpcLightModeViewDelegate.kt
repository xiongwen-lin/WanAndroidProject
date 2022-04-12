package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.apemans.ipcchipproxy.define.IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
import com.apemans.quickui.multitype.getItem
import com.apemans.ipcchipproxy.scheme.bean.LightInfo
import com.apemans.quickui.multitype.CheckType
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.databinding.DeviceItemIpcFlashLightConfigureBinding
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.widget.BaseCheckableItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.dylanc.longan.application

/**
 * @author Dylan Cai
 */

class IpcLightModeViewDelegate(
    private var owner: LifecycleOwner,
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<LightInfo>>,
    type: CheckType,
    listenerBlock: ((Int, MultiViewDelegateItem<LightInfo>?, Any?) -> Unit)
) :
    BaseCheckableItemViewDelegate<MultiViewDelegateItem<LightInfo>, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcFlashLightConfigureBinding>>(type, listenerBlock) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcFlashLightConfigureBinding>(parent)
            .onItemClick {
                listener.onItemClick(getItem(it), it)
            }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcFlashLightConfigureBinding>,
        item: MultiViewDelegateItem<LightInfo>
    ) {
        refreshView(holder.binding, item)
    }

    private fun refreshView(view: DeviceItemIpcFlashLightConfigureBinding, data: MultiViewDelegateItem<LightInfo>) {
        val lightMode = data?.data?.mode ?: IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
        val isSelected = data?.isSelected ?: false
        with(view) {
            deviceIpcFlashLightItem.setupConfigure(owner)
            deviceIpcFlashLightItem.updateUiState {
                it?.apply {
                    text = DeviceControlHelper.convertFlashLightStateTitleByMode(application, lightMode)
                    detailText = DeviceControlHelper.convertFlashLightStateContentByMode(application, lightMode)
                    rightIconVisibility = if (isSelected) View.VISIBLE else View.GONE
                }
            }
        }
    }

}