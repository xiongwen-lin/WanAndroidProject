package com.apemans.dmcomponent.ui.pairmethod.fragment.scanbluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import com.apemans.bluetooth.SmartBleManager
import com.apemans.bluetooth.callback.ConnectionCallback
import com.apemans.bluetooth.callback.EnableNotificationCallback
import com.apemans.bluetooth.callback.ScanBleDeviceCallback
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.databinding.PairdeviceFragmentScanBluetoothBinding
import com.apemans.dmcomponent.ui.pairdevice.nearbydevice.NearbyDeviceActivity
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.utils.BLE_CMD_LOGIN
import com.apemans.dmcomponent.utils.BLE_CMD_LOGIN_RSP
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.dylanc.longan.*
import no.nordicsemi.android.ble.callback.DataReceivedCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult

class ScanBluetoothFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentScanBluetoothBinding>() {

    private val scanResultCache = mutableListOf<ScanResult>()

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        binding.ivDevice1.doOnClick {
            connectDevice(scanResultCache[0])
        }
        binding.ivDevice2.doOnClick {
            connectDevice(scanResultCache[1])
        }
        binding.ivDevice3.doOnClick {
            connectDevice(scanResultCache[2])
        }
        binding.ivDevice4.doOnClick {
            connectDevice(scanResultCache[3])
        }
        binding.btnMore.doOnClick {
            startActivity<NearbyDeviceActivity>()
        }
        binding.ivScanRadar.startAnimation(
            RotateAnimation(
                0f,
                359f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 1500
                repeatCount = Animation.INFINITE
            })

        SmartBleManager.initCore(requireContext())
        SmartBleManager.core.observerScanCallback(this, scanCallback)
        SmartBleManager.core.observerConnectionCallback(this, connectCallback)
        SmartBleManager.core.observerBleNotificationOpenCallback(requireContext(), notifyCallback)
        SmartBleManager.core.observerBleMessageCallback(this, dataCallback)

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        enableBluetoothLauncher.launchAndEnableLocation("",
//            onLocationEnabled = {
//                SmartBleManager.core
//                    .configServiceUUID("0000181c-0000-1000-8000-00805f9b34fb")
//                    .configWriteUUID("0000181d-0000-1000-8000-00805f9b34fb")
//                    //.configReadUUID("00002222-0000-1000-8000-00805f9b34fb")
//                    .configNotifyUUID("0000181e-0000-1000-8000-00805f9b34fb")
//                    .configReceiveMessageCharacteristicUUID("0000181e-0000-1000-8000-00805f9b34fb")
//                    .configAutoSplitLongData(true)
//
//                SmartBleManager.core.startScan {
//
//                }
//            }, onPermissionDenied = {
//
//            }, onExplainRequestPermission = {
//
//            })
    }

    private fun connectDevice(scanResult: ScanResult) {
        SmartBleManager.core.stopScan()
        SmartBleManager.core.connectBluetooth(scanResult.device)
    }

    private val scanCallback = object : ScanBleDeviceCallback {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            logDebug(result)
        }

        override fun onBatchScanResultsByFilter(results: List<ScanResult>) {
            val list = results.filter {
                val deviceName = it.scanRecord?.deviceName
                YRMiddleServiceManager.request(
                    "yrcx://yripccomponentdevice/checknetspotssidvalid",
                    mapOf("deviceSsid" to deviceName.orEmpty())
                ).data as? Boolean == true
            }
            binding.setScanResult(list)
        }

        override fun onScanFailed(errorCode: Int) {
            binding.setScanResult(emptyList())
        }
    }

    private val connectCallback = object : ConnectionCallback {
        override fun onDeviceConnecting(device: BluetoothDevice) {

        }

        override fun onDeviceFailedToConnectTimeout(device: BluetoothDevice) {

        }

        override fun onDeviceFailedToConnectNotSupport(device: BluetoothDevice) {

        }

        override fun onDeviceFailedToConnectReasonUnknown(device: BluetoothDevice, reason: Int) {

        }

        override fun onDeviceConnected(device: BluetoothDevice) {
        }

        override fun onDeviceReady(device: BluetoothDevice) {
            SmartBleManager.core.send(BLE_CMD_LOGIN)
        }

        override fun onDeviceDisconnecting(device: BluetoothDevice) {

        }

        override fun onDeviceLinkLost(device: BluetoothDevice) {

        }

        override fun onDeviceTerminateLocalHost(device: BluetoothDevice) {

        }

        override fun onDeviceTerminateRemoteHost(device: BluetoothDevice) {

        }

        override fun onDeviceDisConnectUnknownReason(device: BluetoothDevice, reason: Int) {

        }

    }

    private val notifyCallback = object : EnableNotificationCallback {
        override fun onRequestCompleted(device: BluetoothDevice) {

        }

        override fun onRequestFailed(device: BluetoothDevice, status: Int) {

        }
    }

    private val dataCallback = DataReceivedCallback { device, data ->
        val message = data.getStringValue(0)
        logDebug(message)
        if (message?.startsWith(BLE_CMD_LOGIN_RSP) == true) {
            nextStep()
        }
    }

    private fun PairdeviceFragmentScanBluetoothBinding.setScanResult(results: List<ScanResult>) {
        scanResultCache.clear()
        scanResultCache.addAll(results)
        ivDevice1.isVisible = results.isNotEmpty()
        tvDevice1.isVisible = results.isNotEmpty()
        ivDevice2.isVisible = results.size >= 2
        tvDevice2.isVisible = results.size >= 2
        ivDevice3.isVisible = results.size >= 3
        tvDevice3.isVisible = results.size >= 3
        ivDevice4.isVisible = results.size >= 4
        tvDevice4.isVisible = results.size >= 4
        btnMore.isVisible = results.size > 4

        for (i in results.indices) {
            when (i) {
                0 -> tvDevice1
                1 -> tvDevice2
                2 -> tvDevice3
                3 -> tvDevice4
                else -> null
            }?.text = results[i].scanRecord?.deviceName
        }
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            enableBluetoothLauncher.launch()
        },
        onDenied = {

        },
        onShowRequestRationale = {

        }
    )

    private val enableBluetoothLauncher = enableBluetoothLauncher(
        onLocationDisabled = {

        },
        onBluetoothEnabled = { enabled ->
            if (enabled){
                SmartBleManager.core
                    .configServiceUUID("0000181c-0000-1000-8000-00805f9b34fb")
                    .configWriteUUID("0000181d-0000-1000-8000-00805f9b34fb")
                    //.configReadUUID("00002222-0000-1000-8000-00805f9b34fb")
                    .configNotifyUUID("0000181e-0000-1000-8000-00805f9b34fb")
                    .configReceiveMessageCharacteristicUUID("0000181e-0000-1000-8000-00805f9b34fb")
                    .configAutoSplitLongData(true)

                SmartBleManager.core.startScan {

                }
            }
        })

}