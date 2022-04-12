package com.apemans.tuya.component.ui.scheduleaction

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.GroupType
import com.apemans.tuya.component.constants.KEY_GROUP_ID
import com.apemans.tuya.component.constants.KEY_TYPE
import com.apemans.tuya.component.ui.scheduleaction.adapter.HourWheelAdapter
import com.apemans.tuya.component.ui.scheduleaction.adapter.MinutesWheelAdapter
import com.apemans.tuya.component.ui.scheduleaction.items.Week
import com.apemans.tuya.component.ui.scheduleaction.items.WeekViewDelegate
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.databinding.TuyaActivityScheduleActionBinding
import com.dylanc.longan.doOnClick
import java.util.*

class ScheduleActionActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityScheduleActionBinding>() {

    private val hourWheelAdapter = HourWheelAdapter()
    private val minutesWheelAdapter = MinutesWheelAdapter()
    private val delegate = WeekViewDelegate()
    private val adapter = MultiTypeAdapter(delegate)
    private val viewModel: ScheduleActionViewModel by viewModels()
    private val groupId: Long by safeIntentExtras(KEY_GROUP_ID)
    private val groupType: String by safeIntentExtras(KEY_TYPE)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.normal_timer) {
            rightIcon(R.drawable.define_black) {
                val hour = hourWheelAdapter.getValue(binding.wheelStartHour.currentItem)
                val min = minutesWheelAdapter.getValue(binding.wheelStartMinutes.currentItem)
                val checkedItems = delegate.getCheckedItems().map { it.calender }
                val loops = getLooper(checkedItems)
                viewModel.addTimerWithTask(
                    groupId.toString(), groupId.toString(), loops,
                    mapOf((if (groupType == GroupType.PLUG) "1" else "20") to isOn), "$hour:$min"
                ).observe(lifecycleOwner) {
                    finish()
                }
            }
        }
        binding.apply {
            val now = Calendar.getInstance().apply { time = Date() }
            wheelStartHour.adapter = hourWheelAdapter
            wheelStartMinutes.currentItem = now.get(Calendar.HOUR_OF_DAY)
            wheelStartMinutes.adapter = minutesWheelAdapter
            wheelStartMinutes.currentItem = now.get(Calendar.MINUTE)
            btnCreateScheduleOn.tag = SWITCH_ON
            switchBtnOnAndOff(true)
            btnCreateScheduleOn.doOnClick { toggleSwitchBtn() }
            btnCreateScheduleOff.doOnClick { toggleSwitchBtn() }
            adapter.items = listOf(
                Week(R.string.mon_upper, Calendar.MONDAY),
                Week(R.string.tues_upper, Calendar.TUESDAY),
                Week(R.string.wed_upper, Calendar.WEDNESDAY),
                Week(R.string.thur_upper, Calendar.THURSDAY),
                Week(R.string.fri_upper, Calendar.FRIDAY),
                Week(R.string.sat_upper, Calendar.SATURDAY),
                Week(R.string.sun_upper, Calendar.SUNDAY),
            )
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(context, 7)
        }
    }

    private val isOn: Boolean get() = (binding.btnCreateScheduleOn.tag as Int) == SWITCH_ON

    private fun updateFromAndToView() {
        binding.apply {
            val itemHeight = wheelStartHour.itemHeight as Int + 2
            val layoutParams: ViewGroup.LayoutParams = tvAtLabel.layoutParams
            layoutParams.height = wheelStartHour.itemHeight as Int + 2
            tvAtLabel.layoutParams = layoutParams
            val layoutParams2: ViewGroup.LayoutParams = tvDay.layoutParams
            layoutParams2.height = itemHeight
            tvDay.layoutParams = layoutParams2
        }
    }

    private fun toggleSwitchBtn() {
        binding.apply {
            if (btnCreateScheduleOn.tag as Int == SWITCH_ON) {
                btnCreateScheduleOn.tag = SWITCH_OFF
                switchBtnOnAndOff(false)
            } else {
                btnCreateScheduleOn.tag = SWITCH_ON
                switchBtnOnAndOff(true)
            }
        }
    }

    private fun switchBtnOnAndOff(on: Boolean) {
        binding.apply {
            if (on) {
                btnCreateScheduleOn.setTextColor(resources.getColor(android.R.color.white))
                btnCreateScheduleOff.setTextColor(resources.getColor(android.R.color.white))
                btnCreateScheduleOn.setBackgroundResource(R.drawable.button_schedule_action_on_radius_20)
                btnCreateScheduleOff.setBackgroundResource(R.drawable.button_schedule_action_off_radius_20)
            } else {
                btnCreateScheduleOn.setTextColor(resources.getColor(android.R.color.white))
                btnCreateScheduleOff.setTextColor(resources.getColor(android.R.color.white))
                btnCreateScheduleOn.setBackgroundResource(R.drawable.button_schedule_action_off_radius_20)
                btnCreateScheduleOff.setBackgroundResource(R.drawable.button_schedule_action_on_radius_20)
            }
        }
    }

    private fun getLooper(weekList: List<Int>): String {
        val looper = StringBuilder("0000000")
        if (weekList == null || weekList.size < 1) {
            return looper.toString()
        } else {
            for (i in weekList.indices) {
                when (weekList[i]) {
                    Calendar.MONDAY -> looper.replace(1, 2, "1")
                    Calendar.TUESDAY -> looper.replace(2, 3, "1")
                    Calendar.WEDNESDAY -> looper.replace(3, 4, "1")
                    Calendar.THURSDAY -> looper.replace(4, 5, "1")
                    Calendar.FRIDAY -> looper.replace(5, 6, "1")
                    Calendar.SATURDAY -> looper.replace(6, 7, "1")
                    Calendar.SUNDAY -> looper.replace(0, 1, "1")
                    else -> {}
                }
            }
        }
        return looper.toString()
    }

    companion object {
        const val SWITCH_ON = 1
        const val SWITCH_OFF = 0

        fun start(groupId: Long, groupType: String) =
            startActivity<ScheduleActionActivity>(
                KEY_GROUP_ID to groupId,
                KEY_TYPE to groupType
            )
    }
}