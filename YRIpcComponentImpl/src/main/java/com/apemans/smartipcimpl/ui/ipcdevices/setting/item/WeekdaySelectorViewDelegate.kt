package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.ViewGroup
import com.apemans.quickui.multitype.CheckType
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.databinding.DeviceItemWeekdaySelectorBinding
import com.apemans.smartipcimpl.widget.BaseCheckableItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
class WeekdaySelectorViewDelegate(
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<String>>
) :
    BaseCheckableItemViewDelegate<MultiViewDelegateItem<String>, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemWeekdaySelectorBinding>>(type = CheckType.MULTIPLE) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemWeekdaySelectorBinding>(parent)
            .onItemClick { checkItem(it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemWeekdaySelectorBinding>,
        item: MultiViewDelegateItem<String>
    ) {
        with(holder.binding) {
            deviceItemWeekdaySelector.setText(item.data)
            val selectorBgRes = if (item.isChecked) R.drawable.device_weekday_selector_checked_bg else R.drawable.device_weekday_selector_bg
            deviceItemWeekdaySelector.setBackgroundResource(selectorBgRes)
        }
    }
}