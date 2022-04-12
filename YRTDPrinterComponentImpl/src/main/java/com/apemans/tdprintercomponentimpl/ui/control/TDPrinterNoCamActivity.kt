package com.apemans.tdprintercomponentimpl.ui.control

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityPrinterNoCamBinding
import com.apemans.tdprintercomponentimpl.router.ACTIVITY_PATH_TD_PRINTER_MANAGER_CONTROL_NO_CAM
import com.apemans.tdprintercomponentimpl.ui.control.bean.TDPrinterWorkstationUIState
import com.apemans.tdprintercomponentimpl.ui.control.vm.TDPrinterWorkstationViewModel
import com.apemans.tdprintercomponentimpl.ui.setting.TDPrinterSettingActivity
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity

@Route(path = ACTIVITY_PATH_TD_PRINTER_MANAGER_CONTROL_NO_CAM)
class TDPrinterNoCamActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TdActivityPrinterNoCamBinding>() {

    private val viewModel: TDPrinterWorkstationViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "3D printer"
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
            //binding.tdPrinterWorkstationSetting,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.tdPrinterWorkstationSetting -> {
                startActivity<TDPrinterSettingActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra)
                )
            }
        }
    }

    private fun refreshUiState(uiState: TDPrinterWorkstationUIState?) {
    }

}