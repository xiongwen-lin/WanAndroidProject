package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_FLASH_LIGHT_MODE_ALARM
import com.apemans.ipcchipproxy.define.IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
import com.apemans.ipcchipproxy.define.IPC_SCHEME_FLASH_LIGHT_MODE_COLOR
import com.apemans.ipcchipproxy.scheme.bean.LightInfo
import com.apemans.quickui.multitype.CheckType
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.constants.DETECTION_TYPE_HUMAN
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcFlashLightBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.IpcLightModeViewDelegate
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.lifecycleOwner

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcFlashLightActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcFlashLightBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val lightModeAdapter = MultiTypeAdapter(lightModeViewDelegate)
    private val lightModeList = MutableLiveData(listOf<MultiViewDelegateItem<LightInfo>>())

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
        viewModel.loadDetectionInfo(DETECTION_TYPE_HUMAN, getDeviceId())
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcFlashLightConfigureRv.adapter = lightModeAdapter
        }

        lightModeAdapter.observeItemsChanged(lifecycleOwner, lightModeList) { oldItem, newItem ->
            oldItem.data?.mode == newItem.data?.mode && oldItem.isSelected == newItem.isSelected
        }

        lightModeList.value = createLightModeList()
        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        val mode = uiState?.detectionInfo?.lightInfo?.mode ?: IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
        lightModeList.value = lightModeList.value?.map {
            MultiViewDelegateItem<LightInfo>(it.groupId, it.isChecked).apply {
                isSelected = it.data?.mode == mode
                data = it.data
            }
        }
        displayFlashLightScreen(mode)

    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            0 -> {}
            else -> {}
        }
    }

    private val lightModeViewDelegate get() = IpcLightModeViewDelegate(lifecycleOwner, lightModeItemListener, CheckType.SINGLE_CANCEL_DISABLE, lightModeListenerBlock)

    private val lightModeItemListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<LightInfo>> { item, position ->
        item.data?.let {
            val lightMode = it.mode
            viewModel.uiState.value = viewModel.uiState.value?.apply {
                detectionInfo?.apply {
                    lightInfo?.apply {
                        mode = lightMode
                    }
                }
            }
            displayFlashLightScreen(it.mode)
            viewModel.setFlashLightMode(getDeviceId(), it.mode)
        }
    }

    private val lightModeListenerBlock get() = { code: Int, data: MultiViewDelegateItem<LightInfo>?, extra: Any? -> }

    private fun createLightModeList() : List<MultiViewDelegateItem<LightInfo>> {
        val lightModes = mutableListOf<MultiViewDelegateItem<LightInfo>>()
        lightModes.add(MultiViewDelegateItem<LightInfo>().apply {
            groupId = 1
            data = LightInfo().apply {
                mode = IPC_SCHEME_FLASH_LIGHT_MODE_COLOR
            }
        })
        lightModes.add(MultiViewDelegateItem<LightInfo>().apply {
            groupId = 1
            data = LightInfo().apply {
                mode = IPC_SCHEME_FLASH_LIGHT_MODE_ALARM
            }
        })
        lightModes.add(MultiViewDelegateItem<LightInfo>().apply {
            groupId = 1
            data = LightInfo().apply {
                mode = IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
            }
        })
        return lightModes
    }

    private fun displayFlashLightScreen(mode: Int) {
        when (mode) {
            IPC_SCHEME_FLASH_LIGHT_MODE_COLOR -> {
                binding.deviceIpcFlashLightIcon.setImageResource(R.drawable.device_default_preview)
            }
            IPC_SCHEME_FLASH_LIGHT_MODE_ALARM -> {
                binding.deviceIpcFlashLightIcon.setImageResource(R.drawable.device_default_preview)
            }
            else -> {
                binding.deviceIpcFlashLightIcon.setImageResource(R.drawable.device_gateway_camera)
            }
        }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}