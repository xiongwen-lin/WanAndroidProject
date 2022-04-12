package com.apemans.tuya.component.ui.familymember

import android.os.Bundle
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.*
import com.apemans.tuya.component.R
import com.apemans.tuya.component.constants.*
import com.apemans.tuya.component.databinding.TuyaActivityFamilyMemberBinding
import com.apemans.tuya.component.ui.inputtext.InputTextContract
import com.apemans.tuya.component.ui.inputtext.InputTextRequest
import com.apemans.tuya.component.ui.memberaccess.MemberAccessActivity

/**
 * @author Dylan Cai
 */
class FamilyMemberActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityFamilyMemberBinding>() {

    private val viewModel: FamilyMemberViewModel by requestViewModels()
    private val memberId: Long by safeIntentExtras(KEY_MEMBER_ID)
    private val nickname: String by safeIntentExtras(KEY_NICKNAME)
    private val account: String by safeIntentExtras(KEY_ACCOUNT)
    private val iconUrl: String by safeIntentExtras(KEY_URL)
    private val isOwner: Boolean by safeIntentExtras(KEY_IS_OWNER)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar(R.string.my_profile)
        with(binding) {
            tvNickname.text = nickname
            tvMemberAccess.text = if (isOwner) {
                getString(R.string.owner)
            } else {
                getString(R.string.guest)
            }
            btnSharedDevices.isVisible = !isOwner
            ivPortrait.load(iconUrl) {
                transformations(CircleCropTransformation())
            }

            btnMemberRemove.doOnClick {
                if (isOwner) {
                    viewModel.removeMember(memberId)
                } else {
                    viewModel.removeUserShare(memberId)
                }.observe(lifecycleOwner) {
                    finish()
                }
            }
            btnNickname.doOnClick {
                inputText.launch(
                    InputTextRequest(getString(R.string.name), getString(R.string.account_setting))
                )
            }
            btnSharedDevices.doOnClick {
                MemberAccessActivity.start(memberId, tvAccount.textString, iconUrl)
            }

            viewModel.getAccount(account).observe(lifecycleOwner) {
                tvAccount.text = it
            }
            viewModel.getUserShareInfo(memberId).observe(lifecycleOwner) { detailBean ->
                if (detailBean.devices.size < 2) {
                    tvMemberSharedDevice.text = String.format(getString(R.string.device_singular), detailBean.devices.size)
                } else {
                    tvMemberSharedDevice.text = String.format(getString(R.string.device_plural), detailBean.devices.size)
                }
                if (detailBean.remarkName.isNotEmpty()) {
                    tvNickname.text = detailBean.remarkName
                } else if (detailBean.nameWithoutRemark != null) {
                    tvNickname.text = detailBean.nameWithoutRemark
                }
            }
        }
    }

    private val inputText = registerForActivityResult(InputTextContract()) { value ->
        if (value != null) {
            viewModel.renameShareNickname(memberId, value).observe(this) {
                binding.tvNickname.text = value
            }
        }
    }

    companion object {

        fun start(memberId: Long, nickname: String, account: String, iconUrl: String, isOwner: Boolean) {
            startActivity<FamilyMemberActivity>(
                KEY_MEMBER_ID to memberId, KEY_NICKNAME to nickname, KEY_ACCOUNT to account,
                KEY_URL to iconUrl, KEY_IS_OWNER to isOwner
            )
        }
    }
}