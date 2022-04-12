package com.apemans.dmcomponent.ui.pairmethod

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmcomponent.databinding.DeviceLayoutPairMethodBinding
import com.dylanc.longan.TAG
import com.dylanc.longan.design.FragmentStateAdapter
import com.dylanc.longan.doOnClick
import com.dylanc.longan.finishWithResult
import com.dylanc.longan.isLightStatusBar

abstract class BasePairActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceLayoutPairMethodBinding>() {

    private var currentItem = 0
    private val fragmentConfig = mutableListOf<FragmentConfig>()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initStepFragments()
        isLightStatusBar = true
        binding.apply {
            viewPager2.isUserInputEnabled = false
            val fragmentList = fragmentConfig.map { it.fragment }.toTypedArray()
            viewPager2.adapter = FragmentStateAdapter(*fragmentList)
            viewPager2.offscreenPageLimit = fragmentConfig.size
            btnBack.doOnClick {
                if (currentItem > 0) {
                    viewPager2.currentItem = --currentItem
                } else {
                    finish()
                }
            }
            btnClose.doOnClick { finish() }
        }
        fragmentConfig.forEachIndexed { _, config ->
            supportFragmentManager.setFragmentResultListener(
                config.fragment.TAG,
                this
            ) { _, result ->
                config.onFragmentResult?.invoke(result)
                if (currentItem < fragmentConfig.size - 1) {
                    nextPage()
                } else {
                    finishWithResult()
                }
            }
        }
        supportFragmentManager.setFragmentResultListener(TAG_LAST_STEP, this) { _, _ ->
            lastPage()
        }
    }

    abstract fun initStepFragments()

    protected fun addStepFragment(fragment: Fragment, onFragmentResult: ((Bundle) -> Unit)? = null) {
        fragmentConfig.add(FragmentConfig(fragment, onFragmentResult))
    }

    override fun onBackPressed() {
        if (currentItem > 0) {
            binding.viewPager2.currentItem = --currentItem
        } else {
            super.onBackPressed()
        }
    }

    private fun nextPage() {
        binding.viewPager2.currentItem = ++currentItem
    }

    private fun lastPage() {
        binding.viewPager2.currentItem = --currentItem
    }

    private class FragmentConfig(
        val fragment: Fragment,
        val onFragmentResult: ((Bundle) -> Unit)?
    )

    companion object {
        private const val TAG_LAST_STEP = "last_step"

        fun Fragment.nextStep(bundle: Bundle = bundleOf()) {
            setFragmentResult(TAG, bundle)
        }

        fun Fragment.lastStep(bundle: Bundle = bundleOf()) {
            setFragmentResult(TAG_LAST_STEP, bundle)
        }
    }
}