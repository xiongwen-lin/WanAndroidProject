package com.apemans.tdprintercomponentimpl.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityTdPrinterSettingBinding
import com.apemans.tdprintercomponentimpl.ui.setting.bean.TDPrinterSettingUIState
import com.apemans.tdprintercomponentimpl.ui.setting.vm.TDPrinterSettingViewModel
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity

class TplActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TdActivityTdPrinterSettingBinding>() {

    private val viewModel: TDPrinterSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "3DPrinter Settings"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            binding.tdPrinterSettingInfo,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.tdPrinterSettingInfo -> {
                startActivity<TDPrinterInfoActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra)
                )
            }
        }
    }

    private fun refreshUiState(uiState: TDPrinterSettingUIState?) {
    }

}