package com.apemans.tuya.component.ui.deviceinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.dylanc.longan.*
import com.google.gson.Gson
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityDeviceInfoBinding
import com.apemans.tuya.component.repository.TuyaRepository
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IRequestCallback
import com.tuya.smart.sdk.bean.DeviceBean

class DeviceInfoActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityDeviceInfoBinding>() {

    private val deviceId: String by safeIntentExtras("device_id")
    private lateinit var device: DeviceBean

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.information)
        binding.apply {
            labelModel.setupConfigure(lifecycleOwner)
            labelDeviceId.setupConfigure(lifecycleOwner)
            labelIp.setupConfigure(lifecycleOwner)
            labelMac.setupConfigure(lifecycleOwner)

            device = TuyaRepository.getDeviceBean(deviceId)!!

            labelModel.update {
                text = getString(R.string.model)
                subText = "SP01"
            }
            labelDeviceId.update {
                text = getString(R.string.device_id)
                subText = device.devId
                rightIconVisibility = View.VISIBLE
            }
            labelDeviceId.doOnClick {
                SmartDialog.build(supportFragmentManager, lifecycleOwner)
                    .setTitle(getString(R.string.device_id))
                    .setContentText(device.devId)
                    .setPositiveTextName(getString(R.string.copy_upper_case))
                    .setOnPositive {
                        (application.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let { clipboardManager ->
                            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, device.devId))
                            if (clipboardManager.hasPrimaryClip()) {
                                clipboardManager.primaryClip?.getItemAt(0)?.text
                            }
                            toast(R.string.copy_success)
                        }
                        it.dismiss()
                    }
                    .setNegativeTextName(getString(R.string.hide_upper_case))
                    .setOnNegative {
                        it.dismiss()
                    }
                    .show()
            }
            labelIp.update {
                text = getString(R.string.ip)
            }
            labelMac.update {
                text = getString(R.string.mac_address)
            }
            TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.device.get", "1.0",
                mapOf("devId" to device.devId), object : IRequestCallback {

                    override fun onSuccess(result: Any) {
                        val deviceInfo = Gson().fromJson(result.toString(), DeviceInfoBean::class.java)
                        labelIp.update { subText = deviceInfo.ip }
                        labelMac.update { subText = deviceInfo.mac }
                    }

                    override fun onFailure(errorCode: String?, errorMessage: String?) {
                        toast(errorMessage)
                    }
                })
        }
    }

    data class DeviceInfoBean(
        val name: String,
        val devId: String,
        val productId: String,
        val ip: String,
        val mac: String,
    )

    companion object {
        fun start(deviceId: String) =
            startActivity<DeviceInfoActivity>("device_id" to deviceId)
    }
}