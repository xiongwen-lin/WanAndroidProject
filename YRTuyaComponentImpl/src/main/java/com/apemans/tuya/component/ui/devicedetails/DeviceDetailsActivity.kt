package com.apemans.tuya.component.ui.devicedetails

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.tuya.module.api.ACTIVITY_PATH_TUYA_DEVICE_DETAILS
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityDeviceDetailsBinding
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.ui.deviceinfo.DeviceInfoActivity
import com.apemans.tuya.component.ui.devicesharelist.DeviceShareListActivity
import com.tuya.smart.android.device.bean.UpgradeInfoBean
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IGetOtaInfoCallback
import com.tuya.smart.sdk.bean.DeviceBean

@Route(path = ACTIVITY_PATH_TUYA_DEVICE_DETAILS)
class DeviceDetailsActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityDeviceDetailsBinding>() {

    private val deviceId: String by safeIntentExtras("device_id")
    private val isShare: Boolean by intentExtras("is_share", false)
    private lateinit var device: DeviceBean
    private val viewModel: DeviceDetailsViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.settings)
        binding.apply {
            labelName.setupConfigure(lifecycleOwner)
            labelDeviceSharing.setupConfigure(lifecycleOwner)
            labelInformation.setupConfigure(lifecycleOwner)
            labelFirmware.setupConfigure(lifecycleOwner)

            device = TuyaRepository.getDeviceBean(deviceId)!!
            TuyaHomeSdk.newOTAInstance(deviceId).getOtaInfo(object : IGetOtaInfoCallback {
                override fun onSuccess(upgradeInfoBeans: MutableList<UpgradeInfoBean>) {
                    upgradeInfoBeans.forEach {
                        if (it.type == 0) {
                            labelFirmware.update { subText = it.currentVersion }
                        }
                    }
                }

                override fun onFailure(code: String?, error: String?) {
                    toast(error)
                }
            })
            labelName.update {
                text = getString(R.string.name)
                subText = device.name
            }
            labelDeviceSharing.update { text = getString(R.string.dev_sharing) }
            labelDeviceSharing.doOnClick {
                DeviceShareListActivity.start(deviceId)
            }
            labelInformation.update { text = getString(R.string.information) }
            labelInformation.doOnClick {
                DeviceInfoActivity.start(deviceId)
            }
            labelFirmware.update { text = getString(R.string.firmware) }
            btnRemove.doOnClick {
                SmartDialog.build(supportFragmentManager, lifecycleOwner)
                    .setTitle(getString(R.string.remove_device))
                    .setContentText(String.format(getString(R.string.device_remove_tip), device.name))
                    .setPositiveTextName(getString(R.string.confirm))
                    .setOnPositive {
                        if (!isShare) {
                            viewModel.removeDevice(deviceId)
                        } else {
                            viewModel.removeShareDevice(deviceId)
                        }.observe(lifecycleOwner) {
                            toast("删除成功")
                            finish()
                        }
                        it.dismiss()
                    }
                    .setNegativeTextName(getString(R.string.cancel))
                    .setOnNegative {
                        it.dismiss()
                    }
                    .show()
            }
            btnReset.doOnClick {
                SmartDialog.build(supportFragmentManager, lifecycleOwner)
                    .setTitle(getString(R.string.device_settings_reset))
                    .setContentText(getString(R.string.device_settings_reset_info))
                    .setPositiveTextName(getString(R.string.confirm))
                    .setOnPositive {
                        viewModel.resetFactory(deviceId).observe(lifecycleOwner) {
                            toast("重制成功")
                            finish()
                        }
                        it.dismiss()
                    }
                    .setNegativeTextName(getString(R.string.cancel))
                    .setOnNegative {
                        it.dismiss()
                    }
                    .show()
            }
        }
    }
}