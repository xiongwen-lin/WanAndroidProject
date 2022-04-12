package com.apemans.tuya.component.ui.creategroup.plug

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
class PlugGroupFragment : GroupDeviceFragment() {

    private val viewModel: PlugGroupViewModel by requestViewModels()
    private val homeId: Long by arguments(KEY_HOME_ID, TuyaRepository.selectedHomeId)

//    override val list: LiveData<List<GroupDeviceBean>>
//        get() = viewModel.list

    override fun queryList() {
        submitList(viewModel.queryList(homeId, productId))
    }

    override val productId: String = SMART_PLUG_PRODUCT_ID

    companion object {
        private const val SMART_PLUG_PRODUCT_ID = "octeoqhuayzof69q"//teckin美规单插  SP10
        fun newInstance(homeId: Long) =
            PlugGroupFragment().withArguments(KEY_HOME_ID to homeId)

        private fun <T : Fragment> T.withArguments(vararg pairs: Pair<String, *>) = apply {
            arguments = bundleOf(*pairs)
        }
    }
}