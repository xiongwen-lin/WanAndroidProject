package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.dylanc.longan.intentExtras
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.ui.toolbar.updateToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SHOOTING_IMAGE
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SHOOTING_VIDEO
import com.apemans.ipcchipproxy.define.IPC_SCHEME_SHOOTING_VIDEO_IMAGE
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.MultiViewDelegateItem
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcShootingBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.IpcShootingModeViewDelegate
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.IpcShootingRecordDurationViewDelegate
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
open class IpcShootingActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcShootingBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val operationTypeExtra: Int by intentExtras(INTENT_KEY_OPERATION_TYPE, MEDIA_MODE_OPERATION_TYPE_MODE)
    private val shootingConfigureAdapter = MultiTypeAdapter(IpcShootingModeViewDelegate(lifecycleOwner, shootingModeItemListener))
    private val shootingRecordDurationAdapter = MultiTypeAdapter(IpcShootingRecordDurationViewDelegate(lifecycleOwner, recordDurationModeItemListener))

    private val shootingModeList = MutableLiveData(listOf<MultiViewDelegateItem<Int>>())
    private val shootingRecordDurationList = MutableLiveData(listOf<MultiViewDelegateItem<Int>>())

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = ""
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMediaModeInfo(getDeviceId())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcShootingTip.text = when(operationTypeExtra) {
                MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM -> { getString(R.string.camera_share_title) }
                MEDIA_MODE_OPERATION_TYPE_DURATION -> { getString(R.string.camera_share_title) }
                else -> { getString(R.string.camera_share_title) }
            }
            deviceIpcShootingImageNum.setupConfigure(lifecycleOwner)
            deviceIpcShootingRecordDuration.setupConfigure(lifecycleOwner)

            deviceIpcShootingImageNum.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }

            deviceIpcShootingRecordDuration.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
        }

        displayShootingView(operationTypeExtra)

        when(operationTypeExtra) {
            MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM -> {
                updateToolbar {
                    title = getString(R.string.camera_share_title)
                }
            }
            MEDIA_MODE_OPERATION_TYPE_DURATION -> {
                updateToolbar {
                    title = getString(R.string.camera_share_title)
                }
                binding.deviceIpcShootingConfigureRv.adapter = shootingRecordDurationAdapter
                shootingRecordDurationAdapter.observeItemsChanged(lifecycleOwner, shootingRecordDurationList) { oldItem, newItem ->
                    oldItem.data == newItem.data && oldItem.isSelected == newItem.isSelected
                }
                shootingRecordDurationList.value = createShootingRecordDurations();
            }
            MEDIA_MODE_OPERATION_TYPE_MODE -> {
                updateToolbar {
                    title = getString(R.string.camera_share_title)
                }
                binding.deviceIpcShootingConfigureRv.adapter = shootingConfigureAdapter
                shootingConfigureAdapter.observeItemsChanged(lifecycleOwner, shootingModeList) { oldItem, newItem ->
                    oldItem.data == newItem.data && oldItem.isSelected == newItem.isSelected
                }
                shootingModeList.value = createShootingModes()
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            binding.deviceIpcShootingImageNum,
            binding.deviceIpcShootingRecordDuration,
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {
        val imageNum: Int = uiState?.mediaModeInfo?.picNum ?: 1
        binding.deviceIpcShootingImageNum.updateUiState {
            it?.apply {
                subText = "$imageNum"
            }
        }
        val recordDuration: Int = uiState?.mediaModeInfo?.vidDur ?: 5
        binding.deviceIpcShootingRecordDuration.updateUiState {
            it?.apply {
                subText = "${recordDuration}s"
            }
        }
        when(operationTypeExtra) {
            MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM -> {
            }
            MEDIA_MODE_OPERATION_TYPE_DURATION -> {
                shootingRecordDurationList.value = shootingRecordDurationList.value?.map {
                    MultiViewDelegateItem<Int>().apply {
                        isSelected = it.data == recordDuration
                        data = it.data
                    }
                }
            }
            MEDIA_MODE_OPERATION_TYPE_MODE -> {
                val mode: Int = uiState?.mediaModeInfo?.mode ?: IPC_SCHEME_SHOOTING_VIDEO_IMAGE
                displayShootingConfigureViewByMode(mode)
                shootingModeList.value = shootingModeList.value?.map {
                    MultiViewDelegateItem<Int>().apply {
                        isSelected = it.data == mode
                        data = it.data
                    }
                }
            }
        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcShootingImageNum -> {
                startActivity<IpcShootingImageNumActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_OPERATION_TYPE, MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM)
                )
            }
            R.id.deviceIpcShootingRecordDuration -> {
                startActivity<IpcShootingRecordDurationActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_OPERATION_TYPE, MEDIA_MODE_OPERATION_TYPE_DURATION)
                )
            }
            else -> {}
        }
    }

    private val shootingModeItemListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<Int>> { item, position ->
        val mode: Int = item?.data ?: IPC_SCHEME_SHOOTING_VIDEO_IMAGE
        displayShootingConfigureViewByMode(mode)
        viewModel.uiState.value = viewModel.uiState.value?.apply {
            mediaModeInfo?.apply {
                this.mode = mode
            }
        }
        viewModel.setMediaModeInfo(MEDIA_MODE_OPERATION_TYPE_MODE, getDeviceId(), mediaMode = mode)
    }

    private val recordDurationModeItemListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<MultiViewDelegateItem<Int>> { item, position ->
        val recordDuration: Int = item?.data ?: 5
        viewModel.uiState.value = viewModel.uiState.value?.apply {
            mediaModeInfo?.apply {
                this.vidDur = recordDuration
            }
        }
        viewModel.setMediaModeInfo(MEDIA_MODE_OPERATION_TYPE_DURATION, getDeviceId(), recordDuration = recordDuration)
    }

    private fun displayShootingView(operationType: Int) {
        with(binding) {
            binding.deviceIpcShootingCard.visibilityEnable { operationType != MEDIA_MODE_OPERATION_TYPE_IMAGE_NUM }
            binding.deviceIpcShootingCard2.visibilityEnable { operationType == MEDIA_MODE_OPERATION_TYPE_MODE }
        }
    }

    private fun  displayShootingConfigureViewByMode(mode: Int) {
        with(binding) {
            when(mode) {
                IPC_SCHEME_SHOOTING_VIDEO -> {
                    deviceIpcShootingImageNum.visibilityEnable { false }
                    deviceIpcShootingRecordDuration.visibilityEnable { true }
                    deviceIpcShootingRecordDuration.updateUiState {
                        it?.apply {
                            dividerLineVisible = View.GONE
                        }
                    }
                }
                IPC_SCHEME_SHOOTING_IMAGE -> {
                    deviceIpcShootingImageNum.visibilityEnable { true }
                    deviceIpcShootingRecordDuration.visibilityEnable { false }
                    deviceIpcShootingImageNum.updateUiState {
                        it?.apply {
                            dividerLineVisible = View.GONE
                        }
                    }
                }
                IPC_SCHEME_SHOOTING_VIDEO_IMAGE -> {
                    deviceIpcShootingImageNum.visibilityEnable { true }
                    deviceIpcShootingRecordDuration.visibilityEnable { true }
                    deviceIpcShootingRecordDuration.updateUiState {
                        it?.apply {
                            dividerLineVisible = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun createShootingModes() : List<MultiViewDelegateItem<Int>> {
        val shootingModes: MutableList<MultiViewDelegateItem<Int>> = mutableListOf()
        shootingModes.add(MultiViewDelegateItem<Int>().apply {
            data = IPC_SCHEME_SHOOTING_VIDEO
        })
        shootingModes.add(MultiViewDelegateItem<Int>().apply {
            data = IPC_SCHEME_SHOOTING_IMAGE
        })
        shootingModes.add(MultiViewDelegateItem<Int>().apply {
            data = IPC_SCHEME_SHOOTING_VIDEO_IMAGE
        })
        return shootingModes
    }

    private fun createShootingRecordDurations() : List<MultiViewDelegateItem<Int>> {
        val recordDurations: MutableList<MultiViewDelegateItem<Int>> = mutableListOf()
        for (i in 1..4) {
            recordDurations.add(MultiViewDelegateItem<Int>().apply {
                data = if (i > 1) 10 * (i - 1) else 5
            })
        }
        return recordDurations
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}