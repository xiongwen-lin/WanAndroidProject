package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.quickui.alerter.alertInfo
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcDetectionScheduleEditorBinding
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.logger.YRLog
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.picker.HOUR_MIN_PICKER_TYPE_HALF_HOUR
import com.apemans.quickui.picker.HOUR_MIN_PICKER_TYPE_NORMAL
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.WeekdaySelectorViewDelegate
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.finishWithResult
import com.dylanc.longan.intentExtras

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcDetectionScheduleEditorActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcDetectionScheduleEditorBinding>() {

    private val idExtra: Int by intentExtras(INTENT_KEY_DETECTION_SCHEDULE_ID, 0)
    private val startTimeExtra: Int by intentExtras(INTENT_KEY_START_TIME, 0)
    private val endTimeExtra: Int by intentExtras(INTENT_KEY_END_TIME, 0)
    private val weekArrExtra: List<Int>? by intentExtras(INTENT_KEY_WEEK_ARR, listOf())
    private val dtTypeExtra: Int? by intentExtras(INTENT_KEY_DETECTION_TYPE)

    private val weekdaySelectorAdapter = MultiTypeAdapter(WeekdaySelectorViewDelegate(weekdaySelectorItemListener))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
            rightIcon(R.drawable.device_home_add, ::onRightBtnClick)
        }
        YRLog.d { "-->> debug IpcDetectionScheduleEditorActivity idExtra $idExtra startTimeExtra endTimeExtra $startTimeExtra $endTimeExtra weekArrExtra ${weekArrExtra?.toString()}" }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            val pickerType = if (getDetectionType() == DETECTION_TYPE_HUMAN) HOUR_MIN_PICKER_TYPE_HALF_HOUR else HOUR_MIN_PICKER_TYPE_NORMAL
            deviceIpcDetectionScheduleEditorFromPicker.setupHourMinPickerType(pickerType)
            deviceIpcDetectionScheduleEditorToPicker.setupHourMinPickerType(pickerType)

            deviceIpcDetectionScheduleEditorFromPicker.obtainHourPicker().value = DeviceControlHelper.computeHourForTime(startTimeExtra)
            deviceIpcDetectionScheduleEditorFromPicker.obtainMinPicker().value = DeviceControlHelper.computeMinForTime(startTimeExtra)

            deviceIpcDetectionScheduleEditorToPicker.obtainHourPicker().value = DeviceControlHelper.computeHourForTime(endTimeExtra)
            deviceIpcDetectionScheduleEditorToPicker.obtainMinPicker().value = DeviceControlHelper.computeMinForTime(endTimeExtra)

        }

        binding.deviceIpcDetectionScheduleEditorWeekdayList.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.HORIZONTAL }
        binding.deviceIpcDetectionScheduleEditorWeekdayList.adapter = weekdaySelectorAdapter
        weekdaySelectorAdapter.items = convertWeekdayItem(weekArrExtra)

        registerOnViewClick(
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            0 -> {}
            else -> {}
        }
    }

    private fun onRightBtnClick(v: View) {
        val startTime = DeviceControlHelper.computeTimeByHourAndMin(binding.deviceIpcDetectionScheduleEditorFromPicker.obtainHourPicker().value, binding.deviceIpcDetectionScheduleEditorFromPicker.obtainMinPicker().value)
        val endTime = DeviceControlHelper.computeTimeByHourAndMin(binding.deviceIpcDetectionScheduleEditorToPicker.obtainHourPicker().value, binding.deviceIpcDetectionScheduleEditorToPicker.obtainMinPicker().value)
        if (endTime <= startTime) {
            alertInfo(getString(R.string.camera_share_title))
            return
        }
        val weekArr = getWeekdayArr()
        finishWithResult(
            Pair(INTENT_KEY_DETECTION_SCHEDULE_ID, idExtra),
            Pair(INTENT_KEY_START_TIME, startTime),
            Pair(INTENT_KEY_END_TIME, endTime),
            Pair(INTENT_KEY_WEEK_ARR, weekArr)
        )
    }

    private val weekdaySelectorItemListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<String>> { item, position -> }

    private fun getWeekdayArr() : List<Int> {
        val weekDayArr = mutableListOf<Int>()
        for (i in 0 until WEEK_DAY_LENGTH) {
            val isChecked = (weekdaySelectorAdapter.items.getOrNull(i) as? MultiViewDelegateItem<String>)?.isChecked ?: false
            val weekDayValue = if (isChecked) 1 else 0
            weekDayArr.add(weekDayValue)
        }
        return weekDayArr
    }

    private fun convertWeekdayItem(weekArr: List<Int>?) : List<MultiViewDelegateItem<String>> {
        var weekdayArr = weekArr
        if (weekdayArr.isNullOrEmpty() || weekdayArr.size < WEEK_DAY_LENGTH) {
            weekdayArr = listOf(1, 1, 1, 1, 1, 1, 1)
        }
        return weekdayArr.mapIndexed { index, i ->
            MultiViewDelegateItem<String>().apply {
                groupId = 1
                data = convertWeekDayStr(index)
                isChecked = i == 1
            }
        }
    }

    private fun convertWeekDayStr(index: Int) : String {
        return when(index) {
            0 -> "M"
            1 -> "T"
            2 -> "W"
            3 -> "T"
            4 -> "F"
            5 -> "S"
            6 -> "S"
            else -> ""
        }
    }

    private fun getDetectionType() : Int {
        return dtTypeExtra ?: 0
    }

}