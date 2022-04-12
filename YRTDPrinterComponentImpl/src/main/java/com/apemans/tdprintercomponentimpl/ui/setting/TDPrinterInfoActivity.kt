package com.apemans.tdprintercomponentimpl.ui.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels

import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar

import com.apemans.quickui.alerter.alertInfo
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityTdPrinterInfoBinding
import com.apemans.tdprintercomponentimpl.ui.setting.bean.TDPrinterInfoUIState
import com.apemans.tdprintercomponentimpl.ui.setting.vm.TDPrinterInfoViewModel
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import java.lang.StringBuilder

class TDPrinterInfoActivity : BaseComponentActivity<TdActivityTdPrinterInfoBinding>() {

    private val viewModel: TDPrinterInfoViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            title = "Equipment information"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDeviceInfo(deviceIdExtra.orEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {

        with(binding) {
            tdPrinterInfoName.setupConfigure(lifecycleOwner)
            tdPrinterInfoOwner.setupConfigure(lifecycleOwner)
            tdPrinterInfoModel.setupConfigure(lifecycleOwner)
            tdPrinterInfoId.setupConfigure(lifecycleOwner)
            tdPrinterInfoVersion.setupConfigure(lifecycleOwner)
            tdPrinterInfoSsid.setupConfigure(lifecycleOwner)
            tdPrinterInfoIp.setupConfigure(lifecycleOwner)
            tdPrinterInfoMac.setupConfigure(lifecycleOwner)

            tdPrinterInfoName.updateUiState {
                it?.apply {
                    text = "Name"
                    subText = ""
                    rightIconVisibility = View.VISIBLE
                }
            }
            tdPrinterInfoOwner.updateUiState {
                it?.apply {
                    text = "Owner"
                    subText = ""
                }
            }
            tdPrinterInfoModel.updateUiState {
                it?.apply {
                    text = "Model"
                    subText = ""
                }
            }
            tdPrinterInfoId.updateUiState {
                it?.apply {
                    text = "DeviceId"
                    subText = deviceIdExtra.orEmpty()
                    rightIconRes = R.drawable.td_printer_test
                    rightIconVisibility = View.VISIBLE
                }
            }
            tdPrinterInfoVersion.updateUiState {
                it?.apply {
                    text = "Firmware"
                    subText = ""
                }
            }
            tdPrinterInfoSsid.updateUiState {
                it?.apply {
                    text = "SSID"
                    subText = ""
                }
            }
            tdPrinterInfoIp.updateUiState {
                it?.apply {
                    text = "IP Address"
                    subText = ""
                }
            }
            tdPrinterInfoMac.updateUiState {
                it?.apply {
                    text = "MAC"
                    subText = ""
                    dividerLineVisible = View.GONE
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            binding.tdPrinterInfoName,
            binding.tdPrinterInfoId,
            binding.tdPrinterInfoVersion,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.tdPrinterInfoName -> {
                /*
                startActivity<TDPrinterInfoActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra)
                )

                 */
            }
            R.id.tdPrinterInfoId -> {
                copyDeviceId(deviceIdExtra)
            }
            R.id.tdPrinterInfoVersion -> {
                /*
                val upgradeEnable = true
                if (upgradeEnable) {
                    startActivity<IpcUpgradeActivity>(
                        Pair(INTENT_KEY_DEVICE_ID, getDeviceId()),
                        Pair(INTENT_KEY_MODEL, viewModel.uiState.value?.ipcDeviceInfo?.model.orEmpty())
                    )
                }

                 */
            }
        }
    }

    private fun refreshUiState(uiState: TDPrinterInfoUIState?) {

        with(binding) {
            tdPrinterInfoName.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.name.orEmpty()
                }
            }

            tdPrinterInfoOwner.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.nickname.orEmpty()
                }
            }

            tdPrinterInfoModel.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.type.orEmpty()
                }
            }

            tdPrinterInfoId.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.uuid.orEmpty()
                }
            }

            tdPrinterInfoVersion.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.version.orEmpty()
                }
            }

            tdPrinterInfoSsid.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.name.orEmpty()
                }
            }

            tdPrinterInfoIp.updateUiState {
                it?.apply {
                    subText = bigNumToIP(uiState?.deviceInfo?.local_ip ?:0)//IPv4IntTransformer.bigNumToIP(uiState?.ipcDeviceInfo?.localIp ?: 0)
                }
            }

            tdPrinterInfoMac.updateUiState {
                it?.apply {
                    subText = uiState?.deviceInfo?.mac.orEmpty()
                }
            }
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
            alertInfo { "copy success" }
        } catch (e : Exception) {
        }
    }

    private fun bigNumToIP(ip: Long): String {
        val sb = StringBuilder()
        for (i in 0..3) {
            sb.append(ip ushr i * 8 and 255L)
            if (i != 3) {
                sb.append('.')
            }
        }
        return sb.toString()
    }

}