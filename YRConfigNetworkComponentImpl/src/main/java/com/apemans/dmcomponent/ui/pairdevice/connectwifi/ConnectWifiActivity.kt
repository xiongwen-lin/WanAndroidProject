package com.apemans.dmcomponent.ui.pairdevice.connectwifi

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.apemans.bluetooth.SmartBleManager
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.contants.KEY_TYPE
import com.apemans.dmcomponent.databinding.DeviceFragmentConnectWifiBinding
import com.apemans.dmcomponent.db.KEY_DEVICE_NAME
import com.apemans.dmcomponent.ui.pairdevice.connectcables.ConnectCablesActivity
import com.apemans.dmcomponent.ui.pairdevice.connecting.ConnectingActivity
import com.apemans.dmcomponent.ui.pairdevice.connectinghotspot.ConnectingHotspotActivity
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.items.ConnectMethodItem
import com.apemans.dmcomponent.ui.pairdevice.connectwifi.items.ConnectMethodViewDelegate
import com.apemans.dmcomponent.ui.pairdevice.dvmode.waitflashing.DvWaitFlashingActivity
import com.apemans.dmcomponent.ui.pairdevice.waitflashing.WaitFlashingActivity
import com.apemans.dmcomponent.utils.*
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.dylanc.longan.*
import com.dylanc.longan.design.alert
import com.dylanc.longan.design.okButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.callback.DataReceivedCallback

/**
 * @author Dylan Cai
 */
class ConnectWifiActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceFragmentConnectWifiBinding>(),
    com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<String> {

    private val adapter = MultiTypeAdapter(ConnectMethodViewDelegate(this))
    private val type: Int by safeIntentExtras(KEY_TYPE)
    private val deviceName: String? by intentExtras(KEY_DEVICE_NAME)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar()
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        when (type) {
            DeviceType.CAMERA -> {
                binding.stepHeader.step = 1
                binding.btnNext.doOnClick {
                    WaitFlashingActivity.start(
                        binding.edtWifiName.textString,
                        binding.edtPwd.textString
                    )
                }
                adapter.items = listOf(
                    ConnectMethodItem(R.drawable.device_connect_cable, "Use network cable"),
                    ConnectMethodItem(
                        R.drawable.device_connect_wireless,
                        "DV mode (not using network)"
                    ),
                )
            }
            DeviceType.HUNTING_CAMERA -> {
                SmartBleManager.core.observerBleMessageCallback(this, dataCallback)

                binding.stepHeader.step = 3
                binding.btnNext.doOnClick {
                    lifecycleScope.launch {
                        val wifiEncode = wifiEncodeStringOf(
                            binding.edtWifiName.textString,
                            binding.edtPwd.textString,
                            "us"
                        )
                        val wifiCmd = convertBleLongCmd(wifiEncode)
                        repeat(wifiCmd.size) {
                            val text = "AT+S$it${wifiCmd[it]}\r"
                            logDebug(text)
                            SmartBleManager.core.send(text)
                            delay(200)
                        }
                        SmartBleManager.core.send("AT+SEND,${getTextByteSize(wifiEncode)}\r")
                    }
//                    startActivity<ConnectingHotspotActivity>()
                }
                adapter.items = listOf(
                    ConnectMethodItem(
                        R.drawable.device_connect_cable,
                        "DV mode (not using network)"
                    ),
                )
            }
        }

        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: String, position: Int) {
        when (item) {
            "Use network cable" -> {
                startActivity<ConnectCablesActivity>()
            }
            "DV mode (not using network)" -> {
                when (type) {
                    DeviceType.CAMERA -> {
                        alert(
                            "When you take the device or place it in a place without internet, you can use DV mode. Only MC120 and M1 support DV mode.",
                            "Please confirm that the device supports DV mode"
                        ) {
                            okButton {
                                startActivity<DvWaitFlashingActivity>()
                            }
                        }
                    }
                    DeviceType.HUNTING_CAMERA -> {
                        SmartBleManager.core.send(BLE_CMD_OPEN_HOT_SPOT)
//                        startActivity<ConnectingHotspotActivity>()
                    }
                }
            }
        }
    }

    private val dataCallback = DataReceivedCallback { device, data ->
        val message = data.getStringValue(0)
        logDebug(message)
        when {
            message?.startsWith("$BLE_CMD_DISTRIBUTE_NETWORK_SEND_RSP,$BLE_CMD_RSP_SUCCESS") == true -> {
                startActivity<ConnectingActivity>()
            }
            message?.startsWith("$BLE_CMD_OPEN_HOT_SPOT_RSP,$BLE_CMD_RSP_SUCCESS") == true -> {
                ConnectingHotspotActivity.start(deviceName!!)
            }
        }
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            binding.edtWifiName.setText(wifiSSID)
        },
        onDenied = {

        },
        onShowRequestRationale = {

        }
    )

    companion object {
        fun start(type: Int, bluetoothName: String? = null) =
            startActivity<ConnectWifiActivity>(KEY_TYPE to type, KEY_DEVICE_NAME to bluetoothName)
    }
}

object DeviceType {
    const val CAMERA = 1
    const val HUNTING_CAMERA = 2
}

