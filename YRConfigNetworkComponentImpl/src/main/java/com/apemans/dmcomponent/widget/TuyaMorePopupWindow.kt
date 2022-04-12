package com.apemans.dmcomponent.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.dp
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.getItem
import com.apemans.dmcomponent.databinding.DeviceItemMorePopupBinding
import com.apemans.dmcomponent.databinding.DevicePopupTuyaMoreBinding
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */
data class MenuItem(
    @DrawableRes val icon: Int,
    val text: String
)

class TuyaMorePopupWindow(
    context: Context,
    items: List<MenuItem>,
    private val onItemClick: (PopupWindow, MenuItem) -> Unit
) : PopupWindow(context) {

    private val adapter = MultiTypeAdapter(MenuViewDelegate())

    init {
        val binding = DevicePopupTuyaMoreBinding.inflate(LayoutInflater.from(context))
        width = 175.dp.toInt()
        contentView = binding.root
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = true

        adapter.items = items
        binding.recyclerView.adapter = adapter
    }

    private inner class MenuViewDelegate :
        ItemViewDelegate<MenuItem, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemMorePopupBinding>>() {

        override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
            com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemMorePopupBinding>(parent)
                .onItemClick {
                    onItemClick(this@TuyaMorePopupWindow, getItem(it))
                }

        override fun onBindViewHolder(
            holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemMorePopupBinding>,
            item: MenuItem
        ) {
            with(holder.binding) {
                ivIcon.setImageResource(item.icon)
                tvTitle.text = item.text
            }
        }
    }
}