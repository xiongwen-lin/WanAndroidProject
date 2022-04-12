package com.apemans.tuya.component.ui.scheduleaction.items

import android.content.Context
import android.view.ViewGroup
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.quickui.multitype.CheckType
import com.apemans.quickui.multitype.CheckableItemViewDelegate
import com.apemans.quickui.multitype.ICheckable
import com.apemans.tuya.component.databinding.TuyaItemWeekBinding

data class Week(val week: Int,val calender:Int, override var isChecked: Boolean = false) : ICheckable

class WeekViewDelegate : CheckableItemViewDelegate<Week, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemWeekBinding>>(CheckType.MULTIPLE) {
    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemWeekBinding>(parent)
            .onItemClick {
                checkItem(it)
            }

    override fun onBindViewHolder(holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemWeekBinding>, item: Week) {
        holder.binding.apply {
            btnSelectMon.setText(item.week)
            btnSelectMon.isSelected = item.isChecked
        }
    }
}