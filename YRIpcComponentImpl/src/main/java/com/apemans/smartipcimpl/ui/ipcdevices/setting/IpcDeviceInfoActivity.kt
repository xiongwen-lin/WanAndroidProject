package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcDeviceInfoBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.IPv4IntTransformer
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.apemans.quickui.alerter.alertInfo
import com.apemans.smartipcimpl.constants.*


/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 3:20 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcDeviceInfoActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcDeviceInfoBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val bindTypeExtra: Int by intentExtras(INTENT_KEY_BIND_TYPE, DeviceDefine.BIND_TYPE_SHARER)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.checkIsNetSpotMode()) {
            viewModel.loadDeviceNetSpotConfigure(getDeviceId())
            viewModel.loadNetSpotInfo(getDeviceId())
        } else {
            viewModel.loadDeviceInfo(getDeviceId())
        }
    }

    private fun setupUI() {
        displayDeviceInfoFunctionView(getDeviceId(), DeviceControlHelper.checkIsOwnerDevice(bindTypeExtra))
        val showBlePw = viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportChangeNetSpotPw(getDeviceId())
        val hideDeviceIdDivider = viewModel.checkIsNetSpotMode() && !viewModel.checkIsSupportChangeNetSpotPw(getDeviceId())
        with(binding) {
            deviceIpcDeviceInfoName.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoOwner.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoPower.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoModel.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoId.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoVersion.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoIp.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoMac.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoSsid.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoBleName.setupConfigure(lifecycleOwner)
            deviceIpcDeviceInfoNetSpotPw.setupConfigure(lifecycleOwner)

            deviceIpcDeviceInfoName.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
            deviceIpcDeviceInfoOwner.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoPower.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoModel.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoId.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = getDeviceId()
                    rightIconRes = R.drawable.right_arrow_black
                    rightIconVisibility = View.VISIBLE
                    dividerLineVisible = if (hideDeviceIdDivider) View.GONE else View.VISIBLE
                }
            }
            deviceIpcDeviceInfoVersion.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
            deviceIpcDeviceInfoIp.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoMac.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    dividerLineVisible = if (showBlePw) View.VISIBLE else View.GONE
                }
            }
            deviceIpcDeviceInfoBleName.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoSsid.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceIpcDeviceInfoNetSpotPw.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconRes = R.drawable.right_arrow_black
                    rightIconVisibility = View.VISIBLE
                    dividerLineVisible = View.GONE
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            binding.deviceIpcDeviceInfoName,
            binding.deviceIpcDeviceInfoId,
            binding.deviceIpcDeviceInfoVersion,
            binding.deviceIpcDeviceInfoNetSpotPw,
            block = ::handleOnViewClick
        )

    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        with(binding) {
            deviceIpcDeviceInfoName.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.name.orEmpty()
                }
            }

            deviceIpcDeviceInfoOwner.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.ownerAccount.orEmpty()
                }
            }

            deviceIpcDeviceInfoPower.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.batteryLevel?.toString() ?: ""
                }
            }

            deviceIpcDeviceInfoModel.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.model.orEmpty()
                }
            }

            deviceIpcDeviceInfoId.updateUiState {
                it?.apply {
                    subText = if (uiState?.ipcDeviceInfo?.device_id.isNullOrBlank()) getDeviceId() else uiState?.ipcDeviceInfo?.device_id.orEmpty()
                }
            }

            deviceIpcDeviceInfoVersion.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.version.orEmpty()
                }
            }

            deviceIpcDeviceInfoIp.updateUiState {
                it?.apply {
                    subText = IPv4IntTransformer.bigNumToIP(uiState?.ipcDeviceInfo?.localIp ?: 0)
                }
            }

            deviceIpcDeviceInfoMac.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.mac.orEmpty()
                }
            }

            deviceIpcDeviceInfoBleName.updateUiState {
                it?.apply {
                    subText = uiState?.netSpotInfo?.ssid.orEmpty()
                }
            }
            deviceIpcDeviceInfoSsid.updateUiState {
                it?.apply {
                    subText = uiState?.netSpotInfo?.ssid.orEmpty()
                }
            }
        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcDeviceInfoName -> {
                if (DeviceControlHelper.checkIsOwnerDevice(bindTypeExtra)) {
                    startActivity<IpcNameDeviceActivity>(
                        Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                        Pair(INTENT_KEY_DEVICE_NAME, viewModel.uiState.value?.ipcDeviceInfo?.name.orEmpty())
                    )
                }
            }
            R.id.deviceIpcDeviceInfoId -> {
                copyDeviceId(getDeviceId())
            }
            R.id.deviceIpcDeviceInfoVersion -> {
                val upgradeEnable = DeviceControlHelper.checkIsOwnerDevice(bindTypeExtra) && !viewModel.checkIsNetSpotMode()
                if (upgradeEnable) {
                    startActivity<IpcUpgradeActivity>(
                        Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                        Pair(INTENT_KEY_MODEL, viewModel.uiState.value?.ipcDeviceInfo?.model.orEmpty())
                    )
                }
            }
            R.id.deviceIpcDeviceInfoNetSpotPw -> {
                startActivity<IpcChangePasswordActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId())
                )
            }
            else -> {}
        }
    }

    private fun copyDeviceId(deviceId: String?) {
        if (deviceId.isNullOrEmpty()) {
            return
        }
        try {
            //获取剪贴板管理器：

            //获取剪贴板管理器：
            val cm: ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("DeviceId", deviceId.orEmpty())
            // 将ClipData内容放到系统剪贴板里。
            cm?.setPrimaryClip(mClipData)
            alertInfo { getString(R.string.camera_share_title) }
        } catch (e : Exception) {
        }
    }

    private fun displayDeviceInfoFunctionView(deviceId: String, isOwner: Boolean) {
        with(binding) {
            deviceIpcDeviceInfoOwner.visibilityEnable {
                !viewModel.checkIsNetSpotMode()
            }
            deviceIpcDeviceInfoPower.visibilityEnable {
                viewModel.checkIsSupportHumanDetect(getDeviceId())
            }
            deviceIpcDeviceInfoVersion.visibilityEnable {
                !(viewModel.checkIsNetSpotMode() && !viewModel.checkIsSupportChangeNetSpotPw(getDeviceId()))
            }
            deviceIpcDeviceInfoIp.visibilityEnable {
                !viewModel.checkIsNetSpotMode()
            }
            deviceIpcDeviceInfoMac.visibilityEnable {
                !viewModel.checkIsNetSpotMode()
            }
            deviceIpcDeviceInfoBleName.visibilityEnable {
                isOwner && viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportChangeNetSpotPw(getDeviceId())
            }
            deviceIpcDeviceInfoBleName.visibilityEnable {
                isOwner && viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportChangeNetSpotPw(getDeviceId())
            }
            deviceIpcDeviceInfoNetSpotPw.visibilityEnable {
                isOwner && viewModel.checkIsNetSpotMode() && viewModel.checkIsSupportChangeNetSpotPw(getDeviceId())
            }
        }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}