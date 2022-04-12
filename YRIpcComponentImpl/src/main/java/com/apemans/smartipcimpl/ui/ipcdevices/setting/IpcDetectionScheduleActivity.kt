package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcDetectionScheduleBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.ui.toolbar.updateToolbar
import com.apemans.ipcchipproxy.scheme.bean.DetectionPlanSchedule
import com.apemans.logger.YRLog
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.IpcDetectionScheduleViewDelegate
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.intentOf
import com.dylanc.longan.lifecycleOwner

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcDetectionScheduleActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcDetectionScheduleBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val dtTypeExtra: Int? by intentExtras(INTENT_KEY_DETECTION_TYPE)
    private val detectionScheduleAdapter = MultiTypeAdapter(IpcDetectionScheduleViewDelegate(lifecycleOwner, detectionScheduleItemListener, detectionScheduleListenerBlock))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        if (getDetectionType() == DETECTION_TYPE_HUMAN) {
            viewModel.loadPirDetectionPlanInfo(getDeviceId())
        } else{
            viewModel.loadDetectionPlanInfo(getDetectionType(), getDeviceId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        YRLog.d { "-->> debug IpcDetectionScheduleActivity requestCode $requestCode resultCode $resultCode" }
        if (resultCode == RESULT_OK) {
            var dsId = data?.getIntExtra(INTENT_KEY_DETECTION_SCHEDULE_ID, 0) ?: 0
            var dsStartTime = data?.getIntExtra(INTENT_KEY_START_TIME, 0) ?: 0
            var dsEndTime = data?.getIntExtra(INTENT_KEY_END_TIME, 0) ?: 0
            var dsWeekArr = data?.getIntegerArrayListExtra(INTENT_KEY_WEEK_ARR).orEmpty()

            if (!DeviceControlHelper.checkDetectionScheduleValid(dsId, dsStartTime, dsEndTime, dsWeekArr)) {
                return
            }

            val detectionSchedule = DetectionPlanSchedule().apply {
                id = dsId
                startTime = dsStartTime
                endTime = dsEndTime
                weekArr = dsWeekArr
            }
            viewModel.setDetectionPlan(getDetectionType(), getDeviceId(), detectionSchedule)

            YRLog.d { "-->> debug IpcDetectionScheduleActivity id $dsId startTime $dsStartTime endTime $dsEndTime weekArr ${dsWeekArr?.toString()}" }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcDetectionScheduleRv.adapter = detectionScheduleAdapter
        }

        detectionScheduleAdapter.observeItemsChanged(lifecycleOwner, viewModel.detectionSchedules) { oldItem, newItem ->
            oldItem.id == newItem.id
        }

        viewModel.detectionSchedules.observe(lifecycleOwner) {
            it?.let {
                if (getDetectionType() == DETECTION_TYPE_HUMAN) {
                    if (it.size < 3) {
                        updateToolbar {
                            rightIcon(R.drawable.device_home_add, ::onRightBtnClick)
                        }
                    } else {
                        updateToolbar {
                            rightIcon = -1
                        }
                    }
                }
            }
        }

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
        val planScheduleSize = viewModel.detectionSchedules.value?.size ?: 0
        val dsId = if (planScheduleSize < DETECTION_PLAN_SCHEDULE_SIZE) planScheduleSize + 1 else DETECTION_PLAN_SCHEDULE_SIZE
        var dsEndTime = if (getDetectionType() == DETECTION_TYPE_HUMAN) HOUR_LENGTH_OF_DAY * MIN_LENGTH_OF_HOUR else HOUR_LENGTH_OF_DAY * MIN_LENGTH_OF_HOUR - 1
        startActivityForResult(intentOf<IpcDetectionScheduleEditorActivity>(
            Pair(INTENT_KEY_DETECTION_TYPE, getDetectionType()),
            Pair(INTENT_KEY_DETECTION_SCHEDULE_ID, dsId),
            Pair(INTENT_KEY_START_TIME, 0),
            Pair(INTENT_KEY_END_TIME, dsEndTime),
            Pair(INTENT_KEY_WEEK_ARR, listOf(1, 1, 1, 1, 1, 1, 1))
        ), 101)
    }

    private val detectionScheduleItemListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DetectionPlanSchedule> { item, position ->
        /*
        startActivity<IpcDetectionScheduleEditorActivity>(
            Pair(INTENT_KEY_DETECTION_SCHEDULE_ID, item.id),
            Pair(INTENT_KEY_START_TIME, item.startTime),
            Pair(INTENT_KEY_END_TIME, item.endTime),
            Pair(INTENT_KEY_WEEK_ARR, item.weekArr)
        )
         */
        startActivityForResult(
            intentOf<IpcDetectionScheduleEditorActivity>(
                Pair(INTENT_KEY_DETECTION_TYPE, getDetectionType()),
                Pair(INTENT_KEY_DETECTION_SCHEDULE_ID, item.id),
                Pair(INTENT_KEY_START_TIME, item.startTime),
                Pair(INTENT_KEY_END_TIME, item.endTime),
                Pair(INTENT_KEY_WEEK_ARR, item.weekArr)
            ), 101
        )
    }

    private val detectionScheduleListenerBlock get() = { code: Int, data: DetectionPlanSchedule?, extra: Any? ->
        when (code) {
            VD_ACTION_IPC_DETECTION_SCHEDULE_ITEM_SWITCH -> {
                var scheduleIsValid = data?.let {
                    DeviceControlHelper.checkDetectionScheduleValid(it.id, it.startTime, it.endTime, it.weekArr)
                } ?: false
                if (scheduleIsValid) {
                    val isChecked = extra as? Boolean ?: false
                    data?.weekArr = data?.weekArr?.map {
                        if (isChecked) 1 else 0
                    }
                    viewModel.setDetectionPlan(getDetectionType(), getDeviceId(), data!!)
                }
            }
        }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun getDetectionType() : Int {
        return dtTypeExtra ?: 0
    }
}