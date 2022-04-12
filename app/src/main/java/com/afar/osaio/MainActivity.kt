package com.afar.osaio

import android.os.Bundle
import androidx.core.view.WindowInsetsControllerCompat
import com.afar.osaio.databinding.ActivityMainBinding
import com.afar.osaio.databinding.LayoutBottomTabBinding
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.utils.viewbinding.setCustomView
import com.apemans.messageapi.FRAGMENT_PATH_MESSAGE_LIST
import com.apemans.router.routerFragments
import com.apemans.smartipcapi.services.path.FRAGMENT_PATH_SMART_IPC_DEVICE_LIST
import com.apemans.tuyahome.api.FRAGMENT_PATH_MIX_HOME
import com.apemans.userapi.paths.FRAGMENT_PATH_ACCOUNT
import com.dylanc.longan.*
import com.dylanc.longan.design.FragmentStateAdapter
import com.dylanc.longan.design.setupWithViewPager2

//@Route(path = ACTIVITY_PATH_MAIN)
class MainActivity : com.apemans.yruibusiness.base.BaseComponentActivity<ActivityMainBinding>() {

    private val titleList = listOf(R.string.home, R.string.camera, R.string.message_center, R.string.mine)
    private val iconList = listOf(R.drawable.ic_menu_home_selector, R.drawable.ic_menu_ipc_selector, R.drawable.ic_menu_account_selector, R.drawable.ic_menu_account_selector)

    private val tuyaDevicesFragment by routerFragments(FRAGMENT_PATH_MIX_HOME)
    private val ipcDevicesFragment by routerFragments(FRAGMENT_PATH_SMART_IPC_DEVICE_LIST)
    private val messageFragment by routerFragments(FRAGMENT_PATH_MESSAGE_LIST)
    private val accountFragment by routerFragments(FRAGMENT_PATH_ACCOUNT)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        with(binding) {
            immerseStatusBar(lightMode = true)
            viewPager.adapter = FragmentStateAdapter(tuyaDevicesFragment, ipcDevicesFragment, messageFragment, accountFragment)
            tabLayout.setupWithViewPager2(viewPager, enableScroll = false) { tab, position ->
                tab.setCustomView<LayoutBottomTabBinding> {
                    tvTitle.setText(titleList[position])
                    ivIcon.contentDescription = getString(titleList[position])
                    ivIcon.setImageResource(iconList[position])
                }
            }
        }
        pressBackTwiceToExitApp("再点一次返回退出")
    }

    private fun immerseStatusBar(lightMode: Boolean) {
        decorFitsSystemWindows = false
        window.decorView.windowInsetsControllerCompat?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        transparentStatusBar()
        isLightStatusBar = lightMode
        binding.bottomCardView.addNavigationBarHeightToMarginBottom()
    }
}