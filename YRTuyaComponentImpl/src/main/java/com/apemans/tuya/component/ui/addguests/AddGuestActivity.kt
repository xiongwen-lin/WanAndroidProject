package com.apemans.tuya.component.ui.addguests

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.router.startRouterActivityForResult
import com.apemans.userapi.paths.KEY_COUNTRY_CODE
import com.apemans.userapi.paths.KEY_COUNTRY_NAME
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.databinding.TuyaActivityAddGuestBinding
import com.apemans.tuya.component.ui.creategroup.CreateGroupContract
import com.apemans.userapi.paths.ACTIVITY_PATH_SELECT_COUNTRY

/**
 * @author Dylan Cai
 */
class AddGuestActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityAddGuestBinding>() {

    private val viewModel: AddGuestViewModel by requestViewModels()
    private val homeId: Long by safeIntentExtras(KEY_HOME_ID)
    private lateinit var uid: String
    private var countryCode: String? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.add_guest)
        with(binding) {
            tvAddMemberTip.isVisible = false
            layoutSelectCountry.tvCountry.text = "中国"
            btnDone.enableWhenOtherTextNotEmpty(edtEmail, edtConfirmEmail)
            btnDone.setOnClickListener {
                viewModel.queryUserShared(homeId, edtEmail.textString)
                    .observe(lifecycleOwner) {
                        if (it != null) {
                            uid = it
                            createGroup.launch(homeId)
                        } else {
                            toast(R.string.have_added_guest)
                        }
                    }
            }
            if (isAppDebug) {
                edtEmail.setText("xumingyang@apemans.com")
                edtConfirmEmail.setText("xumingyang@apemans.com")
            }
            layoutSelectCountry.root.doOnClick {
                startRouterActivityForResult(ACTIVITY_PATH_SELECT_COUNTRY, 0)
            }
        }
    }

    private val createGroup = registerForActivityResult(CreateGroupContract()) {
        if (it != null) {
            viewModel.addShareWithHomeId(homeId, "86", uid, it.deviceIds)
                .observe(this) {
                    finish()
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

        fun start(homeId: Long) {
            startActivity<AddGuestActivity>(KEY_HOME_ID to homeId)
        }
    }
}