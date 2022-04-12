package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcConnectionModeSwitchBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcConnectionModeSwitchActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcConnectionModeSwitchBinding>() {

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
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            val modeText = if (viewModel.checkIsNetSpotMode()) getString(R.string.camera_share_title) else getString(R.string.camera_share_title)
            deviceIpcConnectionModeSwitchTip1.text = String.format("connection mode %1\$s", modeText)
        }

        registerOnViewClick(
            binding.deviceIpcConnectionModeSwitchNext,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcConnectionModeSwitchNext -> {
                clickSwitchConnectionMode()
            }
            else -> {}
        }
    }

    private fun clickSwitchConnectionMode() {
        if (!viewModel.checkIsNetSpotMode()) {
            //TODO go to home activity
            gotoHomePage()
            return
        }
        //todo disconnect connection and go to home activity
        gotoHomePage()
    }

    private fun gotoHomePage() {
        finish()
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}