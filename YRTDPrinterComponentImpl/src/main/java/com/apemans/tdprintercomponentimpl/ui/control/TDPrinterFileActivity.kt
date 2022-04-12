package com.apemans.tdprintercomponentimpl.ui.control

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.logger.YRLog
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityPrinterFileBinding
import com.apemans.tdprintercomponentimpl.router.ACTIVITY_PATH_TD_PRINTER_MANAGER_FILE
import com.apemans.tdprintercomponentimpl.ui.control.bean.TDPrinterWorkstationUIState
import com.apemans.tdprintercomponentimpl.ui.control.vm.TDPrinterWorkstationViewModel
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner

@Route(path = ACTIVITY_PATH_TD_PRINTER_MANAGER_FILE)
class TDPrinterFileActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TdActivityPrinterFileBinding>() {

    private val viewModel: TDPrinterWorkstationViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Select the file"
            rightIcon(R.drawable.td_ic_3dprint_file) {
                val intent = Intent()
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(intent, 100)
            }
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
            binding.tvSDCard,
            binding.tvUDisk,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when (id) {
            R.id.tvSDCard -> {
                binding.vSDCard.visibility = View.VISIBLE
                binding.vUDisk.visibility = View.INVISIBLE
            }
            R.id.tvUDisk -> {
                binding.vSDCard.visibility = View.INVISIBLE
                binding.vUDisk.visibility = View.VISIBLE
            }
        }
    }

    private fun refreshUiState(uiState: TDPrinterWorkstationUIState?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100->if (resultCode== RESULT_OK){
                if (data==null||data.data==null){
                    return
                }
                YRLog.d { "----> data="+data.data }
            }
        }
    }
}