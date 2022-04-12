package com.apemans.tdprintercomponentimpl.ui.control

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route

import com.apemans.logger.YRLog

import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.tdprintercomponentimpl.R
import com.apemans.tdprintercomponentimpl.constant.INTENT_KEY_DEVICE_ID
import com.apemans.tdprintercomponentimpl.databinding.TdActivityPrinterCamBinding
import com.apemans.tdprintercomponentimpl.router.ACTIVITY_PATH_TD_PRINTER_MANAGER_CONTROL
import com.apemans.tdprintercomponentimpl.ui.control.bean.TDPrinterWorkstationUIState
import com.apemans.tdprintercomponentimpl.ui.control.vm.TDPrinterWorkstationViewModel
import com.apemans.tdprintercomponentimpl.ui.setting.TDPrinterSettingActivity
import com.dylanc.longan.intentExtras
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.startActivity
import java.util.*

@Route(path = ACTIVITY_PATH_TD_PRINTER_MANAGER_CONTROL)
class TDPrinterWorkstationActivity : BaseComponentActivity<TdActivityPrinterCamBinding>() {

    private val viewModel: TDPrinterWorkstationViewModel by viewModels()
    private val deviceIdExtra: String? by intentExtras(INTENT_KEY_DEVICE_ID)
    private lateinit var translateAniShow: TranslateAnimation
    private lateinit var translateAniHide: TranslateAnimation

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar() {
            title = "3D printer"
            rightIcon(R.drawable.td_ic_nav_setting){
                startActivity<TDPrinterSettingActivity>(Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra))
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

        translateAnimation()

        registerOnViewClick(
            binding.vContainer,
            binding.containerControl,
            binding.containerConsole,
            binding.clSprinkler,
            binding.clBed,
            binding.clFan,
            binding.clSleep,
            binding.clSpeed,
            binding.tvSprinkler,
            binding.tvBed,
            binding.tvFan,
            binding.tvSleep,
            binding.tvSpeed,
            binding.containerConsoleTitle,
            binding.vSelectedFile,
            binding.btnShow,
            binding.btnHide,
            block = ::handleOnViewClick
        )
    }

    private fun translateAnimation() {
        //向上位移显示动画  从自身位置的最下端向上滑动了自身的高度
        translateAniShow = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
            0f,//fromXValue表示开始的X轴位置
            Animation.RELATIVE_TO_SELF,
            0f,//fromXValue表示结束的X轴位置
            Animation.RELATIVE_TO_SELF,
            1f,//fromXValue表示开始的Y轴位置
            Animation.RELATIVE_TO_SELF,
            0f
        )//fromXValue表示结束的Y轴位置
        translateAniShow.repeatMode = Animation.REVERSE;
        translateAniShow.duration = 1000;

        //向下位移隐藏动画  从自身位置的最上端向下滑动了自身的高度
        translateAniHide = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
            0f,//fromXValue表示开始的X轴位置
            Animation.RELATIVE_TO_SELF,
            0f,//fromXValue表示结束的X轴位置
            Animation.RELATIVE_TO_SELF,
            0f,//fromXValue表示开始的Y轴位置
            Animation.RELATIVE_TO_SELF,
            1f
        );//fromXValue表示结束的Y轴位置
        translateAniHide.repeatMode = Animation.REVERSE;
        translateAniHide.duration = 1000;
    }

    private fun handleOnViewClick(id: Int?) {
        when (id) {
            R.id.containerConsoleTitle -> {
                binding.containerConsoleTitle.visibility = View.GONE
                binding.containerConsole.startAnimation(translateAniShow)
                binding.containerConsole.visibility = View.VISIBLE
            }
            R.id.vContainer -> {
                binding.containerConsoleTitle.visibility = View.VISIBLE
                binding.containerConsole.startAnimation(translateAniHide)
                binding.containerConsole.visibility = View.GONE
            }
            R.id.vSelectedFile -> {
                startActivity<TDPrinterFileActivity>(Pair(INTENT_KEY_DEVICE_ID, deviceIdExtra))
            }
            R.id.clSprinkler->{
                SmartDialog.build(supportFragmentManager)
                    .showEditBox(show = true, showKeyboard = true, "Set Params")
                    .setPositiveTextName("Set")
                    .setOnPositive {
                        val text = it.getEditBox().getText()
                        binding.tvSprinkler.text=text
                        it.dismiss()
                    }
                    .show()
            }
            R.id.clBed->{
                SmartDialog.build(supportFragmentManager)
                    .showEditBox(show = true, showKeyboard = true, "Set Params")
                    .setPositiveTextName("Set")
                    .setOnPositive {
                        val text = it.getEditBox().getText()
                        binding.tvBed.text=text
                        it.dismiss()
                    }
                    .show()
            }
            R.id.clFan->{
                SmartDialog.build(supportFragmentManager)
                    .showEditBox(show = true, showKeyboard = true, "Set Params")
                    .setPositiveTextName("Set")
                    .setOnPositive {
                        val text = it.getEditBox().getText()
                        binding.tvFan.text=text
                        it.dismiss()
                    }
                    .show()
            }
            R.id.clSleep->{
                SmartDialog.build(supportFragmentManager)
                    .showEditBox(show = true, showKeyboard = true, "Set Params")
                    .setPositiveTextName("Set")
                    .setOnPositive {
                        val text = it.getEditBox().getText()
                        binding.tvSleep.text=text
                        it.dismiss()
                    }
                    .show()
            }
            R.id.clSpeed->{
                SmartDialog.build(supportFragmentManager)
                    .showEditBox(show = true, showKeyboard = true, "Set Params")
                    .setPositiveTextName("Set")
                    .setOnPositive {
                        val text = it.getEditBox().getText()
                        binding.tvSpeed.text=text
                        it.dismiss()
                    }
                    .show()
            }
            R.id.btnShow->{
                binding.containerConsole.visibility=View.VISIBLE
                binding.containerSelectFile.visibility=View.GONE
            }
            R.id.btnHide->{
                binding.containerSelectFile.visibility=View.VISIBLE
                binding.containerConsole.visibility=View.GONE
            }
        }
    }

    private fun refreshUiState(uiState: TDPrinterWorkstationUIState?) {
    }

}