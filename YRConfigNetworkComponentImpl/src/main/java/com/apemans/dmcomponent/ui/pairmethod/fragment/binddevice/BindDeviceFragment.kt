package com.apemans.dmcomponent.ui.pairmethod.fragment.binddevice

import android.view.View
import androidx.fragment.app.activityViewModels
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.databinding.PairdeviceFragmentWaitingBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.dylanc.longan.finishWithResult
import com.dylanc.longan.toast

class BindDeviceFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentWaitingBinding>() {

    private val viewModel: BindDeviceViewModel by activityViewModels()

    override fun onViewCreated(root: View) {
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        viewModel.bindDevice().observe(viewLifecycleOwner) {
            if (it.code == 1102) {
                toast("设备已被绑定")
                requireActivity().finishWithResult()
            } else {
                nextStep()
            }
        }
    }
}