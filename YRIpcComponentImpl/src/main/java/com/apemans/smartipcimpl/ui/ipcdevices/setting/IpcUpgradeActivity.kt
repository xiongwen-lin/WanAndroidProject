package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcUpgradeBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.smartipcimpl.utils.visibilityEnable
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.smartipcapi.webapi.FirmwareVersion
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
class IpcUpgradeActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcUpgradeBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val modelExtra: String? by intentExtras(INTENT_KEY_MODEL)

    private var firstUpgradeStateChecked = false

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = getString(R.string.camera_share_title)
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUpgradeInfo(getDeviceId(), getModel())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {

        viewModel.upgradeInfoState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        viewModel.upgradeState.observe(lifecycleOwner) {
            refreshUpgradeUi((it ?: DeviceDefine.UPGRADE_TYPE_NORMAL))
        }

        registerOnViewClick(
            binding.deviceIpcUpgradeOperation,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcUpgradeOperation -> {
                showUpgradeDialog(getDeviceId(), viewModel.upgradeInfoState.value)
            }
            else -> {}
        }
    }

    private fun refreshUiState(upgradeInfo: FirmwareVersion?) {
        val upgradeAble = DeviceControlHelper.compareVersion(upgradeInfo?.version_code.orEmpty(), upgradeInfo?.currentVersionCode.orEmpty()) > 0
        with(binding) {
            if (!firstUpgradeStateChecked) {
                deviceIpcUpgradeIcon.visibilityEnable { true }
                deviceIpcUpgradeProgress.textColor = android.R.color.transparent
                deviceIpcUpgradeProgress.reachBarColor = com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_color)
                binding.deviceIpcUpgradeProgress.progress = if (upgradeAble) 0 else 100
            }
            deviceIpcUpgradeVersion.text = getString(R.string.camera_share_title)
                .plus(":")
                .plus(upgradeInfo?.currentVersionCode.orEmpty())

            deviceIpcUpgradeInfoContainer.visibilityEnable { upgradeAble }
            deviceIpcUpgradeInfo.visibilityEnable { !upgradeAble }

            deviceIpcUpgradeNewVersion.text = getString(R.string.camera_share_title)
                .plus(":")
                .plus(upgradeInfo?.version_code.orEmpty())

            deviceIpcUpgradeLog.text = upgradeInfo?.log.orEmpty()
        }
        if (upgradeAble && !firstUpgradeStateChecked) {
            firstUpgradeStateChecked = true
            viewModel.checkUpgradeState(getDeviceId(), false)
        }
    }

    private fun refreshUpgradeUi(type: Int) {
        binding.deviceIpcUpgradeIcon.visibilityEnable { !checkIsUpgrading(type) }
        binding.deviceIpcUpgradeProgress.textColor = if (checkIsUpgrading(type)) com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_text_color) else android.R.color.transparent
        binding.deviceIpcUpgradeProgress.reachBarColor = com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_color)
        binding.deviceIpcUpgradeOperation.text = getString(R.string.camera_share_title)
        binding.deviceIpcUpgradeOperation.visibilityEnable { !checkIsUpgrading(type) }
        when(type) {
            DeviceDefine.UPGRADE_TYPE_NORMAL -> {
                binding.deviceIpcUpgradeProgress.progress = 0

            }
            DeviceDefine.UPGRADE_TYPE_DOWNLOADING -> {
                binding.deviceIpcUpgradeProgress.progress = 20
            }
            DeviceDefine.UPGRADE_TYPE_DOWNLOADED -> {
                binding.deviceIpcUpgradeProgress.progress = 40
            }
            DeviceDefine.UPGRADE_TYPE_INSTALLING -> {
                binding.deviceIpcUpgradeProgress.progress = 60
            }
            DeviceDefine.UPGRADE_TYPE_SUCCESS -> {
                binding.deviceIpcUpgradeProgress.progress = 80
            }
            DeviceDefine.UPGRADE_TYPE_FINISH -> {
                binding.deviceIpcUpgradeIcon.visibilityEnable { false }
                binding.deviceIpcUpgradeProgress.textColor = com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_text_color)
                binding.deviceIpcUpgradeOperation.visibilityEnable {false}
                binding.deviceIpcUpgradeProgress.progress = 100
                showUpgradeFinishDialog()
            }
            DeviceDefine.UPGRADE_TYPE_FAIL -> {
                binding.deviceIpcUpgradeProgress.reachBarColor = com.apemans.base.utils.CompatUtil.getColor(application, R.color.theme_warning_color)
                binding.deviceIpcUpgradeProgress.progress = 100
                binding.deviceIpcUpgradeOperation.text = getString(R.string.camera_share_title)
            }
            else -> {}
        }
    }

    private fun checkUpgradeInfoValid(updateInfo: FirmwareVersion?) : Boolean {
        return updateInfo?.let {
            !it.model.isNullOrEmpty() && !it.version_code.isNullOrEmpty() && !it.key.isNullOrEmpty()
        } ?: false
    }

    private fun checkIsUpgrading(type: Int) : Boolean {
        return type == DeviceDefine.UPGRADE_TYPE_DOWNLOADING || type == DeviceDefine.UPGRADE_TYPE_DOWNLOADED
                || type == DeviceDefine.UPGRADE_TYPE_INSTALLING || type == DeviceDefine.UPGRADE_TYPE_SUCCESS
    }

    private fun showUpgradeDialog(deviceId: String, updateInfo: FirmwareVersion?) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                if (checkUpgradeInfoValid(updateInfo)) {
                    updateInfo?.let {
                        viewModel.startUpgrade(deviceId, it.model.orEmpty(), it.version_code.orEmpty(), it.key.orEmpty(), it.md5.orEmpty())
                    }
                }
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    private fun showUpgradeFinishDialog() {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setContentText(getString(R.string.camera_share_title))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setOnPositive {
                it.dismiss()
                finish()
            }
            .show()
    }

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }

    private fun getModel() : String {
        return modelExtra.orEmpty()
    }
}