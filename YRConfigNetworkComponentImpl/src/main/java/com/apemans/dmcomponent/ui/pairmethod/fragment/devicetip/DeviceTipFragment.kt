package com.apemans.dmcomponent.ui.pairmethod.fragment.devicetip

import android.view.View
import androidx.annotation.DrawableRes
import coil.load
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.contants.KEY_DESCRIPTION
import com.apemans.dmcomponent.contants.KEY_ICON
import com.apemans.dmcomponent.databinding.PairdeviceFragmentDeviceTipBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.dylanc.longan.arguments
import com.dylanc.longan.doOnClick
import com.dylanc.longan.withArguments

class DeviceTipFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentDeviceTipBinding>() {
    private val icon: Int by arguments(KEY_ICON, 0)
    private val description: String? by arguments(KEY_DESCRIPTION)

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        binding.apply {
            ivImage.load(icon)
            tvDescription.text = description
            btnNext.doOnClick { nextStep() }
        }
    }

    companion object {
        fun newInstance(@DrawableRes icon: Int, description: String?) =
            DeviceTipFragment().withArguments(KEY_ICON to icon, KEY_DESCRIPTION to description)
    }
}