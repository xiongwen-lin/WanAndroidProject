package com.apemans.tdprintercomponentimpl.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.PopupWindow
import androidx.activity.viewModels
import com.apemans.logger.YRLog
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityTdPrinterSettingBinding
import com.apemans.tdprintercomponentimpl.ui.setting.bean.TDPrinterSettingUIState
import com.apemans.tdprintercomponentimpl.ui.setting.vm.TDPrinterSettingViewModel
import com.apemans.tdprintercomponentimpl.utils.MediaPopupWindows
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import java.lang.Exception
import java.lang.StringBuilder

class TDPrinterSettingActivity : BaseComponentActivity<TdActivityTdPrinterSettingBinding>() {

    private val viewModel: TDPrinterSettingViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private var mPopMenus: MediaPopupWindows? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
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
        with(binding) {
            tdPrinterSettingInfo.setupConfigure(lifecycleOwner)
            tdPrinterSettingSharing.setupConfigure(lifecycleOwner)
            tdPrinterSettingRecord.setupConfigure(lifecycleOwner)
            tdPrinterSettingManagerFile.setupConfigure(lifecycleOwner)
            tdPrinterSettingPushing.setupConfigure(lifecycleOwner)
            tdPrinterSettingEnergy.setupConfigure(lifecycleOwner)
            tdPrinterSettingLed.setupConfigure(lifecycleOwner)
            tdPrinterSettingTemperature.setupConfigure(lifecycleOwner)
            tdPrinterSettingOtherContractUs.setupConfigure(lifecycleOwner)

            tdPrinterSettingInfo.updateUiState {
                it?.apply {
                    text = "Equipment information"
                }
            }
            tdPrinterSettingSharing.updateUiState {
                it?.apply {
                    text = "Share equipment"

                }
            }
            tdPrinterSettingRecord.updateUiState {
                it?.apply {
                    text = "Printed out"
                }
            }
            tdPrinterSettingManagerFile.updateUiState {
                it?.apply {
                    text = "File management"
                }
            }
            tdPrinterSettingPushing.updateUiState {
                it?.apply {
                    text = "Push Settings"
                }
            }
            tdPrinterSettingEnergy.updateUiState {
                it?.apply {
                    text = "Energy Saving Settings"
                    subText = "An hour"
                }
            }
            tdPrinterSettingLed.updateUiState {
                it?.apply {
                    text = "Indicator setting"
                }
            }
            tdPrinterSettingTemperature.updateUiState {
                it?.apply {
                    text = "Temperature of the unit"
                }
            }
            tdPrinterSettingOtherContractUs.updateUiState {
                it?.apply {
                    text = "Contact us"
                }
            }
        }

        viewModel.uiState.observe(lifecycleOwner) {
            refreshUiState(it)
        }

        registerOnViewClick(
            binding.tdPrinterSettingInfo,
            binding.tdPrinterSettingSharing,
            binding.tdPrinterSettingOtherContractUs,
            block = ::handleOnViewClick
        )
    }

    private fun handleOnViewClick(id: Int?) {
        when (id) {
            R.id.tdPrinterSettingInfo -> {
                startActivity<TDPrinterInfoActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra)
                )
            }
            R.id.tdPrinterSettingSharing -> {
                startActivity<TDPrinterSharingActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra)
                )
            }
            R.id.tdPrinterSettingOtherContractUs -> {
                showPopMenu()
            }
        }
    }

    fun showPopMenu() {
        mPopMenus?.dismiss()

        mPopMenus = MediaPopupWindows(this, object : MediaPopupWindows.OnClickMediaListener {
            override fun onFaceBookClick() {
                try {
                    packageManager.getPackageInfo("com.facebook.katana", 0)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/4240701792613897")))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/fokoos")))
                }
            }

            override fun onTiktokClick() {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@fokoos3d")))
            }

            override fun onInstagramClick() {
                try {
                    packageManager.getPackageInfo("com.instagram.android", 0)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=fokoos_tech")))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/fokoos_tech/")))
                }
            }

            override fun onEmailClick() {
                val mailToSb = StringBuilder()
                mailToSb.append("mailto:")
                mailToSb.append("support.us@fokoos.com")
                val uri = Uri.parse(mailToSb.toString())
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(Intent.createChooser(intent, getString(R.string.about_select_email_application)))
            }

        })

        mPopMenus?.setOnDismissListener(PopupWindow.OnDismissListener { mPopMenus = null })

        mPopMenus?.showAtLocation(
            findViewById(R.id.containerDeviceSetting),
            Gravity.TOP or Gravity.BOTTOM, 0, 0
        )
    }

    private fun refreshUiState(uiState: TDPrinterSettingUIState?) {
    }

}