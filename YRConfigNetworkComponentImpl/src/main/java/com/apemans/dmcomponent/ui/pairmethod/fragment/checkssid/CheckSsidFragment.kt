package com.apemans.dmcomponent.ui.pairmethod.fragment.checkssid

import android.Manifest
import android.view.View
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.databinding.PairdeviceFragmentWaitingBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.lastStep
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.utils.wifiSSID
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.dylanc.longan.launchWifiSettingsLauncher
import com.dylanc.longan.requestPermissionLauncher

class CheckSsidFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentWaitingBinding>() {

    private var grantedPermission = false

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun checkSsid() {
        val checkSsidValid = YRMiddleServiceManager.request(
            "yrcx://yripccomponentdevice/checknetspotssidvalid",
            mapOf("deviceSsid" to wifiSSID)
        ).data as? Boolean == true
        if (checkSsidValid) {
            nextStep()
        } else {
            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
                .setTitle("提示")
                .setContentText("请连接到 Victrue_ 开头的热点")
                .setPositiveTextName("确定")
                .setOnPositive {
                    launchWifiSettingsLauncher.launch(Unit)
                    it.dismiss()
                }
                .setNegativeTextName("取消")
                .setOnNegative {
                    lastStep()
                    it.dismiss()
                }
                .show()
        }
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            if (!grantedPermission) {
                checkSsid()
                grantedPermission = true
            }
        },
        onDenied = {
            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
                .setTitle("提示")
                .setContentText("请连接到 Victrue_ 开头的热点")
                .setPositiveTextName("确定")
                .setOnPositive {
                    launchWifiSettingsLauncher.launch(Unit)
                }
                .setNegativeTextName("取消")
                .setOnNegative {
                    lastStep()
                }
                .show()
        },
        onShowRequestRationale = {

        }
    )

    private val launchWifiSettingsLauncher = launchWifiSettingsLauncher {
        checkSsid()
    }
}