package com.apemans.tuya.component.ui.memberaccess

import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.longan.safeIntentExtras
import com.dylanc.longan.startActivity
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.KEY_EMAIL
import com.apemans.tuya.component.constants.KEY_MEMBER_ID
import com.apemans.tuya.component.constants.KEY_URL
import com.apemans.tuya.component.databinding.TuyaActivityMemberAccessBinding

/**
 * @author Dylan Cai
 */
class MemberAccessActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityMemberAccessBinding>() {
    private val memberId: Long by safeIntentExtras(KEY_MEMBER_ID)
    private val email: String by safeIntentExtras(KEY_EMAIL)
    private val iconUrl: String by safeIntentExtras(KEY_URL)
    private val adapter = MultiTypeAdapter()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            titleRes = R.string.shared_access
            rightIcon(R.drawable.device_home_add) {

            }
        }
        with(binding) {
            tvEmail.text = email
            ivPortrait.load(iconUrl) {
                transformations(CircleCropTransformation())
            }
            recyclerView.adapter = adapter
        }
    }

    companion object {

        fun start(memberId: Long, email: String, iconUrl: String) =
            startActivity<MemberAccessActivity>(
                KEY_MEMBER_ID to memberId,
                KEY_EMAIL to email,
                KEY_URL to iconUrl
            )
    }
}