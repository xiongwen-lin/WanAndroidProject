package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import com.apemans.quickui.label.LABEL_SELECTION_1
import com.apemans.base.utils.NotificationUtil
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.dmapi.status.DeviceDefine.BIND_TYPE_SHARER
import com.apemans.quickui.alerter.alertInfo
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcSettingBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_DETECTION_TYPE_PIR
import com.apemans.ipcchipproxy.define.IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE
import com.apemans.logger.YRLog
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.startRouterActivity
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.utils.SCOPE_TASK_STATE_RESULT_SUCCESS
import com.apemans.smartipcimpl.utils.ScopeTaskResult

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/5 7:50 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcSettingActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcSettingBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val deviceModelExtra: String? by intentExtras(INTENT_KEY_MODEL)
    private val parentDeviceIdExtra: String? by intentExtras(INTENT_KEY_PARENT_DEVICE_ID)
    private val bindTypeExtra: Int by intentExtras(INTENT_KEY_BIND_TYPE, BIND_TYPE_SHARER)
    private val onlineStateExtra: Boolean by intentExtras(INTENT_KEY_DEVICE_ONLINE_STATE, false)

    private var removeDeviceLoadingState: MutableLiveData<ScopeTaskResult<Any>> = MutableLiveData<ScopeTaskResult<Any>>()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera Settings"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.checkIsNetSpotMode()) {
        } else {
            viewModel.loadDeviceInfo(getDeviceId())
            checkSystemNoticeView()
        }
        viewModel.loadDeviceSetting(getDeviceId())
        if (viewModel.checkIsSupportFlashLight(getDeviceId())) {
            viewModel.loadDetectionInfo(DETECTION_TYPE_HUMAN, getDeviceId())
        }
        if (viewModel.checkIsSupportLed(getDeviceId())) {
            viewModel.loadLedState(getDeviceId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterOnViewClick(
            binding.deviceIpcSettingInfo,
            binding.deviceIpcSettingNoticeSharing,
            binding.deviceIpcSettingNoticeCheck,
            binding.deviceIpcSettingPicture,
            binding.deviceIpcSettingMotionDetect,
            binding.deviceIpcSettingSoundDetect,
            binding.deviceIpcSettingHumanDetect,
            binding.deviceIpcSettingShooting,
            binding.deviceIpcSettingFlashLight,
            binding.deviceIpcSettingSwitchConnection,
            binding.deviceIpcSettingStorage,
            binding.deviceIpcSettingAlexa,
            binding.deviceIpcSettingGoogle,
            binding.btnIpcSettingRemoveDevice,
            binding.btnIpcSettingResetDevice
        )
    }

    private fun setupUI() {
        displayIpcFunctionView(getDeviceId(), DeviceControlHelper.checkIsOwnerDevice(bindTypeExtra))
        with(binding) {
            deviceIpcSettingInfoSwitch.setupConfigure(lifecycleOwner)
            deviceIpcSettingNoticeSharing.setupConfigure(lifecycleOwner)
            deviceIpcSettingNoticeDetection.setupConfigure(lifecycleOwner)
            deviceIpcSettingPicture.setupConfigure(lifecycleOwner)
            deviceIpcSettingMotionDetect.setupConfigure(lifecycleOwner)
            deviceIpcSettingSoundDetect.setupConfigure(lifecycleOwner)
            deviceIpcSettingHumanDetect.setupConfigure(lifecycleOwner)
            deviceIpcSettingShooting.setupConfigure(lifecycleOwner)
            deviceIpcSettingFlashLight.setupConfigure(lifecycleOwner)
            deviceIpcSettingLed.setupConfigure(lifecycleOwner)
            deviceIpcSettingSyncTime.setupConfigure(lifecycleOwner)
            deviceIpcSettingSwitchConnection.setupConfigure(lifecycleOwner)
            deviceIpcSettingStorage.setupConfigure(lifecycleOwner)

            deviceIpcSettingInfoSwitch.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }
            deviceIpcSettingNoticeSharing.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingNoticeDetection.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }
            deviceIpcSettingPicture.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingMotionDetect.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingSoundDetect.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingHumanDetect.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingShooting.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingFlashLight.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingLed.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingSyncTime.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                    selection1 = getString(R.string.camera_share_title)
                    selectionIndex = LABEL_SELECTION_1
                }
            }
            deviceIpcSettingSwitchConnection.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceIpcSettingStorage.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        removeDeviceLoadingState.observe(lifecycleOwner) {
            if (it?.state == SCOPE_TASK_STATE_RESULT_SUCCESS) {
                finish()
            } else {
                alertInfo { getString(R.string.camera_share_title) }
            }
        }

        registerOnViewClick(
            binding.deviceIpcSettingInfo,
            binding.deviceIpcSettingNoticeSharing,
            binding.deviceIpcSettingNoticeCheck,
            binding.deviceIpcSettingPicture,
            binding.deviceIpcSettingMotionDetect,
            binding.deviceIpcSettingSoundDetect,
            binding.deviceIpcSettingHumanDetect,
            binding.deviceIpcSettingShooting,
            binding.deviceIpcSettingFlashLight,
            binding.deviceIpcSettingSwitchConnection,
            binding.deviceIpcSettingStorage,
            binding.deviceIpcSettingAlexa,
            binding.deviceIpcSettingGoogle,
            binding.btnIpcSettingRemoveDevice,
            binding.btnIpcSettingResetDevice,
            block = ::handleOnViewClick
        )

        binding.deviceIpcSettingNoticeDetection.mListener = { isChecked ->
            viewModel.updateDeviceNotice(getDeviceId(), isChecked)
        }

        binding.deviceIpcSettingLed.mListener = { isChecked ->
            viewModel.setLedState(getDeviceId(), isChecked)
        }

        binding.deviceIpcSettingSyncTime.mListener = { index ->
            if (index == LABEL_SELECTION_1) {
                viewModel.startSyncTime(getDeviceId())
            }
        }
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        binding.deviceIpcSettingInfoName.text = uiState?.ipcDeviceInfo?.name
        binding.deviceIpcSettingInfoModel.text = uiState?.ipcDeviceInfo?.model

        binding.deviceIpcSettingInfoSwitch.updateUiState { configure ->
            configure?.apply {
                switchOn = if (viewModel.checkIsNetSpotMode()) {
                    !(uiState?.deviceSettingInfo?.sleep ?: false)
                } else {
                    uiState?.ipcDeviceInfo?.openStatus == DeviceDefine.SWITCH_ON
                }
            }
        }

        binding.deviceIpcSettingNoticeDetection.updateUiState { configure ->
            configure?.apply {
                switchOn = uiState?.ipcDeviceInfo?.isNotice == DeviceDefine.NOTICE_ON
            }
        }

        binding.deviceIpcSettingFlashLight.updateUiState { configure ->
            configure?.apply {
                val mode = uiState?.detectionInfo?.lightInfo?.mode ?: IPC_SCHEME_FLASH_LIGHT_MODE_CLOSE;
                subText = DeviceControlHelper.convertFlashLightStateTitleByMode(com.dylanc.longan.application, mode)
            }
        }

        binding.deviceIpcSettingLed.updateUiState { configure ->
            configure?.apply {
                switchOn = uiState?.ledState ?: false
            }
        }

        binding.deviceIpcSettingAlexa.visibilityEnable {
            uiState?.ipcDeviceInfo?.isAlexa == DeviceDefine.THIRD_CONTROL_ENABLE
        }

        binding.deviceIpcSettingGoogle.visibilityEnable {
            uiState?.ipcDeviceInfo?.isGoogle == DeviceDefine.THIRD_CONTROL_ENABLE
        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcSettingInfo -> {
                startActivity<IpcDeviceInfoActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_BIND_TYPE, bindTypeExtra)
                )
            }
            R.id.deviceIpcSettingNoticeSharing -> {
                startActivity<IpcSharingActivity>(Pair(INTENT_KEY_DEVICE_ID, getDeviceId()))
            }
            R.id.deviceIpcSettingNoticeCheck -> {
                NotificationUtil.openNotificationSetting(application)
            }
            R.id.deviceIpcSettingPicture -> {
                startActivity<IpcPictureSettingActivity>(Pair(INTENT_KEY_DEVICE_ID, getDeviceId()))
            }
            R.id.deviceIpcSettingMotionDetect -> {
                startActivity<IpcDetectionActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_DETECTION_TYPE, DETECTION_TYPE_MOTION)
                )
            }
            R.id.deviceIpcSettingSoundDetect -> {
                startActivity<IpcDetectionActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_DETECTION_TYPE, DETECTION_TYPE_SOUND)
                )
            }
            R.id.deviceIpcSettingHumanDetect -> {
                startActivity<IpcDetectionActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_DETECTION_TYPE, IPC_SCHEME_DETECTION_TYPE_PIR)
                )
            }
            R.id.deviceIpcSettingShooting -> {
                startActivity<IpcShootingActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_OPERATION_TYPE, MEDIA_MODE_OPERATION_TYPE_MODE)
                )
            }
            R.id.deviceIpcSettingFlashLight -> {
                startActivity<IpcFlashLightActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                )
            }
            R.id.deviceIpcSettingSwitchConnection -> {
                startActivity<IpcConnectionModeSwitchActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                )
            }
            R.id.deviceIpcSettingStorage -> {
                startActivity<IpcStorageActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_MODEL, deviceModelExtra)
                )
            }
            R.id.deviceIpcSettingAlexa -> {
                gotoThirdPartyPage(THIRD_PARTY_CONTROL_ALEXA_PATH)
            }
            R.id.deviceIpcSettingGoogle -> {
                gotoThirdPartyPage(THIRD_PARTY_CONTROL_GOOGLE_ASSISTANT_PATH)
            }
            R.id.btnIpcSettingRemoveDevice -> {
                tryRemoveDeviceDialog(getDeviceId(), deviceModelExtra.orEmpty())
            }
            R.id.btnIpcSettingResetDevice -> {
                showResetDeviceDialog(getDeviceId())
            }
            else -> {}
        }
    }

    private fun gotoThirdPartyPage(thirdPartyPagePath: String) {
        var pageUrl = when(thirdPartyPagePath) {
            THIRD_PARTY_CONTROL_ALEXA_PATH, THIRD_PARTY_CONTROL_GOOGLE_ASSISTANT_PATH -> {
                THIRD_PARTY_CONTROL_PARENT_URL.plus(thirdPartyPagePath)
            }
            else -> { "" }
        }
        if (pageUrl.isNullOrEmpty()) {
            return
        }
        startRouterActivity(
            ACTIVITY_PATH_GOTO_HYBRID_WEB_VIEW,
            Pair("INTENT_KEY_WEB_LOAD_URL", pageUrl),
            Pair("INTENT_KEY_WEB_IS_CACHE", true),
            Pair("INTENT_KEY_WEB_IS_SHOW_TITLE", true)
        )
    }

    private fun displayIpcFunctionView(deviceId: String, isOwner: Boolean) {
        with(binding) {
            deviceIpcSettingInfoSwitch.visibilityEnable { viewModel.checkIsSupportSleep(deviceId) }
            deviceIpcSettingNoticeCard.visibilityEnable { isOwner && !viewModel.checkIsNetSpotMode() }
            deviceIpcSettingPicture.visibilityEnable { isOwner }
            deviceIpcSettingMotionDetect.visibilityEnable { isOwner && !viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportMotionDetect(deviceId) }
            deviceIpcSettingSoundDetect.visibilityEnable { isOwner && !viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportSoundDetect(deviceId) }
            deviceIpcSettingHumanDetect.visibilityEnable { isOwner && viewModel.checkIsSupportHumanDetect(deviceId) }
            deviceIpcSettingShooting.visibilityEnable { isOwner && viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportShooting(deviceId) }
            deviceIpcSettingFlashLight.visibilityEnable { isOwner && !viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportFlashLight(deviceId) }
            deviceIpcSettingLed.visibilityEnable { isOwner && viewModel.checkIsSupportLed(deviceId) }
            deviceIpcSettingSyncTime.visibilityEnable { isOwner && !viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportSyncTime(deviceId) }
            deviceIpcSettingSwitchConnection.visibilityEnable { isOwner && viewModel.checkIsNetSpotMode() }
            deviceIpcSettingStorage.visibilityEnable { isOwner }
            btnIpcSettingResetDevice.visibilityEnable { isOwner && viewModel.checkIsSupportReset(deviceId) }
        }
    }

    private fun checkSystemNoticeView() {
        with(binding) {
            deviceIpcSettingNoticeTip.visibilityEnable {
                !NotificationUtil.isNotificationEnabled(application)
            }
            deviceIpcSettingNoticeCheck.visibilityEnable {
                !NotificationUtil.isNotificationEnabled(application)
            }
        }
    }

    private fun tryRemoveDeviceDialog(deviceId: String, deviceName: String) {
        if (!DeviceControlHelper.checkIsOwnerDevice(bindTypeExtra)) {
            return
        }
        val cloudIsSubscribed = false
        if (cloudIsSubscribed) {
            showUnsubscribeCloudDialog(deviceId, deviceName)
        } else {
            showRemoveDeviceDialog(deviceId, deviceName)
        }
    }

    private fun showRemoveDeviceDialog(deviceId: String, deviceName: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startRemoveDevice(deviceId, obtainUid(), parentDeviceIdExtra.orEmpty(), onlineStateExtra, false, removeDeviceLoadingState)
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    private fun showUnsubscribeCloudDialog(deviceId: String, deviceName: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startUnsubscribe(deviceId)
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    private fun showResetDeviceDialog(deviceId: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startResetDevice(deviceId)
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun obtainUid() : String {
        return ""
    }

}