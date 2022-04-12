package com.apemans.dmcomponent.ui.pairmethod.fragment.waitingonline

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.contants.deviceId
import com.apemans.dmcomponent.contants.name
import com.apemans.dmcomponent.databinding.PairdeviceFragmentWaitingBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.ui.pairmethod.PairViewModel
import com.apemans.yruibusiness.base.requestViewModels
import com.dylanc.longan.toast

class WaitingOnlineFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentWaitingBinding>() {

    private val viewModel: PairViewModel by viewModels()

    override fun onViewCreated(root: View) {
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        viewModel.scanPairedDevice().observe(viewLifecycleOwner) {
            nextStep(Bundle().apply {
                name = it.first().name
                deviceId = it.first().uuid
            })
        }
        viewModel.exception.observe(viewLifecycleOwner){
            toast(it.message)
        }
    }
}