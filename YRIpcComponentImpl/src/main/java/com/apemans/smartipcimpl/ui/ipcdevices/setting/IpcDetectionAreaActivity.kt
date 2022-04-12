package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcDetectionAreaBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.DETECTION_AREA_OPERATION_TYPE_SWITCH
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.dylanc.longan.lifecycleOwner

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcDetectionAreaActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcDetectionAreaBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
        viewModel.loadDetectionAreaInfo(getDeviceId())
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcDetectionAreaSwitch.setupConfigure(lifecycleOwner)
            deviceIpcDetectionAreaSwitch.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        binding.deviceIpcDetectionAreaSwitch.mListener = { isChecked ->
            viewModel.setDetectionAreaInfo(DETECTION_AREA_OPERATION_TYPE_SWITCH, getDeviceId(), isChecked)
        }

        registerOnViewClick(
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        with(binding) {
            deviceIpcDetectionAreaSwitch.updateUiState {
                it?.apply {
                    switchOn = uiState?.detectionAreaInfo?.enable ?: false
                }
            }
            displayDetectionAreaView((uiState?.detectionAreaInfo?.enable ?: false))
        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            0 -> {}
            else -> {}
        }
    }

    private fun displayDetectionAreaView(enable: Boolean) {
        binding.deviceIpcDetectionAreaEditorCard.visibilityEnable { enable }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}