package com.apemans.tuya.component.ui.savegroup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.tuya.module.api.ACTIVITY_PATH_SAVE_GROUP
import com.dylanc.longan.applicationViewModels
import com.dylanc.longan.intentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityAddHomeBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.dylanc.longan.textString

@Route(path = ACTIVITY_PATH_SAVE_GROUP)
class SaveGroupActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityAddHomeBinding>() {

    private val viewModel: SaveGroupViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by applicationViewModels()
    private val productId: String? by intentExtras(PRODUCE_ID)
    private val deviceIds: Array<String>? by intentExtras(DEVICE_IDS)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            titleRes = R.string.create_group
            rightIcon(R.drawable.device_nav_confirm_on, ::onRightBtnClick)
        }
    }

    private fun onRightBtnClick(v: View) {
        if (productId != null && deviceIds != null)
            viewModel.createGroup(productId!!, binding.edtName.textString, deviceIds!!.toList())
                .observe(this) {
                    sharedViewModel.saveGroupEvent.tryEmit(Unit)
                    finish()
                }
    }

    companion object {
        private const val PRODUCE_ID = "product_id"
        private const val DEVICE_IDS = "device_ids"

        fun start(productId: String, deviceIds: List<String>) {
            startActivity<SaveGroupActivity>(
                PRODUCE_ID to productId,
                DEVICE_IDS to deviceIds.toTypedArray()
            )
        }
    }
}