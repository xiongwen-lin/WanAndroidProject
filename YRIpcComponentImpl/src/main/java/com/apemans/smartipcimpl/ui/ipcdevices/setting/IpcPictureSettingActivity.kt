package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.apemans.quickui.label.LABEL_SELECTION_1
import com.apemans.quickui.label.LABEL_SELECTION_2
import com.apemans.quickui.label.LABEL_SELECTION_3
import com.apemans.quickui.label.LABEL_SELECTION_4
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcPictureSettingBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_LIGHT_MODE_AUTO
import com.apemans.ipcchipproxy.define.IPC_SCHEME_LIGHT_MODE_COLOR
import com.apemans.ipcchipproxy.define.IPC_SCHEME_LIGHT_MODE_IR
import com.apemans.ipcchipproxy.define.IPC_SCHEME_LIGHT_MODE_OFF
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcPictureSettingActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcPictureSettingBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDeviceSetting(getDeviceId())
        if (viewModel.checkIsNetSpotMode()) {
            if (viewModel.checkIsSupportWaterMark(getDeviceId())) {
                viewModel.loadWaterMark(getDeviceId())
            }
            if (viewModel.checkIsSupportEnergyMode(getDeviceId())) {
                viewModel.loadEnergyMode(getDeviceId())
            }
        } else {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        var isSupportNightVisionLightMode = viewModel.checkIsSupportNightVisionWithLight(getDeviceId())
        with(binding) {
            deviceIpcPictureSettingAudioRecording.setupConfigure(lifecycleOwner)
            deviceIpcPictureSettingNightVision.setupConfigure(lifecycleOwner)
            deviceIpcPictureSettingEnergyMode.setupConfigure(lifecycleOwner)
            deviceIpcPictureSettingWaterMark.setupConfigure(lifecycleOwner)
            deviceIpcPictureSettingRotate.setupConfigure(lifecycleOwner)
            deviceIpcPictureSettingMotionTrack.setupConfigure(lifecycleOwner)

            deviceIpcPictureSettingAudioRecording.visibilityEnable {
                viewModel.checkIsSupportAudioRecord(getDeviceId())
            }

            deviceIpcPictureSettingNightVision.visibilityEnable {
                viewModel.checkIsSupportNightVision(getDeviceId())
            }

            deviceIpcPictureSettingEnergyMode.visibilityEnable {
                false
            }

            deviceIpcPictureSettingWaterMark.visibilityEnable {
                viewModel.checkIsSupportWaterMark(getDeviceId())
            }

            deviceIpcPictureSettingMotionTrack.visibilityEnable {
                viewModel.checkIsSupportMotionTrack(getDeviceId())
            }

            deviceIpcPictureSettingAudioRecording.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }

            deviceIpcPictureSettingNightVision.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                    selection1 = getString(R.string.camera_share_title)
                    selection2 = getString(R.string.camera_share_title)
                    selection3 = if (isSupportNightVisionLightMode) getString(R.string.camera_share_title) else ""
                    selection4 = if (isSupportNightVisionLightMode) getString(R.string.camera_share_title) else ""
                    selectionIndex = LABEL_SELECTION_1
                }
            }

            deviceIpcPictureSettingEnergyMode.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                }
            }

            deviceIpcPictureSettingWaterMark.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }

            deviceIpcPictureSettingRotate.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }

            deviceIpcPictureSettingMotionTrack.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        binding.deviceIpcPictureSettingAudioRecording.mListener = { isChecked ->
            viewModel.setAudioRecord(getDeviceId(), isChecked)
        }
        binding.deviceIpcPictureSettingWaterMark.mListener = { isChecked ->
            viewModel.setWaterMark(getDeviceId(), isChecked)
        }
        binding.deviceIpcPictureSettingEnergyMode.mListener = { isChecked ->
            viewModel.setEnergyMode(getDeviceId(), isChecked)
        }
        binding.deviceIpcPictureSettingRotate.mListener = { isChecked ->
            viewModel.setRotate(getDeviceId(), isChecked)
        }
        binding.deviceIpcPictureSettingMotionTrack.mListener = { isChecked ->
            viewModel.setMotionTrack(getDeviceId(), isChecked)
        }

        binding.deviceIpcPictureSettingNightVision.mListener = { index ->
            if (isSupportNightVisionLightMode) {
                switchNightVisionWithLight(index)
            } else {
                switchNightVision(index)
            }
        }

        registerOnViewClick(
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        with(binding) {
            deviceIpcPictureSettingAudioRecording.updateUiState {
                it?.apply {
                    switchOn = uiState?.deviceSettingInfo?.audioRecordOn ?: false
                }
            }

            deviceIpcPictureSettingEnergyMode.updateUiState {
                it?.apply {
                    switchOn = uiState?.energyModeState ?: false
                }
            }

            deviceIpcPictureSettingWaterMark.updateUiState {
                it?.apply {
                    switchOn = uiState?.waterMarkState ?: false
                }
            }

            deviceIpcPictureSettingRotate.updateUiState {
                it?.apply {
                    switchOn = uiState?.deviceSettingInfo?.rotate ?: false
                }
            }

            deviceIpcPictureSettingMotionTrack.updateUiState {
                it?.apply {
                    switchOn = uiState?.deviceSettingInfo?.motionTrackingOn ?: false
                }
            }
        }

        val mode : Int = uiState?.deviceSettingInfo?.ir ?: IPC_SCHEME_LIGHT_MODE_OFF
        displayNightVisionView(mode)

    }

    private fun displayNightVisionView(mode: Int) {
        var isSupportNightVisionLightMode = viewModel.checkIsSupportNightVisionWithLight(getDeviceId())
        var index = if (isSupportNightVisionLightMode) {
            when(mode) {
                IPC_SCHEME_LIGHT_MODE_IR -> LABEL_SELECTION_2
                IPC_SCHEME_LIGHT_MODE_COLOR -> LABEL_SELECTION_3
                IPC_SCHEME_LIGHT_MODE_AUTO -> LABEL_SELECTION_4
                else -> LABEL_SELECTION_1
            }
        } else {
            when(mode) {
                IPC_SCHEME_LIGHT_MODE_IR -> LABEL_SELECTION_2
                else -> LABEL_SELECTION_1
            }
        }
        binding.deviceIpcPictureSettingNightVision.updateUiState {
            it?.apply {
                selectionIndex = index
            }
        }
        binding.deviceIpcPictureSettingEnergyMode.visibilityEnable {
            mode == IPC_SCHEME_LIGHT_MODE_IR && viewModel.checkIsSupportEnergyMode(getDeviceId())
        }
    }

    private fun switchNightVision(index: Int) {
        val mode: Int = when(index) {
            LABEL_SELECTION_2 -> {
                IPC_SCHEME_LIGHT_MODE_IR
            }
            else -> {
                IPC_SCHEME_LIGHT_MODE_OFF
            }
        }
        viewModel.uiState.value = viewModel.uiState.value?.apply {
            deviceSettingInfo?.ir = mode
        }
        viewModel.setNightVisionIR(getDeviceId(), mode)
    }

    private fun switchNightVisionWithLight(index: Int) {
        var mode: Int = when(index) {
            LABEL_SELECTION_2 -> {
                IPC_SCHEME_LIGHT_MODE_IR
            }
            LABEL_SELECTION_3 -> {
                IPC_SCHEME_LIGHT_MODE_COLOR
            }
            LABEL_SELECTION_4 -> {
                IPC_SCHEME_LIGHT_MODE_AUTO
            }
            else -> {
                IPC_SCHEME_LIGHT_MODE_OFF
            }
        }
        viewModel.uiState.value = viewModel.uiState.value?.apply {
            deviceSettingInfo?.ir = mode
        }
        viewModel.setNightVisionLight(getDeviceId(), mode)
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            0 -> {}
            else -> {}
        }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}