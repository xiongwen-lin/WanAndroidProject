package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.ui.ipcdevices.setting.bean.IpcSettingUIState
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_DAMAGE
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_FORMATTING
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NORMAL
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NO_SD
import com.apemans.ipcchipproxy.scheme.bean.StorageInfo
import com.apemans.quickui.alerter.alertInfo
import com.apemans.quickui.label.LABEL_SELECTION_1
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.databinding.DeviceActivityGatewayDeviceInfoBinding
import com.apemans.smartipcimpl.utils.IPv4IntTransformer
import com.apemans.smartipcimpl.utils.SCOPE_TASK_STATE_RESULT_SUCCESS
import com.apemans.smartipcimpl.utils.ScopeTaskResult


/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 3:20 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class GatewayDeviceInfoActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityGatewayDeviceInfoBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val onlineStateExtra: Boolean by intentExtras(INTENT_KEY_DEVICE_ONLINE_STATE, false)

    private var removeDeviceLoadingState: MutableLiveData<ScopeTaskResult<Any>> = MutableLiveData<ScopeTaskResult<Any>>()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Camera data"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDeviceInfo(getDeviceId())
        viewModel.loadDeviceSetting(getDeviceId())
        viewModel.loadLedState(getDeviceId())
        viewModel.loadStorageInfo(getDeviceId())
    }

    private fun setupUI() {
        with(binding) {
            deviceGatewayDeviceInfoId.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoOnlineStatue.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoMac.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoLed.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoSyncTime.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoStorage.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoVersion.setupConfigure(lifecycleOwner)
            deviceGatewayDeviceInfoIp.setupConfigure(lifecycleOwner)

            deviceGatewayDeviceInfoId.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = getDeviceId()
                    rightIconVisibility = View.GONE
                }
            }
            deviceGatewayDeviceInfoOnlineStatue.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceGatewayDeviceInfoMac.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                }
            }
            deviceGatewayDeviceInfoLed.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                }
            }
            deviceGatewayDeviceInfoSyncTime.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    detailText = getString(R.string.camera_share_title)
                    selection1 = getString(R.string.camera_share_title)
                    selectionIndex = LABEL_SELECTION_1
                }
            }
            deviceGatewayDeviceInfoStorage.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
            deviceGatewayDeviceInfoVersion.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
            deviceGatewayDeviceInfoIp.updateUiState {
                it?.apply {
                    text = getString(R.string.camera_share_title)
                    subText = ""
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
            binding.deviceGatewayDeviceInfoStorage,
            binding.deviceGatewayDeviceInfoVersion,
            binding.btnGatewayDeviceRemoveDevice,
            binding.btnGatewayDeviceResetDevice,
            block = ::handleOnViewClick
        )

        binding.deviceGatewayDeviceInfoLed.mListener = { isChecked ->
            if (checkGatewayIsOnline()) {
                viewModel.setLedState(getDeviceId(), isChecked)
            } else {
                showGatewayOfflineTip()
            }
        }

        binding.deviceGatewayDeviceInfoSyncTime.mListener = { index ->
            if (checkGatewayIsOnline()) {
                if (index == LABEL_SELECTION_1) {
                    viewModel.startSyncTime(getDeviceId())
                }
            } else {
                showGatewayOfflineTip()
            }
        }

    }

    private fun refreshUiState(uiState: IpcSettingUIState?) {

        with(binding) {

            deviceGatewayDeviceInfoId.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.device_id.orEmpty()
                }
            }

            deviceGatewayDeviceInfoOnlineStatue.updateUiState {
                it?.apply {
                    subText = if (uiState?.ipcDeviceInfo?.online == DeviceDefine.ONLINE) getString(R.string.camera_share_title) else getString(R.string.camera_share_title)
                }
            }

            deviceGatewayDeviceInfoMac.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.mac.orEmpty()
                }
            }

            deviceGatewayDeviceInfoLed.updateUiState { configure ->
                configure?.apply {
                    switchOn = uiState?.ledState ?: false
                }
            }

            deviceGatewayDeviceInfoStorage.updateUiState {
                it?.apply {
                    subText = refreshStorageInfo(uiState?.storageInfo)
                }
            }

            deviceGatewayDeviceInfoVersion.updateUiState {
                it?.apply {
                    subText = uiState?.ipcDeviceInfo?.version.orEmpty()
                }
            }

            deviceGatewayDeviceInfoIp.updateUiState {
                it?.apply {
                    subText = IPv4IntTransformer.bigNumToIP(uiState?.ipcDeviceInfo?.localIp ?: 0)
                }
            }

        }
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceGatewayDeviceInfoStorage -> {
                if (!checkGatewayIsOnline()) {
                    showGatewayOfflineTip()
                    return
                }
                val formatStorageEnable = viewModel.uiState?.value?.storageInfo?.status == IPC_SCHEME_STORAGE_STATUS_NORMAL
                if (formatStorageEnable) {
                    showFormatStorageDialog(getDeviceId())
                }
            }
            R.id.deviceGatewayDeviceInfoVersion -> {
                startActivity<IpcUpgradeActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                    Pair(INTENT_KEY_MODEL, viewModel.uiState.value?.ipcDeviceInfo?.model.orEmpty())
                )
            }
            R.id.btnGatewayDeviceRemoveDevice -> {
                showRemoveDeviceDialog(getDeviceId(), "")
            }
            R.id.btnGatewayDeviceResetDevice -> {
                if (!checkGatewayIsOnline()) {
                    showGatewayOfflineTip()
                    return
                }
                showResetDeviceDialog("")
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

    private fun refreshStorageInfo(storageInfo: StorageInfo?) : String {
        return storageInfo?.let {
            when (it.status) {
                IPC_SCHEME_STORAGE_STATUS_NORMAL -> {
                    var total = it.total ?: 0
                    var free = it.free ?: 0
                    val useSpace = if (total > 0 && free >= 0 && total - free >= 0) Math.floor((total - free) / total * 100 * 10 + 0.5) / 10 else -1
                    String.format("%1\$s", useSpace.toString().plus("%"))
                }
                IPC_SCHEME_STORAGE_STATUS_FORMATTING -> {
                    "${it.process}%"
                }
                IPC_SCHEME_STORAGE_STATUS_NO_SD -> {
                    getString(R.string.camera_share_title)
                }
                IPC_SCHEME_STORAGE_STATUS_DAMAGE -> {
                    getString(R.string.camera_share_title)
                }
                else -> {
                    ""
                }
            }
        } ?: ""
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

    private fun showRemoveDeviceDialog(deviceId: String, deviceName: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startRemoveDevice(deviceId, obtainUid(), parentDeviceId = "", onlineStateExtra, true, removeDeviceLoadingState)
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

    private fun checkGatewayIsOnline() : Boolean {
        return viewModel.uiState.value?.ipcDeviceInfo?.online == DeviceDefine.ONLINE
    }

    private fun showGatewayOfflineTip() {
        alertInfo { getString(R.string.camera_share_title) }
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun obtainUid() : String {
        return ""
    }
}