package com.apemans.tuya.component.ui.homesetting

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_HOME_ID
import com.apemans.tuya.component.databinding.TuyaActivityHomeSettingBinding
import com.apemans.tuya.component.ui.SharedViewModel
import com.apemans.tuya.component.ui.inputtext.InputTextContract
import com.apemans.tuya.component.ui.inputtext.InputTextRequest
import com.tuya.smart.home.sdk.TuyaHomeSdk
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 */
class HomeSettingActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityHomeSettingBinding>() {

    private val viewModel: HomeSettingViewModel by requestViewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()
    private val homeId: Long by safeIntentExtras(KEY_HOME_ID)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.home_settings)
        with(binding) {
            val homeName = TuyaHomeSdk.getDataInstance().getHomeBean(homeId)?.name
            tvHomeName.text = homeName
            btnHomeName.doOnClick {
                inputText.launch(
                    InputTextRequest(
                        name = getString(R.string.name),
                        title = getString(R.string.name_your_home),
                        value = homeName
                    )
                )
            }
            btnHomeRemove.doOnClick {
                viewModel.removeHome(homeId).observe(lifecycleOwner) {
                    finish()
                    sharedViewModel.removeHomeEvent.tryEmit(Unit)
                }
            }
        }
    }

    private val inputText = registerForActivityResult(InputTextContract()) { value ->
        if (value != null) {
            viewModel.updateHome(homeId, value).observe(this) {
                binding.tvHomeName.text = value
                lifecycleScope.launch {
                    sharedViewModel.renameHomeEvent.emit(value)
                }
            }
        }
    }

    companion object {
        fun start(homeId: Long) {
            startActivity<HomeSettingActivity>(KEY_HOME_ID to homeId)
        }
    }
}