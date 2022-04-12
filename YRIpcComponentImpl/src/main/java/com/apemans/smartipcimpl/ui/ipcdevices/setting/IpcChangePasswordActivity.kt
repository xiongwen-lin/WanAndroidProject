package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.quickui.alerter.alertInfo
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcChangePasswordBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.SCOPE_TASK_STATE_RESULT_SUCCESS
import com.apemans.smartipcimpl.utils.ScopeTaskResult
import com.apemans.base.utils.addTextChangedListener
import com.apemans.base.utils.enable
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcChangePasswordActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcChangePasswordBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    private val loadingState: MutableLiveData<ScopeTaskResult<Int>> = MutableLiveData<ScopeTaskResult<Int>>()

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
            deviceIpcChangePasswordSubmit.enable = false
        }

        binding.deviceIpcChangePasswordOld.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)
        binding.deviceIpcChangePasswordNew.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)
        binding.deviceIpcChangePasswordConfirmPw.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        loadingState.observe(lifecycleOwner) {
            val isSuccess = it?.state == SCOPE_TASK_STATE_RESULT_SUCCESS
            if (isSuccess) {
                finish()
            } else {
                alertInfo {
                    if (it.data == 2) getString(R.string.camera_share_title) else getString(R.string.camera_share_title)
                }
            }
        }

        registerOnViewClick(
            binding.deviceIpcChangePasswordSubmit,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcChangePasswordSubmit -> {
                val oldPassword = binding.deviceIpcChangePasswordOld.getEditText().toString().orEmpty()
                val confirmPassword = binding.deviceIpcChangePasswordConfirmPw.getEditText().toString().orEmpty()
                viewModel.setNetSpotPw(getDeviceId(), oldPassword, confirmPassword, loadingState)
            }
            else -> {}
        }
    }

    private val checkAfterInputTextChange: (Editable?) -> Unit = {
        val oldPassword = binding.deviceIpcChangePasswordOld.getEditText().toString().orEmpty()
        val newPassword = binding.deviceIpcChangePasswordNew.getEditText().toString().orEmpty()
        val confirmPassword = binding.deviceIpcChangePasswordConfirmPw.getEditText().toString().orEmpty()
        binding.deviceIpcChangePasswordSubmit.isEnabled = false
        if (!checkPwValid(oldPassword) || !checkPwValid(newPassword) || !checkPwValid(confirmPassword)) {
        } else if (oldPassword != newPassword) {
        } else if (newPassword != confirmPassword) {
        } else {
            binding.deviceIpcChangePasswordSubmit.isEnabled = true
        }
    }

    private fun checkPwValid(password: String) : Boolean {
        return password.isNotEmpty() && password.length >= 8 && password.length <= 16
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

}