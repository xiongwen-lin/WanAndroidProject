package com.apemans.dmcomponent.ui.pairmethod.fragment.connecthotspot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.contants.deviceId
import com.apemans.dmcomponent.contants.name
import com.apemans.dmcomponent.databinding.PairdeviceFragmentWaitingBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.ui.pairmethod.PairViewModel
import com.dylanc.longan.toast
import kotlinx.coroutines.CancellationException

class ConnectHotspotFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentWaitingBinding>() {
    private val viewModel: ConnectHotspotViewModel by activityViewModels()
    private val pairViewModel: PairViewModel by activityViewModels()

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        viewModel.exception.observe(viewLifecycleOwner) {
            toast(it.message)
        }
        pairViewModel.exception.observe(viewLifecycleOwner) {
            if (it is CancellationException) {
                pairViewModel.scanPairedDevice().observe(viewLifecycleOwner) {
                    nextStep(Bundle().apply {
                        name = it.first().name
                        deviceId = it.first().uuid
                    })
                }
            } else {
                toast(it.message)
            }
        }
        pairViewModel.scanPairedDevice().observe(viewLifecycleOwner) {
            nextStep(Bundle().apply {
                name = it.first().name
                deviceId = it.first().uuid
            })
        }

        viewModel.sendHotspotCmd().observe(viewLifecycleOwner) {
            viewModel.getHotspotState().observe(viewLifecycleOwner) {

            }
        }
    }

//    override fun onFragmentResume() {
//        super.onFragmentResume()
//        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//    }
//
//    private fun checkSsid() {
//        val checkSsidValid = YRMiddleServiceManager.request(
//            "yrcx://yripccomponentdevice/checknetspotssidvalid",
//            mapOf("deviceSsid" to wifiSSID)
//        ).data as? Boolean == true
//        if (checkSsidValid) {
//            viewModel.sendHotspotCmd().observe(viewLifecycleOwner) {
//                viewModel.getHotspotState().observe(viewLifecycleOwner) {
//
//                }
//            }
//        } else {
//            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
//                .setTitle("提示")
//                .setContentText("请连接到 Victrue_ 开头的热点")
//                .setPositiveTextName("确定")
//                .setOnPositive {
//                    launchWifiSettingsLauncher.launch(Unit)
//                    it.dismiss()
//                }
//                .setNegativeTextName("取消")
//                .setOnNegative {
//                    lastStep()
//                    it.dismiss()
//                }
//                .show()
//        }
//    }
//    private val requestPermissionLauncher = requestPermissionLauncher(
//        onGranted = {
//            checkSsid()
//        },
//        onDenied = {
//            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
//                .setTitle("提示")
//                .setContentText("请连接到 Victrue_ 开头的热点")
//                .setPositiveTextName("确定")
//                .setOnPositive {
//                    launchWifiSettingsLauncher.launch(Unit)
//                }
//                .setNegativeTextName("取消")
//                .setOnNegative {
//                    lastStep()
//                }
//                .show()
//        },
//        onShowRequestRationale = {
//
//        }
//    )
//
//    private val launchWifiSettingsLauncher = launchWifiSettingsLauncher {
//        checkSsid()
//    }
}