package com.apemans.dmcomponent.ui.pairmethod.fragment.createqrcode

import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.databinding.PairdeviceFragmentCreateQrcodeBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.utils.toQRCodeBitmap
import com.dylanc.longan.doOnClick
import com.dylanc.longan.dp

class CreateQrCodeFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentCreateQrcodeBinding>() {

    private val viewModel: CreateQrCodeViewModel by activityViewModels()

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        binding.btnNext.doOnClick { nextStep() }
        viewModel.wifiEncodeString.observe(viewLifecycleOwner) {
            val bitmap = it.toQRCodeBitmap(300.dp.toInt(), 300.dp.toInt())
            binding.ivQrcode.load(bitmap)
        }
    }
}