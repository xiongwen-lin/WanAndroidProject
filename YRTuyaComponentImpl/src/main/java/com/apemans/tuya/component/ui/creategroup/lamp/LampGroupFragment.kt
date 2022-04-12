package com.apemans.tuya.component.ui.creategroup.lamp

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.apemans.yruibusiness.base.requestViewModels
import com.dylanc.longan.arguments
import com.apemans.tuya.component.constants.KEY_HOME_ID
import com.apemans.tuya.component.repository.TuyaRepository
import com.apemans.tuya.component.ui.creategroup.GroupDeviceFragment

/**
 * @author Dylan Cai
 */
class LampGroupFragment : GroupDeviceFragment() {

    private val viewModel: LampGroupViewModel by requestViewModels()
    private val homeId: Long by arguments(KEY_HOME_ID, TuyaRepository.selectedHomeId)

    override fun queryList() {
        submitList(viewModel.queryList(homeId, productId))
    }

    override val productId: String = SMART_LAMP_PRODUCT_ID_FOUR

    companion object {
        private const val SMART_LAMP_PRODUCT_ID_FOUR = "hdnoe1sqimwad9f4" //teckin美规灯 SB60

        fun newInstance(homeId: Long) = LampGroupFragment().withArguments(KEY_HOME_ID to homeId)

        private fun <T : Fragment> T.withArguments(vararg pairs: Pair<String, *>) = apply {
            arguments = bundleOf(*pairs)
        }
    }
}