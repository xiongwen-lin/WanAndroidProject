package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.base.utils.clickWithDuration
import com.apemans.usercomponent.R
import com.apemans.usercomponent.TermsServiceRepository
import com.apemans.usercomponent.databinding.MineActivitySetBinding
import com.apemans.usercomponent.viewModel.ClearCacheViewModel

class AppSettingsActivity : MineBaseActivity<MineActivitySetBinding>() {

    private lateinit var viewModel : ClearCacheViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
    }

    fun initView() {
        setToolbar {
            title = resources.getString(R.string.app_settings_title)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        binding.containerConditionsOfUse.clickWithDuration(500) {
            // 服务条款
            TermsServiceRepository.openTermsServiceWebSite(this)
        }
        binding.containerPrivacy.clickWithDuration(500) {
            // 隐私政策
            TermsServiceRepository.openPrivacyWebSite(this)
        }
        binding.containerClearCache.setOnClickListener {
            // 清除缓存
            SmartDialog.build(supportFragmentManager)
                .setTitle(resources.getString(R.string.app_settings_clear_cache))
                .setContentText(resources.getString(R.string.app_settings_clear_cache_tip))
                .setPositiveTextName(resources.getString(R.string.confirm))
                .setNegativeTextName(resources.getString(R.string.cancel))
                .setOnPositive {
                    viewModel.clearCache().observe(this@AppSettingsActivity){ isBoolean ->
                        if (isBoolean) {
                            toast(resources.getString(R.string.cache_cleared))
                            onResume()
                        }
                    }
                    it.dismiss()
                }.setOnNegative {
                    it.dismiss()
                }.show()
        }
        binding.containerAbout.setOnClickListener {
            // 关于
            AboutActivity.start(this)
        }
    }

    private fun initData() {
        viewModel = registerViewModule(ClearCacheViewModel::class.java)
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCacheSize().observe(this){
            binding.tvCacheSize.text = it
        }
    }

    companion object {
        fun start(from: Context) {
            from.startActivity<AppSettingsActivity>()
        }
    }
}