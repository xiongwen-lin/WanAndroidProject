package com.apemans.tdprintercomponentimpl.ui.setting

import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import com.apemans.base.utils.addTextChangedListener
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.logger.YRLog
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.API_BIND_TYPE_OWNER
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityTdPrinterSettingBinding
import com.apemans.tdprintercomponentimpl.databinding.TdActivityTdPrinterSharingBinding
import com.apemans.tdprintercomponentimpl.ui.setting.item.TDPrinterBinderViewDelegate
import com.apemans.tdprintercomponentimpl.ui.setting.vm.TDPrinterSettingViewModel
import com.apemans.tdprintercomponentimpl.ui.setting.vm.TDPrinterSharingViewModel
import com.apemans.tdprintercomponentimpl.webapi.DeviceRelation
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner

class TDPrinterSharingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TdActivityTdPrinterSharingBinding>() {

    private val viewModel: TDPrinterSharingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val deviceIpcBinderAdapter = MultiTypeAdapter(TDPrinterBinderViewDelegate(binderItemClickListener))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "3DPrinter Sharing"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDeviceRelation(deviceIdExtra.orEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            tdPrinterSharingTipTitle.text = getString(R.string.td_td_printer_title)
            tdPrinterSharingSend.isEnabled = false

            tdPrinterSharerRv.adapter = deviceIpcBinderAdapter
        }

        binding.tdPrinterSharer.getEditText().setOnFocusChangeListener { v, hasFocus ->
            YRLog.d { "-->> SmartEditBox focus 0 $hasFocus" }
        }

        binding.tdPrinterSharer.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        viewModel.deviceRelationState.value = listOf()
        deviceIpcBinderAdapter.observeItemsChanged(lifecycleOwner, viewModel.deviceRelationState) { oldItem, newItem ->
            oldItem.account == newItem.account
        }

        registerOnViewClick(
            binding.tdPrinterSharingSend,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.tdPrinterSharingSend -> {
                val shares = mutableListOf<String>()
                if (binding.tdPrinterSharer.getEditText().text.isNotEmpty()) {
                    shares.add(binding.tdPrinterSharer.getEditText().text.toString())
                }
                if (shares.isNotEmpty()) {
                    viewModel.startShareDevice(deviceIdExtra.orEmpty(), shares)
                }
            }
            else -> {}
        }
    }

    private val checkAfterInputTextChange: (Editable?) -> Unit = {
        var sharingEnable = binding.let {
            it.tdPrinterSharer.getEditText().text.isNotEmpty()
        }
        binding.tdPrinterSharingSend.isEnabled = sharingEnable
    }

    private val binderItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceRelation> { item, position ->
        var isSharer: Boolean = item?.type != API_BIND_TYPE_OWNER
        if (isSharer) {
            showDeleteShareDialog(deviceIdExtra.orEmpty(), item.id, deviceIdExtra.orEmpty())
        }
    }

    private fun showDeleteShareDialog(deviceId: String, id: Int, name: String) {
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.camera_share_title))
            .setContentText(String.format("Are you sure to stop sharing %1\$s with heimao@hotmail.com?", name))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                viewModel.startRemoveShares(deviceId, id)
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

}