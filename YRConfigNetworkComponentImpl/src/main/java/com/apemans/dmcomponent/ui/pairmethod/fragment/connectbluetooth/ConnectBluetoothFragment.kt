package com.apemans.dmcomponent.ui.pairmethod.fragment.connectbluetooth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.apemans.bluetooth.SmartBleManager
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.contants.deviceId
import com.apemans.dmcomponent.contants.name
import com.apemans.dmcomponent.databinding.PairdeviceFragmentWaitingBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.ui.pairmethod.PairViewModel
import com.apemans.dmcomponent.utils.*
import com.dylanc.longan.logDebug
import com.dylanc.longan.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.callback.DataReceivedCallback

class ConnectBluetoothFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentWaitingBinding>() {

    private val viewModel: ConnectBluetoothViewModel by activityViewModels()
    private val pairViewModel: PairViewModel by activityViewModels()

    override fun onViewCreated(root: View) {
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        viewModel.wifiEncodeString.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val wifiCmd = convertBleLongCmd(it)
                repeat(wifiCmd.size) {
                    val text = "AT+S$it${wifiCmd[it]}\r"
                    logDebug(text)
                    SmartBleManager.core.send(text)
                    delay(200)
                }
                SmartBleManager.core.send("AT+SEND,${getTextByteSize(it)}\r")
            }
        }
        pairViewModel.exception.observe(viewLifecycleOwner) {
            toast(it.message)
        }
        SmartBleManager.core.observerBleMessageCallback(this, dataCallback)
    }

    private val dataCallback = DataReceivedCallback { device, data ->
        val message = data.getStringValue(0)
        logDebug(message)
        when {
            message?.startsWith("$BLE_CMD_DISTRIBUTE_NETWORK_SEND_RSP,$BLE_CMD_RSP_SUCCESS") == true -> {
                pairViewModel.scanPairedDevice().observe(viewLifecycleOwner) {
                    nextStep(Bundle().apply {
                        name = it.first().name
                        deviceId = it.first().uuid
                    })
                }
            }
            message?.startsWith("$BLE_CMD_OPEN_HOT_SPOT_RSP,$BLE_CMD_RSP_SUCCESS") == true -> {
//                ConnectingHotspotActivity.start(deviceName!!)
            }
        }
    }
}