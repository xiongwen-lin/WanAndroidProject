package com.apemans.smartipcimpl.ui.ipcdevices.setting.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.apemans.quickui.multitype.getItem
import com.apemans.ipcchipproxy.scheme.bean.DetectionPlanSchedule
import com.apemans.smartipcimpl.constants.VD_ACTION_IPC_DETECTION_SCHEDULE_ITEM_SWITCH
import com.apemans.smartipcimpl.databinding.DeviceItemIpcDetectionScheduleBinding
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.widget.BaseItemViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.yruibusiness.utils.viewbinding.onItemClick

/**
 * @author Dylan Cai
 */

class IpcDetectionScheduleViewDelegate(
    private var owner: LifecycleOwner,
    private val listener: com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DetectionPlanSchedule>,
    listenerBlock: ((Int, DetectionPlanSchedule?, Any?) -> Unit)
) :
    BaseItemViewDelegate<DetectionPlanSchedule, com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDetectionScheduleBinding>>(listenerBlock) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDetectionScheduleBinding>(parent)
            .onItemClick { listener.onItemClick(getItem(it), it) }

    override fun onBindViewHolder(
        holder: com.apemans.yruibusiness.utils.viewbinding.BindingViewHolder<DeviceItemIpcDetectionScheduleBinding>,
        item: DetectionPlanSchedule
    ) {
        refreshView(holder.binding, item)
    }

    private fun refreshView(view: DeviceItemIpcDetectionScheduleBinding, schedule: DetectionPlanSchedule) {
        with(view) {
            deviceIpcDetectionScheduleItem.setupConfigure(owner)
            deviceIpcDetectionScheduleItem.updateUiState {
                val  scheduleActive = schedule.weekArr?.contains(1) ?: false
                it?.apply {
                    text = DeviceControlHelper.convertDetectionScheduleTime(schedule.startTime, schedule.endTime)
                    detailText = if (scheduleActive) DeviceControlHelper.convertDetectionScheduleWeekDay(schedule.weekArr) else ""
                    switchOn = scheduleActive
                    dividerLineVisible = View.GONE
                }
            }

            deviceIpcDetectionScheduleItem.mListener = { isChecked ->
                handleAction(VD_ACTION_IPC_DETECTION_SCHEDULE_ITEM_SWITCH, schedule, isChecked)
            }
        }
    }

}