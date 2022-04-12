package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.quickui.alerter.alertInfo
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_NAME
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcNameDeviceBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.RecommendedNameLabelViewDelegate
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.SCOPE_TASK_STATE_RESULT_SUCCESS
import com.apemans.smartipcimpl.utils.ScopeTaskResult
import com.apemans.base.utils.addTextChangedListener
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
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
class IpcNameDeviceActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcNameDeviceBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val deviceNameExtra: String? by intentExtras(INTENT_KEY_DEVICE_NAME)
    private val loadingState: MutableLiveData<ScopeTaskResult<Any>> = MutableLiveData<ScopeTaskResult<Any>>()

    private val adapter = MultiTypeAdapter(RecommendedNameLabelViewDelegate(recommendedNameItemClickListener))

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
            adapter.items = listOf(
                getString(R.string.add_camera_front_door),
                getString(R.string.add_camera_office),
                getString(R.string.add_camera_living_room),
                getString(R.string.add_camera_garage),
                getString(R.string.add_camera_baby_room),
                getString(R.string.add_camera_kitchen)
            )
            if (!deviceNameExtra.isNullOrEmpty()) {
                binding.deviceIpcNameEditor.setText(deviceNameExtra.orEmpty())
            }
            binding.deviceIpcNameRecommendNameList.adapter = adapter
        }

        binding.deviceIpcNameEditor.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        loadingState.observe(lifecycleOwner) {
            val isSuccess = it?.state == SCOPE_TASK_STATE_RESULT_SUCCESS
            if (isSuccess) {
                finish()
            } else {
                alertInfo { getString(R.string.camera_share_title) }
            }
        }

        registerOnViewClick(
            binding.deviceIpcNameBtn,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcNameBtn -> {
                val deviceName = binding.deviceIpcNameEditor.getEditText().text.toString().orEmpty()
                if (deviceName.isNotEmpty()) {
                    viewModel.startUpdateDeviceName(getDeviceId(), deviceName, loadingState)
                }
            }
            else -> {}
        }
    }

    private val checkAfterInputTextChange: (Editable?) -> Unit = {
        var enable = binding.let {
            it.deviceIpcNameEditor.getEditText().text.isNotEmpty()
        }
        binding.deviceIpcNameBtn.isEnabled = enable
    }

    private val recommendedNameItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<String> { item, position ->
        binding.deviceIpcNameEditor.setText(item.orEmpty())
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

}