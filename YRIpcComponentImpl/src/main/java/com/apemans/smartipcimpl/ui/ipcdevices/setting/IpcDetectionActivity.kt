package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.apemans.quickui.label.LABEL_SELECTION_1
import com.apemans.quickui.label.LABEL_SELECTION_2
import com.apemans.quickui.label.LABEL_SELECTION_3
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcDetectionBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SENSITIVITY_LEVEL_HIGH
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SENSITIVITY_LEVEL_LOW
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE
import com.apemans.ipcchipproxy.scheme.bean.DetectionPlanInfo
import com.apemans.smartipcimpl.constants.DETECTION_TYPE_HUMAN
import com.apemans.smartipcimpl.constants.DETECTION_TYPE_MOTION
import com.apemans.smartipcimpl.constants.INTENT_KEY_DETECTION_TYPE
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.dylanc.longan.startActivity

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcDetectionActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcDetectionBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val dtTypeExtra: Int? by intentExtras(INTENT_KEY_DETECTION_TYPE)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        if (getDeviceId().isNullOrEmpty()) {
            finish()
            return
        }
        viewModel.loadDetectionInfo(getDetectionType(), getDeviceId())
        if (getDetectionType() == DETECTION_TYPE_MOTION && viewModel.checkIsSupportDetectionArea(getDeviceId())) {
            viewModel.loadDetectionAreaInfo(getDeviceId())
        }
        if (getDetectionType() == DETECTION_TYPE_HUMAN) {
            viewModel.loadPirDetectionPlanInfo(getDeviceId())
        } else{
            viewModel.loadDetectionPlanInfo(getDetectionType(), getDeviceId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcDetectionSwitch.setupConfigure(lifecycleOwner)
            deviceIpcDetectionAlarm.setupConfigure(lifecycleOwner)
            deviceIpcDetectionSensitivity.setupConfigure(lifecycleOwner)
            deviceIpcDetectionArea.setupConfigure(lifecycleOwner)
            deviceIpcDetectionPlan.setupConfigure(lifecycleOwner)

            deviceIpcDetectionAlarm.visibilityEnable {
                viewModel.checkIsSupportFlashLight(getDeviceId())
            }

            deviceIpcDetectionArea.visibilityEnable {
                viewModel.checkIsSupportDetectionArea(getDeviceId())
            }

            deviceIpcDetectionSwitch.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }

            deviceIpcDetectionAlarm.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                }
            }

            deviceIpcDetectionSensitivity.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    selection1 = getString(R.string.camera_share_title)
                    selection2 = getString(R.string.camera_share_title)
                    selection3 = getString(R.string.camera_share_title)
                    selectionIndex = LABEL_SELECTION_1
                }
            }

            deviceIpcDetectionArea.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = getString(R.string.camera_share_title)
                    rightIconVisibility = View.VISIBLE
                }
            }

            deviceIpcDetectionPlan.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                    dividerLineVisible = View.GONE
                }
            }
        }

        displayDetectionInfoView(false)

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        binding.deviceIpcDetectionSwitch.mListener = { isChecked ->
            viewModel.switchDetection(getDetectionType(), getDeviceId(), isChecked)
        }

        binding.deviceIpcDetectionAlarm.mListener = { isChecked ->
            viewModel.setDetectionAlarm(getDeviceId(), isChecked)
        }

        binding.deviceIpcDetectionSensitivity.mListener = { index ->
            val level = when(index) {
                LABEL_SELECTION_1 -> { IPC_SCHEME_SENSITIVITY_LEVEL_LOW }
                LABEL_SELECTION_2 -> { IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE }
                else -> { IPC_SCHEME_SENSITIVITY_LEVEL_HIGH }
            }
            viewModel.setDetectionSensitivity(getDetectionType(), getDeviceId(), level)
        }

        registerOnViewClick(
            binding.deviceIpcDetectionArea,
            binding.deviceIpcDetectionPlan,
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        with(binding) {

            displayDetectionInfoView((uiState?.detectionInfo?.enable ?: false))

            deviceIpcDetectionAlarm.updateUiState {
                it?.apply {
                    switchOn = uiState?.detectionInfo?.alarmOn ?: false
                }
            }

            deviceIpcDetectionSensitivity.updateUiState {
                it?.apply {
                    selectionIndex = when((uiState?.detectionInfo?.level ?: IPC_SCHEME_SENSITIVITY_LEVEL_LOW)) {
                        IPC_SCHEME_SENSITIVITY_LEVEL_MIDDLE -> LABEL_SELECTION_2
                        IPC_SCHEME_SENSITIVITY_LEVEL_HIGH -> LABEL_SELECTION_3
                        else -> LABEL_SELECTION_1
                    }
                }
            }

            deviceIpcDetectionArea.updateUiState {
                it?.apply {
                    subText = if (uiState?.detectionAreaInfo?.enable == true) getString(R.string.camera_share_title) else getString(R.string.camera_share_title)
                }
            }

            deviceIpcDetectionPlan.updateUiState {
                it?.apply {
                    subText = getDetectionPlan(uiState?.detectionPlanInfo)
                }
            }
        }

    }

    private fun displayDetectionInfoView(enable: Boolean) {
        with(binding) {
            deviceIpcDetectionSwitch.updateUiState {
                it?.apply {
                    switchOn = enable
                    dividerLineVisible = if (enable) View.VISIBLE else View.GONE
                }
            }
            deviceIpcDetectionAlarm.visibilityEnable {
                enable && viewModel.checkIsSupportFlashLight(getDeviceId())
            }
            deviceIpcDetectionSensitivity.visibilityEnable { enable }
            deviceIpcDetectionArea.visibilityEnable {
                enable && getDetectionType() == DETECTION_TYPE_MOTION && viewModel.checkIsSupportDetectionArea(getDeviceId())
            }
            deviceIpcDetectionPlan.visibilityEnable {
                enable && !viewModel.checkIsNetSpotMode()
            }
        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcDetectionArea -> {
                startActivity<IpcDetectionAreaActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                )
            }
            R.id.deviceIpcDetectionPlan -> {
                startActivity<IpcDetectionScheduleActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_DETECTION_TYPE, getDetectionType())
                )
            }
            else -> {}
        }
    }

    private fun getDetectionPlan(detectionPlanInfo: DetectionPlanInfo?) : String {
        var planSchedule = detectionPlanInfo?.planSchedules?.firstOrNull {
            it.weekArr?.contains(1) ?: false
        } ?: detectionPlanInfo?.planSchedules?.getOrNull(0)
        return planSchedule?.let {
            DeviceControlHelper.convertDetectionScheduleTime(it.startTime, it.endTime)
        } ?: ""
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun getDetectionType() : Int {
        return dtTypeExtra ?: 0
    }
}