package com.apemans.dmcomponent.ui.pairmethod.fragment.scanqrcode

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import com.apemans.base.utils.FlashlightUtils
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.contants.result
import com.apemans.dmcomponent.databinding.PairdeviceFragmentScanQrCodeBinding
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.lastStep
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.dmcomponent.ui.pairmethod.fragment.scanqrcode.zxing.CaptureFragment
import com.apemans.dmcomponent.ui.pairmethod.fragment.scanqrcode.zxing.CodeUtils
import com.dylanc.longan.doOnClick
import com.dylanc.longan.toast

class ScanQrCodeFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentScanQrCodeBinding>() {

    private val permissionLauncher = registerForActivityResult(RequestPermission()) { takeSuccess ->
        if (!takeSuccess) {
            lastStep()
        }
    }

    override fun onViewCreated(root: View) {
        listOf(binding.ivFlashlight, binding.tvFlashlight).doOnClick {
            FlashlightUtils.setFlashlightStatus(!FlashlightUtils.isFlashlightOn)
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        val captureFragment = CaptureFragment()
        captureFragment.analyzeCallback = AnalyzeCallback()
        parentFragmentManager.beginTransaction().replace(R.id.zxing_container, captureFragment).commit()
        captureFragment.setCameraInitCallBack { e ->
            if (e != null) {
                Log.e("TAG", "callBack: ", e)
            }
        }
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroyView() {
        FlashlightUtils.destroy()
        super.onDestroyView()
    }

    private inner class AnalyzeCallback : CodeUtils.AnalyzeCallback {
        override fun onAnalyzeSuccess(mBitmap: Bitmap?, result: String?) {
            nextStep(Bundle().apply { this.result = result })
        }

        override fun onAnalyzeFailed() {
            toast("scan failed")
            lastStep()
        }
    }
}