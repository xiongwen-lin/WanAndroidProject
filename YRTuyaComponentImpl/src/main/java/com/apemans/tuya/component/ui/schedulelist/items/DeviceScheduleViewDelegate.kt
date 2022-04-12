package com.apemans.tuya.component.ui.schedulelist.items

import android.content.Context
import android.content.res.Resources
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.onItemClick
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaItemDeviceScheduleBinding
import com.apemans.tuya.component.utils.StringUtil
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.longan.application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tuya.smart.sdk.bean.Timer

class DeviceScheduleViewDelegate : ItemViewDelegate<Timer, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceScheduleBinding>>() {
    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceScheduleBinding>(parent)
            .onItemClick {
//                ScheduleActionActivity.start()
            }

    override fun onBindViewHolder(holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<TuyaItemDeviceScheduleBinding>, item: Timer) {
        holder.binding.apply {
            tvItemScheduleFromTime.text = item.time
            tvItemScheduleWeekDays.text = getLooperDes(item.loops)
            val map: Map<String, Any?>? = item.value?.let {
                Gson().fromJson(it, object : TypeToken<Map<String, Any?>>() {}.type)
            }
            if (map?.isNotEmpty() == true && !item.dpId.isNullOrEmpty()) {
                val isOn = map[item.dpId] as Boolean? == true
                tvItemScheduleFromState.text = if (isOn) "ON" else "OFF"
            }
            vItemScheduleCenter.isVisible = false
            tvItemScheduleToTime.isVisible = false
            tvItemScheduleToState.isVisible = false
            ivCycleTime.isVisible = false
            tvRandomTime.isVisible = false

            val isOpen = item.status == 1
            if (btnItemScheduleSwitch.isChecked != isOpen) {
                btnItemScheduleSwitch.toggleNoCallback()
            }
        }
    }

    private fun getLooperDes(looper: String): String {
        val resources: Resources = application.resources
        if (looper == "0000000") {
            return resources.getString(R.string.once)
        }
        if (looper == "1111111") {
            return resources.getString(R.string.every_day)
        }
        val strList: List<String> = StringUtil.getStrList(looper, 1)
        val sbValue = StringBuffer()
        for (i in 1 until strList.size) {
            if (strList[i] == "1" && i == 1) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.mon))
                continue
            }
            if (strList[i] == "1" && i == 2) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.tues))
                continue
            }
            if (strList[i] == "1" && i == 3) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.wed))
                continue
            }
            if (strList[i] == "1" && i == 4) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.thurs))
                continue
            }
            if (strList[i] == "1" && i == 5) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.fri))
                continue
            }
            if (strList[i] == "1" && i == 6) {
                if (sbValue.isNotEmpty()) {
                    sbValue.append(" ")
                }
                sbValue.append(resources.getString(R.string.sat))
                continue
            }
        }
        if (strList[0] == "1") {
            if (sbValue.isNotEmpty()) {
                sbValue.append(" ")
            }
            sbValue.append(resources.getString(R.string.sun))
        }
        return sbValue.toString()
    }
}