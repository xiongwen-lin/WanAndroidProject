package com.apemans.quickui.preference

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.apemans.quickui.click
import com.apemans.quickui.enable

/***********************************************************
 * @Author : caro
 * @Date   : 1/9/21
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
class PreferenceViewViewHolder<Event : PreferenceSettingEventCallback>(
    private val preferenceView: PreferenceView,
    private val onSettingFuncEventClick: Event,
) :
    RecyclerView.ViewHolder(preferenceView as View) {
    private val context: Context = itemView.context

    fun bind(preferenceItem: PreferenceBean) {
        //设置设置项名称
        preferenceView.setPreferenceName(preferenceItem.name)
        //设置项当前值
        val curValue = preferenceItem.value
        if (curValue!=null) {
            preferenceView.setPreferenceValue(curValue)
        }
        if (!preferenceItem.enable) {
            preferenceView.enable = false
            preferenceView.showArrowRightIcon(false)
        } else {
            preferenceView.enable = true
        }
        if (preferenceItem.iconDrawable!=null){
            preferenceView.setPreferenceIcon(preferenceItem.iconDrawable!!)
        }


        @PreferenceViewType
        val itemType = preferenceItem.itemUIType
        when (itemType) {
            PreferenceViewType.UITypeSwitch -> {
                preferenceView.showSwitch(true)
                preferenceView.enableSwitch(preferenceItem.enable)
                //Log.i("PreferenceViewHolder", "bind: $curValue")
                preferenceView.setSwitchValue(curValue.toString())
                preferenceView.showTextSummary(false)
                preferenceView.showArrowRightIcon(false)
            }
            PreferenceViewType.UITypeNormal -> {
                preferenceView.showSwitch(false)
                preferenceView.showTextSummary(true)
                preferenceView.showArrowRightIcon(true)
            }
        }

        //支持参数
        val supportParams: List<PreferenceBean.Param> = preferenceItem.params
        preferenceView.click {
            onSettingFuncEventClick.onClick(
                absoluteAdapterPosition,
                preferenceItem.value,
                preferenceItem,
                supportParams,
                preferenceView)

        }

        preferenceView.setOnSwitchListener(object :PreferenceView.SwitchListener{
            override fun onSwitchTurn() {
                val fromUser = true
                onSettingFuncEventClick.onSwitchEvent(
                    absoluteAdapterPosition,
                    true,
                    preferenceItem,
                    supportParams,
                    preferenceView,
                    fromUser
                )
            }

            override fun onSwitchOff() {
                val fromUser = true
                onSettingFuncEventClick.onSwitchEvent(
                    absoluteAdapterPosition,
                    false,
                    preferenceItem,
                    supportParams,
                    preferenceView,
                    fromUser
                )
            }

        })

        preferenceView.setOnLongClickListener {
            onSettingFuncEventClick.onLongPress(
                absoluteAdapterPosition,
                curValue,
                preferenceItem,
                supportParams,
                preferenceView
            )
            false
        }
    }
}

