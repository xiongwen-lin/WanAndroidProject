package com.apemans.tuya.component.ui.addshareuser

import android.content.Intent
import android.os.Bundle
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.router.startRouterActivityForResult
import com.apemans.userapi.paths.KEY_COUNTRY_CODE
import com.apemans.userapi.paths.KEY_COUNTRY_NAME
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_DEVICE_ID
import com.apemans.tuya.component.databinding.TuyaActivityAddOwnerBinding
import com.apemans.userapi.paths.ACTIVITY_PATH_SELECT_COUNTRY

/**
 * @author Dylan Cai
 */
class AddShareUserActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityAddOwnerBinding>() {

    private val viewModel: AddShareUserViewModel by requestViewModels()
    private val homeId: Long by safeIntentExtras(KEY_HOME_ID)
    private val deviceId: String by safeIntentExtras(KEY_DEVICE_ID)
    private var countryCode: String? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.dev_sharing)
        with(binding) {
            layoutSelectCountry.tvCountry.text = "中国"
            countryCode = "86"
            btnDone.enableWhenOtherTextNotEmpty(edtName, edtEmail, edtConfirmEmail)
            btnDone.doOnClick {
                if (!edtEmail.textString.isEmail()) {
                    toast(R.string.email_address_is_not_valid)
                } else if (edtEmail.textString != edtConfirmEmail.textString) {
                    toast(R.string.email_not_match)
                } else {
                    viewModel.shareDevice(homeId, edtEmail.textString, countryCode!!, deviceId)
                        .observe(lifecycleOwner) {
                            finish()
                        }
                }
            }
            if (isAppDebug) {
                edtName.setText("蛮羊")
                edtEmail.setText("xumingyang@apemans.com")
                edtConfirmEmail.setText("xumingyang@apemans.com")
            }
            layoutSelectCountry.root.doOnClick {
                startRouterActivityForResult(ACTIVITY_PATH_SELECT_COUNTRY, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && requestCode == 0) {
            countryCode = data.getIntExtra(KEY_COUNTRY_CODE, -1).toString()
            binding.layoutSelectCountry.tvCountry.text = data.getStringExtra(KEY_COUNTRY_NAME)
        }
    }

    companion object {
        private const val KEY_HOME_ID = "home_id"

        fun start(homeId: Long, deviceId: String) {
            startActivity<AddShareUserActivity>(KEY_HOME_ID to homeId, KEY_DEVICE_ID to deviceId)
        }
    }
}