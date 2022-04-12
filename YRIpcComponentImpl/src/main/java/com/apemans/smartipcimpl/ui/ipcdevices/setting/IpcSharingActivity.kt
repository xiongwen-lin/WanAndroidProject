package com.apemans.smartipcimpl.ui.ipcdevices.setting

import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.base.utils.addTextChangedListener
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcapi.webapi.DeviceRelation
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.databinding.DeviceActivityIpcSharingBinding
import com.apemans.smartipcimpl.ui.ipcdevices.setting.item.IpcBinderViewDelegate
import com.apemans.smartipcimpl.ui.ipcdevices.setting.vm.IpcSettingViewModel
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.constants.INTENT_KEY_DEVICE_ID
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class IpcSharingActivity: com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityIpcSharingBinding>() {

    private val viewModel: IpcSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private val deviceIpcBinderAdapter = MultiTypeAdapter(IpcBinderViewDelegate(binderItemClickListener))

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "Sharing"
        }
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDeviceRelation(getDeviceId())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupUI() {
        with(binding) {
            deviceIpcSharingTipContent.text = String.format("Invite friends and relatives to share your camera：%1\$s ", getDeviceId())
            deviceIpcSharingTipMore.text = getString(R.string.camera_share_title).plus(" >>")
            deviceIpcSharingSend.isEnabled = false

            deviceIpcSharerRv.adapter = deviceIpcBinderAdapter
        }

        binding.deviceIpcSharer.getEditText().setOnFocusChangeListener { v, hasFocus ->
            YRLog.d { "-->> SmartEditBox focus 0 $hasFocus" }
        }

        binding.deviceIpcSharer.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        binding.deviceIpcSharer1.getEditText().setOnFocusChangeListener { v, hasFocus ->
            YRLog.d { "-->> SmartEditBox focus 1 $hasFocus" }
        }

        binding.deviceIpcSharer1.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        binding.deviceIpcSharer2.getEditText().setOnFocusChangeListener { v, hasFocus ->
            YRLog.d { "-->> SmartEditBox focus 2 $hasFocus" }
        }

        binding.deviceIpcSharer2.getEditText().addTextChangedListener(afterTextChanged = checkAfterInputTextChange)

        viewModel.deviceRelationState.value = listOf()
        deviceIpcBinderAdapter.observeItemsChanged(lifecycleOwner, viewModel.deviceRelationState) { oldItem, newItem ->
            oldItem.account == newItem.account
        }

        registerOnViewClick(
            binding.deviceIpcSharingTipMore,
            binding.deviceIpcSharingSend,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when(id) {
            R.id.deviceIpcSharingTipMore -> {}
            R.id.deviceIpcSharingSend -> {
                val shares = mutableListOf<String>()
                if (binding.deviceIpcSharer.getEditText().text.isNotEmpty()) {
                    shares.add(binding.deviceIpcSharer.getEditText().text.toString())
                }
                if (binding.deviceIpcSharer1.getEditText().text.isNotEmpty()) {
                    shares.add(binding.deviceIpcSharer1.getEditText().text.toString())
                }
                if (binding.deviceIpcSharer2.getEditText().text.isNotEmpty()) {
                    shares.add(binding.deviceIpcSharer2.getEditText().text.toString())
                }
                if (shares.isNotEmpty()) {
                    viewModel.startShareDevice(getDeviceId(), shares)
                }
            }
            else -> {}
        }
    }

    private val checkAfterInputTextChange: (Editable?) -> Unit = {
        var sharingEnable = binding.let {
            it.deviceIpcSharer.getEditText().text.isNotEmpty() || it.deviceIpcSharer1.getEditText().text.isNotEmpty() || it.deviceIpcSharer2.getEditText().text.isNotEmpty()
        }
        binding.deviceIpcSharingSend.isEnabled = sharingEnable
    }

    private val binderItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<DeviceRelation> { item, position ->
        var isSharer: Boolean = item?.type != DeviceDefine.BIND_TYPE_OWNER
        if (isSharer) {
            showDeleteShareDialog(getDeviceId(), item.id, getDeviceId())
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

    private fun getDeviceId() : String {
        return deviceIdExtra.orEmpty()
    }
}