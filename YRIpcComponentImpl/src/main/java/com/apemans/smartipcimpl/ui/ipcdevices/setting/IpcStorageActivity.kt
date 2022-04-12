package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcStorageBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_DAMAGE
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_FORMATTING
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NORMAL
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NO_SD
import com.apemans.ipcchipproxy.scheme.bean.StorageInfo
import com.apemans.smartipcapi.webapi.PackageInfoResult
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.startRouterActivity
import com.apemans.smartipcimpl.constants.ACTIVITY_PATH_GOTO_HYBRID_WEB_VIEW
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.smartipcimpl.constants.INTENT_KEY_MODEL
import com.apemans.smartipcimpl.utils.DeviceControlHelper

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcStorageActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcStorageBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val deviceModelExtra: String? by intentExtras(INTENT_KEY_MODEL)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Storage"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        if (checkIsSupportCloud()) {
            viewModel.loadPackageInfo(getDeviceId(), true)
        }
        if (checkIsSupportDiskCard()) {
            viewModel.loadStorageInfo(getDeviceId())
            viewModel.loadLoopRecord(getDeviceId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        var isSubDevice = false
        with(binding) {
            deviceIpcStorageDiskCardLoopRecord.setupConfigure(lifecycleOwner)

            deviceIpcStorageDiskCardInfo.text = if (isSubDevice) getString(R.string.camera_share_title) else getString(R.string.camera_share_title)

            deviceIpcStorageDiskCardLoopRecord.visibilityEnable {
                false
            }
            deviceIpcStorageDiskCardLoopRecord.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    dividerLineVisible = View.GONE
                }
            }

        }

        displayStorageInfoView(checkIsSupportCloud())
        displayDiskCardView(checkIsSupportDiskCard() || isSubDevice)

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        binding.deviceIpcStorageDiskCardLoopRecord.mListener = { isChecked ->
            viewModel.switchLoopRecord(getDeviceId(), isChecked)
        }

        registerOnViewClick(
            binding.deviceIpcStorageCloudSubscribe,
            binding.deviceIpcStorageCloudUnSubscribe,
            binding.deviceIpcStorageDiskCardFormat,
            block = ::handleOnViewClick
        )
    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {
        refreshCloudView(uiState?.cloudInfo)
        refreshDiskCardView(uiState?.storageInfo)

        with(binding) {
            deviceIpcStorageDiskCardLoopRecord.updateUiState {
                it?.apply {
                    switchOn = uiState?.loopRecordState ?: false
                }
            }
        }
    }

    private fun refreshCloudView(cloudInfo: PackageInfoResult?) {
        var cloudIsValid = false
        var cloudSubscribe = false
        cloudInfo?.let {
            cloudIsValid = DeviceControlHelper.checkCloudValid(cloudInfo.end_time, 8.0f)
            cloudSubscribe = DeviceControlHelper.checkCloudSubscribe(cloudInfo.status)
        }
        with(binding) {
            deviceIpcStorageCloudIcon.setImageResource(R.drawable.device_gateway_camera)
            deviceIpcStorageCloudSubscribe.isEnabled = !cloudIsValid
            deviceIpcStorageCloudSubscribe.text = if (cloudIsValid) {
                DeviceControlHelper.convertCloudExpireTime(cloudInfo?.total_time ?: 0).toString()
            } else {
                getString(R.string.camera_share_title)
            }
            deviceIpcStorageCloudUnSubscribe.visibilityEnable {
                cloudSubscribe
            }
        }
    }

    private fun refreshDiskCardView(storageInfo : StorageInfo?) {

        var storageStatus = storageInfo?.status ?: IPC_SCHEME_STORAGE_STATUS_NO_SD

        with(binding) {
            deviceIpcStorageDiskCardInfo.setTextColor(com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_sub_text_color))
            deviceIpcStorageDiskCardFormat.visibilityEnable { false }
            deviceIpcStorageDiskCardLoopRecord.visibilityEnable {
                storageStatus != IPC_SCHEME_STORAGE_STATUS_NO_SD
            }
            when(storageStatus) {
                IPC_SCHEME_STORAGE_STATUS_NORMAL -> {
                    deviceIpcStorageDiskCardInfo.setTextColor(com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_text_color))
                    deviceIpcStorageDiskCardInfo.text = "${DeviceControlHelper.convertStorageCapacity(storageInfo?.free ?: 0)}GB"
                    deviceIpcStorageDiskCardFormat.visibilityEnable { true }
                }
                IPC_SCHEME_STORAGE_STATUS_FORMATTING-> {
                    deviceIpcStorageDiskCardInfo.text = "(${storageInfo?.process ?: 0}%)"
                }
                IPC_SCHEME_STORAGE_STATUS_NO_SD -> {
                    deviceIpcStorageDiskCardInfo.text = getString(R.string.camera_share_title)
                }
                IPC_SCHEME_STORAGE_STATUS_DAMAGE -> {
                    deviceIpcStorageDiskCardInfo.text = getString(R.string.camera_share_title)
                }
                else -> {}
            }
        }
    }

    private fun displayStorageInfoView(show: Boolean) {
        with(binding) {
            deviceIpcStorageCloudTitle.visibilityEnable { show }
            deviceIpcStorageCloudCard.visibilityEnable { show }
        }
    }

    private fun displayDiskCardView(show: Boolean) {
        with(binding) {
            deviceIpcStorageDiskTitle.visibilityEnable { show }
            deviceIpcStorageDiskCard.visibilityEnable { show }
        }
    }

    private fun checkIsSupportCloud() : Boolean {
        return !viewModel.checkIsNetSpotMode()
    }

    private fun checkIsSupportDiskCard() : Boolean {
        return viewModel.checkIsSupportFormatStorage(getDeviceId())
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcStorageCloudSubscribe -> {
                startRouterActivity(
                    ACTIVITY_PATH_GOTO_HYBRID_WEB_VIEW,
                    Pair("INTENT_KEY_WEB_LOAD_URL", DeviceControlHelper.createCloudPackUrl(obtainWebUrl(), getDeviceId(), deviceModelExtra, "", "")),
                    Pair("INTENT_KEY_WEB_IS_CACHE", false),
                    Pair("INTENT_KEY_WEB_IS_SHOW_TITLE", true)
                )
            }
            R.id.deviceIpcStorageCloudUnSubscribe -> {
                showUnsubscribeCloudDialog(getDeviceId())
            }
            R.id.deviceIpcStorageDiskCardFormat -> {
                showFormatStorageDialog(getDeviceId())
            }
            else -> {}
        }
    }

    private fun showUnsubscribeCloudDialog(deviceId: String) {
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

    private fun showFormatStorageDialog(deviceId: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startFormatStorage(deviceId)
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun obtainWebUrl() : String {
        return ""
    }
}